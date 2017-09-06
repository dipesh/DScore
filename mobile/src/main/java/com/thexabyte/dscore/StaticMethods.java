package com.thexabyte.dscore;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.widget.ExpandableListView;

/**
 * Created by Dipesh on 7/4/2016.
 */
public class StaticMethods {

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static int GetPixelFromDips(float pixels, Activity activity) {
        // Get the screen's density scale
        final float scale = activity.getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }

    public static void indicatorOnRight(ExpandableListView expListView, Activity activity){
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            expListView.setIndicatorBounds(width-GetPixelFromDips(35,activity), width-GetPixelFromDips(5,activity));
        } else {
            expListView.setIndicatorBoundsRelative(width-GetPixelFromDips(35,activity), width-GetPixelFromDips(5,activity));
        }
    }
}
