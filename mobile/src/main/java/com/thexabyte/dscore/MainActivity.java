package com.thexabyte.dscore;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.facebook.appevents.AppEventsLogger;
import com.thexabyte.mylibrary.Sport;
import com.thexabyte.util.IabHelper;
import com.thexabyte.util.IabResult;
import com.thexabyte.util.Inventory;
import com.thexabyte.util.Purchase;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

import com.facebook.FacebookSdk;
import android.provider.Settings.Secure;

public class MainActivity extends AppCompatActivity {

    private String myString8 = "Jc+ZUsKjjN1DrRfrH2zJSYqXIfIofXcvZ0nZ3kHePbzasrYS06dfrWJPHi5Wid9VP9wIDAQAB";

    private static SharedPreferences sharedPref;


    private LeaguesFragment leaguesFragment;
    private GamesFragment gamesFragment;
    private AllGamesFragment myTeamsFragment;
    private LinearLayout mainLayout;
    private EditText promoCodeEditText;

    private static MainActivity currentInstance;

    public static void saveFavorites(ArrayList<HashSet<String>> favoritesList) {

        for (int i = 0; i < Sport.allSports.length; i++) {

            sharedPref.edit().putStringSet(Sport.allSports[i].name, favoritesList.get(i)).apply();
        }
    }

    public static ArrayList<HashSet<String>> getAllFavorites() {
        ArrayList<HashSet<String>> allFavorites = new ArrayList<>();
        for (Sport s : Sport.allSports) {
            allFavorites.add(getFavorite(s.name));
        }
        return allFavorites;
    }

    public static HashSet<String> getFavorite(String favorite) {
        if (sharedPref != null) {
            return (HashSet<String>) sharedPref.getStringSet(favorite,
                    new HashSet<String>()); //default is empty set
        } else {
            return new HashSet<>();
        }
    }

    public void toast(String msg) {
        Toast.makeText(this, (String) msg, Toast.LENGTH_SHORT).show();
    }

    /******************************************
     * In App Billing
     ******************************************/
    public String SKU_SUBSCRIPTION_YEARLY = "yearly";
    public String SKU_SUBSCRIPTION_YEARLY2 = "yearly2";
    public String SKU_SUBSCRIPTION_YEARLY3 = "yearly3";
    public String SKU_AUGUST_2016_PROMO = "august2016promo";
    public String SKU_JULY_2016_PROMO = "july2016promo";

    public boolean mHasSubscription = false;

    private String android_id;
    private int trialDaysPassed;
    private boolean trialExists;

    IabHelper mHelper;
    IabHelper.QueryInventoryFinishedListener
            mQueryFinishedListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            if (result.isFailure()) {
                // handle error
                return;
            }
            //String details = inventory.getSkuDetails(SKU_SUBSCRIPTION_YEARLY).toString();

            mHasSubscription =
                    inventory.hasPurchase(SKU_SUBSCRIPTION_YEARLY3)
                    || inventory.hasPurchase(SKU_SUBSCRIPTION_YEARLY2)
                    || inventory.hasPurchase(SKU_SUBSCRIPTION_YEARLY)
                    || inventory.hasPurchase(SKU_AUGUST_2016_PROMO)
                    || inventory.hasPurchase(SKU_JULY_2016_PROMO)
                    || trialDaysPassed <= 10;

            // update the UI
            if (!mHasSubscription) {
                showSubscribeLayout();
                if(trialDaysPassed > 10){
                    toast("Trial has expired!");
                }
            } else {
                showMainLayout();
            }
            mainLayout.invalidate();
        }
    };

    public void showStartTrialLayout(){
        viewPager.setVisibility(View.GONE);
        tabsStrip.setVisibility(View.GONE);
        subscribeLayout.setVisibility(View.GONE);
        startTrialLayout.setVisibility(View.VISIBLE);
    }
    public void showSubscribeLayout(){
        viewPager.setVisibility(View.GONE);
        tabsStrip.setVisibility(View.GONE);
        subscribeLayout.setVisibility(View.VISIBLE);
        startTrialLayout.setVisibility(View.GONE);
    }
    public void showMainLayout(){
        viewPager.setVisibility(View.VISIBLE);
        tabsStrip.setVisibility(View.VISIBLE);
        subscribeLayout.setVisibility(View.GONE);
        startTrialLayout.setVisibility(View.GONE);
    }
    public void startTrialClick(View view) {
        startTrial();
    }
    private void startTrial() {
        new GetTrialDaysRemainingTask().execute(android_id);
    }
    public void purchaseYearlySubscriptionClick(View view) {
        purchaseYearlySubscription();
    }

    private void purchaseYearlySubscription() {
        String promoCode = promoCodeEditText.getText().toString();

        Date aug30 = new Date(1472601599000l);
        Date july30 = new Date(1469923199000l);
        Date now = new Date();

        try {
            if (promoCode.equals("PDS10AUG") && now.getTime() < aug30.getTime()) {
                mHelper.launchPurchaseFlow(this, SKU_AUGUST_2016_PROMO, 10103,
                        mPurchaseFinishedListener, "");
            } else if (promoCode.equals("PromoDS20") && now.getTime() < july30.getTime()) {
                mHelper.launchPurchaseFlow(this, SKU_JULY_2016_PROMO, 10104,
                        mPurchaseFinishedListener, "");
            } else if (promoCode.equals("")) {
                mHelper.launchPurchaseFlow(this, SKU_SUBSCRIPTION_YEARLY3, 10102,
                        mPurchaseFinishedListener, "");
            } else {
                toast("Promo code is invalid");
            }

        } catch (IllegalStateException e) {
            mHelper.flagEndAsync();
            purchaseYearlySubscription();
        }
    }

    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener
            = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            if (result.isFailure()) {
                return;
            } else if (purchase.getSku().equals(SKU_JULY_2016_PROMO) ||
                    purchase.getSku().equals(SKU_AUGUST_2016_PROMO) ||
                    purchase.getSku().equals(SKU_SUBSCRIPTION_YEARLY)||
                    purchase.getSku().equals(SKU_SUBSCRIPTION_YEARLY2)||
                    purchase.getSku().equals(SKU_SUBSCRIPTION_YEARLY3)) {
                showMainLayout();
            }

            mainLayout.invalidate();
        }
    };

    public void setupInAppBilling() {
        String myString = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQE";
        String myString2 = "ArDSJTYO+Kya4Wo0VwoK0bv/rM14YDsokBbTct5SJm6U08mv7HdMG4jhZhGQPIPVjWl3Xnnqzi69Pc7F4JovdS9RSkpIdfV3w2pR9o/C";
        String myString3 = "8vWAlwmMyai5ybMEUOvcixC";
        String myString4 = "+u5xf+BGlTZ5o/Ux7/G8CScUTScMBcoT/PhNo9ReAvk29XdFtpjKr";
        String myString5 = "cK1s5llojKSQ0ZDA6wPhoXLrlj9SsiizX";
        String myString6 = "+7DzRmOtai9+bM8kjpV0e8DXCTyJJII17CE9lO7IXp+oVwF2JwLsAX2wPoDvzk0";
        String key2 = myString + myString2 + myString3 + myString4 + myString5 + myString6 + myString8;

        // compute your public key and store it in base64EncodedPublicKey
        mHelper = new IabHelper(this, key2);
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                if (!result.isSuccess()) {
                    // Oh noes, there was a problem.
                    toast("start setup failed");
                }
                // Hooray, IAB is fully set up!
                ArrayList additionalSkuList = new ArrayList();
                ArrayList additionalSkuSubsList = new ArrayList();
                additionalSkuSubsList.add(SKU_SUBSCRIPTION_YEARLY3);
                additionalSkuSubsList.add(SKU_SUBSCRIPTION_YEARLY2);
                additionalSkuSubsList.add(SKU_SUBSCRIPTION_YEARLY);
                mHelper.queryInventoryAsync(true, additionalSkuList, additionalSkuSubsList,
                        mQueryFinishedListener);
            }
        });
    }

    ViewPager viewPager;
    PagerSlidingTabStrip tabsStrip;
    LinearLayout subscribeLayout;
    LinearLayout startTrialLayout;
    SampleFragmentPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //there is a bug where a certain device has all the same ID...
        android_id = Secure.getString(this.getContentResolver(), Secure.ANDROID_ID);

        setContentView(R.layout.view_pager);
        currentInstance = this;

        mainLayout = (LinearLayout) findViewById(R.id.mainLayout);
        promoCodeEditText = (EditText) findViewById(R.id.promoCodeEditText);

        // ViewPager and its adapters use support library
        // fragments, so use getSupportFragmentManager.
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        viewPager = (ViewPager) findViewById(R.id.pager);
        adapter = new SampleFragmentPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        subscribeLayout = (LinearLayout) findViewById(R.id.subscribeLayout);
        startTrialLayout = (LinearLayout) findViewById(R.id.startTrialLayout);
        // Give the PagerSlidingTabStrip the ViewPager
        tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        // Attach the view pager to the tab strip
        tabsStrip.setViewPager(viewPager);

        // Attach the page change listener to tab strip and **not** the view pager inside the activity
        tabsStrip.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            // This method will be invoked when a new page becomes selected.
            @Override
            public void onPageSelected(int position) {
                if (position == 1) {
                    //allGamesFragment.updateList();
                } else if (position == 2) {
                    myTeamsFragment.updateList();
                }
            }

            // This method will be invoked when the current page is scrolled
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Code goes here
            }

            // Called when the scroll state changes:
            // SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING
            @Override
            public void onPageScrollStateChanged(int state) {
                // Code goes here
            }
        });

        sharedPref = this.getPreferences(this.MODE_PRIVATE);

        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/Walkway rounded.ttf");
        SpannableStringBuilder SS = new SpannableStringBuilder("DSCORE");
        SS.setSpan(new CustomTypefaceSpan("", tf), 0, SS.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        this.getSupportActionBar().setTitle(SS);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(getApplication());

        new TrialExistsTask(this).execute(android_id);
    }

    private String tabTitles[] = new String[]{"Sports", "Games", "My Teams"};

    public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {
        final int PAGE_COUNT = 3;

        public SampleFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                leaguesFragment = LeaguesFragment.newInstance();
                return leaguesFragment;
            } else if (position == 1) {
                gamesFragment = GamesFragment.newInstance();
                return gamesFragment;
            } else {
                myTeamsFragment = AllGamesFragment.newInstance(true);
                return myTeamsFragment;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            return tabTitles[position];
        }
    }

    public void setSport(Sport sport) {
        gamesFragment.setCurrentSport(sport);
        tabTitles[1] = sport.name;
        adapter.notifyDataSetChanged();
        tabsStrip.notifyDataSetChanged();
        viewPager.setCurrentItem(1, true);
    }

    public int getCurrentPage() {
        return  viewPager.getCurrentItem();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_favorite:
                Intent favoriteIntent = new Intent(this, FavoritesActivity.class);
                startActivity(favoriteIntent);
                return true;
            case R.id.action_rate:
                Uri uri = Uri.parse("market://details?id=" + this.getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_DOCUMENT | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + this.getPackageName())));
                }
                return true;

            case R.id.action_feedback:
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto: thexabyte@gmail.com"));
                startActivity(Intent.createChooser(emailIntent, "Send feedback"));
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    public static Intent myServiceIntent;

    public static void startService() {
        myServiceIntent = new Intent(currentInstance, MyService.class);
        currentInstance.startService(myServiceIntent);
    }

    public static void stopService() {
        if (myServiceIntent != null) {
            currentInstance.stopService(myServiceIntent);
        }
    }

    public static void restartService() {
        stopService();
        startService();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService();

        if (mHelper != null) mHelper.dispose();
        mHelper = null;

    }

    public class GetTrialDaysRemainingTask extends AsyncTask<String,Void,String> {
        String url = "http://thexabyte.com/GetTrialDaysRemaining.php";
        //flag 0 means get and 1 means post.(By default it is get.)
        public GetTrialDaysRemainingTask() {
        }

        protected void onPreExecute(){

        }

        @Override
        protected String doInBackground(String... arg0) {
            return httpRequest(url + "?id=" + arg0[0]);
        }


        @Override
        protected void onPostExecute(String result){
            try {
                trialDaysPassed = Integer.parseInt(result);
            } catch (Exception e) {
                //if there is an error then assume trial was complete
                trialDaysPassed = 11;
            }
            setupInAppBilling();
        }
    }
    public class TrialExistsTask extends AsyncTask<String,Void,String> {
        private Context context;
        String url = "http://thexabyte.com/TrialExists.php";
        //flag 0 means get and 1 means post.(By default it is get.)
        public TrialExistsTask(Context context) {
            this.context = context;
        }

        protected void onPreExecute(){

        }

        @Override
        protected String doInBackground(String... arg0) {
            return httpRequest(url + "?id=" + arg0[0]);
        }


        @Override
        protected void onPostExecute(String result){
            try {
                trialExists = Boolean.parseBoolean(result);
            } catch (Exception e) {
                //if there is an error then assume trial never started
                trialExists = false;
            }
            if(!trialExists){
                //ask them to click the start trial button
                showStartTrialLayout();
            }
            else {
                //find out how many days of the trial is left
                new GetTrialDaysRemainingTask().execute(android_id);
            }
            //setupInAppBilling();
        }
    }

    public String httpRequest(String link){
        try{
            URL url = new URL(link);
            URLConnection conn = url.openConnection();

            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

            wr.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while((line = reader.readLine()) != null)
            {
                sb.append(line);
                break;
            }
            return sb.toString();
        }
        catch(Exception e){
            return new String("Exception: " + e.getMessage());
        }
    }
}
