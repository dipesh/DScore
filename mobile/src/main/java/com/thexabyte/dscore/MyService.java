package com.thexabyte.dscore;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.support.v7.app.NotificationCompat;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.thexabyte.mylibrary.*;

import java.util.ArrayList;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class MyService extends
        Service implements
        DataApi.DataListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    public static final String MY_SERVICE = "com.dipesh.MyService";

    public static SportsGame MY_GAME;

    private GoogleApiClient mGoogleApiClient;

    public MyService() {
        //super("MyService");
    }

    public int UPDATE_TIMER = 1000 * 30;

 //   public static final String SPORT_PATH = "/hockey";

//    private static final String SPORT = "com.dscore.sport";
//
//    private static final String HOME_TEAM_INITAL = "com.dscore.homeTeamInitial";
//    private static final String HOME_TEAM_CITY = "com.dscore.homeTeamCity";
//    private static final String HOME_TEAM_NAME = "com.dscore.homeTeamName";
//    private static final String HOME_TEAM_SCORE = "com.dscore.homeTeamScore";
//
//    private static final String AWAY_TEAM_INITAL = "com.dscore.awayTeamInitial";
//    private static final String AWAY_TEAM_CITY = "com.dscore.awayTeamCity";
//    private static final String AWAY_TEAM_NAME = "com.dscore.awayTeamName";
//    private static final String AWAY_TEAM_SCORE = "com.dscore.awayTeamScore";
//
//    private static final String GAME_TIME_OR_START_DATE = "com.dscore.gameTimeOrStartDate";
//    private static final String GAME_STATUS_OR_START_TIME = "com.dscore.gameStatusOrStartTime";

    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            //increaseCounter();
            getData();
            timerHandler.postDelayed(this, UPDATE_TIMER);
        }
    };

    public void getData() {
        HockeyGamesAsyncTask ldat = new HockeyGamesAsyncTask();
        ldat.execute();
    }

    public class HockeyGamesAsyncTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            //if(MY_GAME.sport == Sport.NHL) {
                return InternetTools.httpGet(MY_GAME.sport.url); //"http://www.nhl.com/");
//            }
//            else if(MY_GAME.sport == Sport.NBA){
//                return InternetTools.httpGet("http://espn.go.com/nba/bottomline/scores"); //"http://www.nhl.com/");
//            }
//            else if(MY_GAME.sport == Sport.MLS){
//                return InternetTools.httpGet("https://ca.sports.yahoo.com/soccer/mls/teams/"); //"http://www.nhl.com/");
//            }
//            else if(MY_GAME.sport == Sport.EURO_2016){
//                return InternetTools.httpGet("https://uk.sports.yahoo.com/football/euro-2016/teams/"); //"http://www.nhl.com/");
//            }
//            else {
//                return "";
//            }
        }

        @Override
        protected void onPostExecute(String result) {
            sendData(result);
        }
    }

    public void sendData(String result) {
        if (StaticMethods.isNetworkAvailable(this)) {

            ArrayList<SportsGame> sportsGames = SportsGameHandler.getGames(MY_GAME.sport, result);
            //String[] data = {};// SportsGame.getTeamData(MY_TEAM,result);

            String gameTimeOrStartDate = "";
            String gameStatusOrStartTime = "";
            String awayTeamInitial = "";
            String awayTeamCity = "";
            String awayTeamName = "";
            String awayTeamScore = "";
            String homeTeamInitial = "";
            String homeTeamCity = "";
            String homeTeamName = "";
            String homeTeamScore = "";

            for (SportsGame hg : sportsGames) {
                if (hg.awayTeam.initials.equals(MY_GAME.awayTeam.initials) &&
                        hg.homeTeam.initials.equals(MY_GAME.homeTeam.initials)) {
                    // data = new String[]{hg.gameTimeOrStartDate, hg.awayTeam.toString(), hg.awayTeam.score, hg.homeTeam.toString(), hg.homeTeam.score};
                    gameTimeOrStartDate = hg.gameTimeOrStartDate;
                    gameStatusOrStartTime = hg.gameStatusOrStartTime;
                    awayTeamInitial = hg.awayTeam.initials;
                    awayTeamCity = hg.awayTeam.city;
                    awayTeamName = hg.awayTeam.name;
                    awayTeamScore = hg.awayTeam.score;
                    homeTeamInitial = hg.homeTeam.initials;
                    homeTeamCity = hg.homeTeam.city;
                    homeTeamName = hg.homeTeam.name;
                    homeTeamScore = hg.homeTeam.score;
                    break;
                }
            }

            PutDataMapRequest putDataMapReq = PutDataMapRequest.create(SportsGame.SPORT_PATH);
            int value = MY_GAME.sport.id;
            putDataMapReq.getDataMap().putInt(SportsGame.SPORT, value);

            putDataMapReq.getDataMap().putString(SportsGame.HOME_TEAM_INITAL, homeTeamInitial);
            putDataMapReq.getDataMap().putString(SportsGame.HOME_TEAM_CITY, homeTeamCity);
            putDataMapReq.getDataMap().putString(SportsGame.HOME_TEAM_NAME, homeTeamName);
            putDataMapReq.getDataMap().putString(SportsGame.HOME_TEAM_SCORE, homeTeamScore);

            putDataMapReq.getDataMap().putString(SportsGame.AWAY_TEAM_INITAL, awayTeamInitial);
            putDataMapReq.getDataMap().putString(SportsGame.AWAY_TEAM_CITY, awayTeamCity);
            putDataMapReq.getDataMap().putString(SportsGame.AWAY_TEAM_NAME, awayTeamName);
            putDataMapReq.getDataMap().putString(SportsGame.AWAY_TEAM_SCORE, awayTeamScore);

            putDataMapReq.getDataMap().putString(SportsGame.GAME_TIME_OR_START_DATE, gameTimeOrStartDate);
            putDataMapReq.getDataMap().putString(SportsGame.GAME_STATUS_OR_START_TIME, gameStatusOrStartTime);

            PutDataRequest putDataReq = putDataMapReq.asPutDataRequest();
            PendingResult<DataApi.DataItemResult> pendingResult =
                    Wearable.DataApi.putDataItem(mGoogleApiClient, putDataReq);

            if (gameTimeOrStartDate.toUpperCase().contains("FINAL") || gameStatusOrStartTime.equals("FT")) { //gameTimeOrStartDate
                stopSelf();
            }
        } else {
            Toast.makeText(this, getResources().getString(R.string.network_connection_unavailable), Toast.LENGTH_LONG).show();
        }
    }

    private static final String COUNT_KEY = "com.example.key.count";
    private static final String COUNT_PATH = "/count";
    private int count;

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        for (DataEvent event : dataEvents) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                // DataItem changed
                DataItem item = event.getDataItem();
                if (item.getUri().getPath().compareTo("/count") == 0) {
                    DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();
                    // updateCount(dataMap.getInt(COUNT_KEY));
                }
            } else if (event.getType() == DataEvent.TYPE_DELETED) {
                // DataItem deleted
            }
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Wearable.DataApi.addListener(mGoogleApiClient, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    private void increaseCounter() {
        PutDataMapRequest putDataMapReq = PutDataMapRequest.create(COUNT_PATH);
        putDataMapReq.getDataMap().putInt(COUNT_KEY, count++);
        PutDataRequest putDataReq = putDataMapReq.asPutDataRequest();
        PendingResult<DataApi.DataItemResult> pendingResult =
                Wearable.DataApi.putDataItem(mGoogleApiClient, putDataReq);
    }

    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;

    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            // Normally we would do some work here, like download a file.
            // For our sample, we just sleep for 5 seconds.
//            long endTime = System.currentTimeMillis() + 5*1000;
//            while (System.currentTimeMillis() < endTime) {
//                synchronized (this) {
//                    try {
//                        wait(endTime - System.currentTimeMillis());
//                    } catch (Exception e) {
//                    }
//                }
//            }
            //increaseCounter();
            timerHandler.postDelayed(timerRunnable, 5000);
            // Stop the service using the startId, so that we don't stop
            // the service in the middle of handling another job
            //stopSelf(msg.arg1);
        }
    }

    @Override
    public void onCreate() {
        // Start up the thread running the service.  Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block.  We also make it
        // background priority so CPU-intensive work will not disrupt our UI.
        HandlerThread thread = new HandlerThread("ServiceStartArguments",
                Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        if (BuildConfig.DEBUG) {
            UPDATE_TIMER = 1000 * 5;
        }

        // Get the HandlerThread's Looper and use it for our Handler
        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);
    }

    public int ONGOING_NOTIFICATION_ID = 1;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();

        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = startId;
        mServiceHandler.sendMessage(msg);

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle(getText(R.string.app_name))
                .setSmallIcon(R.drawable.ic_watch_black_48dp)
                .setContentText(getText(R.string.foreground_service_text))
                .setContentIntent(pendingIntent).build();

        startForeground(ONGOING_NOTIFICATION_ID, notification);
        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Wearable.DataApi.removeListener(mGoogleApiClient, this);
        mGoogleApiClient.disconnect();
        stopForeground(true);
        super.onDestroy();
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
        //return mBinder;
    }


}
