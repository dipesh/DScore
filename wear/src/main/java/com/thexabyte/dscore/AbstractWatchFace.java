/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.thexabyte.dscore;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.wearable.watchface.CanvasWatchFaceService;
import android.support.wearable.watchface.WatchFaceStyle;
import android.text.format.Time;
import android.view.SurfaceHolder;
import android.view.WindowInsets;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;
import com.thexabyte.mylibrary.SportsGame;

import java.lang.ref.WeakReference;
import java.text.DateFormatSymbols;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Analog watch face with a ticking second hand. In ambient mode, the second hand isn't shown. On
 * devices with low-bit ambient mode, the hands are drawn without anti-aliasing in ambient mode.
 */
public class AbstractWatchFace extends CanvasWatchFaceService {
    /**
     * Update rate in milliseconds for interactive mode. We update once a second to advance the
     * second hand.
     */
    private static final long INTERACTIVE_UPDATE_RATE_MS = TimeUnit.SECONDS.toMillis(1);

    /**
     * Handler message id for updating the time periodically in interactive mode.
     */
    private static final int MSG_UPDATE_TIME = 0;

    AssetManager mAssetManager;

    @Override
    public Engine onCreateEngine() {
        mAssetManager = this.getApplicationContext().getAssets();
        return new Engine();
    }

    private static class EngineHandler extends Handler {
        private final WeakReference<AbstractWatchFace.Engine> mWeakReference;

        public EngineHandler(AbstractWatchFace.Engine reference) {
            mWeakReference = new WeakReference<>(reference);
        }

        @Override
        public void handleMessage(Message msg) {
            AbstractWatchFace.Engine engine = mWeakReference.get();
            if (engine != null) {
                switch (msg.what) {
                    case MSG_UPDATE_TIME:
                        engine.handleUpdateTimeMessage();
                        break;
                }
            }
        }
    }

    private class Engine extends CanvasWatchFaceService.Engine implements DataApi.DataListener,
            GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

        final Handler mUpdateTimeHandler = new EngineHandler(this);
        boolean mRegisteredTimeZoneReceiver = false;

        boolean mAmbient;
        String text = "DS4";
        String debugText = "";

        Time mTime;
        final BroadcastReceiver mTimeZoneReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mTime.clear(intent.getStringExtra("time-zone"));
                mTime.setToNow();
            }
        };
        int mTapCount;

        Bitmap mBackgroundBitmap;
        Bitmap mAmbientBackgroundBitmap;
        /**
         * Whether the display supports fewer bits for each color in ambient mode. When true, we
         * disable anti-aliasing in ambient mode.
         */
        boolean mLowBitAmbient;

        GoogleApiClient mGoogleApiClient;
        private static final String HOCKEY_PATH = "/hockey";

        SportsGame currentGame;

        Paint mTimeTextPaint;
        Paint mMonthPaint;
        Paint mAMPMPaint;
        Paint mDayPaint;

        Paint mPeriodTimeTextPaint;
        Paint mPeriodSuffixTextPaint;
        Paint mPeriodTextPaint;

        Paint mHomeScoreTextPaint;
        Paint mHomeCityTextPaint;
        Paint mHomeTeamNameTextPaint;
        Paint mHomeTextPaint;

        Paint mAwayScoreTextPaint;
        Paint mAwayCityTextPaint;
        Paint mAwayTeamNameTextPaint;
        Paint mAwayTextPaint;

        private final Typeface CITY_BOLB = Typeface.createFromAsset(mAssetManager,
                String.format(Locale.US, "fonts/%s", "CITYBOLB.TTF"));
        private final Typeface DIGITAL_7 = Typeface.createFromAsset(mAssetManager,
                String.format(Locale.US, "fonts/%s", "digital-7 (mono).ttf"));


        private final Typeface EUROSTILE_BOLD = Typeface.createFromAsset(mAssetManager,
                String.format(Locale.US, "fonts/%s", "eurostile-bold.ttf"));
        private final Typeface EUROSTILE_DEMI = Typeface.createFromAsset(mAssetManager,
                String.format(Locale.US, "fonts/%s", "eurostile-demi.ttf"));

        private final Typeface EUROSTILE_DEMI_BOLD = Typeface.create(EUROSTILE_DEMI, Typeface.BOLD);
        private final Typeface DIGITAL_7_ITALIC = Typeface.create(EUROSTILE_DEMI, Typeface.ITALIC);

        @Override
        public void onConnected(Bundle bundle) {
            Wearable.DataApi.addListener(mGoogleApiClient, Engine.this);
            text = "Connected";
        }

        @Override
        public void onConnectionSuspended(int i) {
            text = "suspended";
        }

        @Override
        public void onDataChanged(DataEventBuffer dataEvents) {
            // Loop through the events and send a message
            // to the node that created the data item.
            for (DataEvent event : dataEvents) {
                String uri = event.getDataItem().getUri().toString();

                if (uri.contains(HOCKEY_PATH)) {
                    DataMapItem dataItem = DataMapItem.fromDataItem(event.getDataItem());

                    SportsGame previousInstance = currentGame;
                    currentGame = new SportsGame(dataItem);

                    if (currentGame.gameScoreChanged(previousInstance) || currentGame.gameStatusChanged(previousInstance)) {
                        vibrate();
                    }
                }
            }
            invalidate();
        }

        @Override
        public void onConnectionFailed(ConnectionResult connectionResult) {
            text = "failed";
        }

        private void vibrate() {
            Vibrator v = (Vibrator) getBaseContext().getSystemService(VIBRATOR_SERVICE);
            v.vibrate(1000);
        }

        @Override
        public void onCreate(SurfaceHolder holder) {
            super.onCreate(holder);

            setWatchFaceStyle(new WatchFaceStyle.Builder(AbstractWatchFace.this)
                    .setCardPeekMode(WatchFaceStyle.PEEK_MODE_SHORT)
                    .setBackgroundVisibility(WatchFaceStyle.BACKGROUND_VISIBILITY_INTERRUPTIVE)
                    .setShowSystemUiTime(false)
                    .setAcceptsTapEvents(true)
                    .build());

            currentGame = new SportsGame(BuildConfig.DEBUG);

            mTime = new Time();
            mGoogleApiClient = new GoogleApiClient.Builder(AbstractWatchFace.this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(Wearable.API)
                    .build();
            mGoogleApiClient.connect();
        }

        @Override
        public void onDestroy() {
            mUpdateTimeHandler.removeMessages(MSG_UPDATE_TIME);
            super.onDestroy();
        }

        @Override
        public void onPropertiesChanged(Bundle properties) {
            super.onPropertiesChanged(properties);
            mLowBitAmbient = properties.getBoolean(PROPERTY_LOW_BIT_AMBIENT, false);
        }

        @Override
        public void onTimeTick() {
            super.onTimeTick();
            invalidate();
        }

        public Paint createPaint(int color, float size, Paint.Align align, Typeface typeface) {
            Paint paint = new Paint();
            paint.setColor(color);

            float ratio = 1.0f / 320f * mWidth;

            paint.setTextSize(size * ratio);
            paint.setTextAlign(align);
            paint.setTypeface(typeface);

            return paint;
        }

        @Override
        public void onAmbientModeChanged(boolean inAmbientMode) {
            super.onAmbientModeChanged(inAmbientMode);
            if (mAmbient != inAmbientMode) {
                mAmbient = inAmbientMode;

                //todo add the paints
                mTimeTextPaint.setAntiAlias(!mAmbient);
                mMonthPaint.setAntiAlias(!mAmbient);
                mAMPMPaint.setAntiAlias(!mAmbient);

                mPeriodTimeTextPaint.setAntiAlias(!mAmbient);
                mPeriodTextPaint.setAntiAlias(!mAmbient);

                mHomeScoreTextPaint.setAntiAlias(!mAmbient);
                mHomeCityTextPaint.setAntiAlias(!mAmbient);
                mHomeTeamNameTextPaint.setAntiAlias(!mAmbient);
                mHomeTextPaint.setAntiAlias(!mAmbient);

                mAwayScoreTextPaint.setAntiAlias(!mAmbient);
                mAwayCityTextPaint.setAntiAlias(!mAmbient);
                mAwayTeamNameTextPaint.setAntiAlias(!mAmbient);
                mAwayTextPaint.setAntiAlias(!mAmbient);

                invalidate();
            }

            // Whether the timer should be running depends on whether we're visible (as well as
            // whether we're in ambient mode), so we may need to start or stop the timer.
            updateTimer();
        }

        int mWidth;
        int mHeight;

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            mWidth = width;
            mHeight = height;

            super.onSurfaceChanged(holder, format, width, height);
        }

        boolean mIsRound;

        @Override
        public void onApplyWindowInsets(WindowInsets insets) {
            super.onApplyWindowInsets(insets);
            // Load resources that have alternate values for round watches.
            Resources resources = AbstractWatchFace.this.getResources();
            mIsRound = insets.isRound();

            if (mIsRound) {
                Drawable backgroundDrawable = resources.getDrawable(R.drawable.abstract_circle_640x640, null);
                mBackgroundBitmap = ((BitmapDrawable) backgroundDrawable).getBitmap();

                Drawable backgroundAmbientDrawable = resources.getDrawable(R.drawable.abstract_circle_ambient_mode_640x640, null);
                mAmbientBackgroundBitmap = ((BitmapDrawable) backgroundAmbientDrawable).getBitmap();
            } else {
                Drawable backgroundDrawable = resources.getDrawable(R.drawable.abstract_square_640x640, null);
                mBackgroundBitmap = ((BitmapDrawable) backgroundDrawable).getBitmap();

                Drawable backgroundAmbientDrawable = resources.getDrawable(R.drawable.abstract_square_ambient_mode_640x640, null);
                mAmbientBackgroundBitmap = ((BitmapDrawable) backgroundAmbientDrawable).getBitmap();
            }

            mBackgroundBitmap = Bitmap.createScaledBitmap(mBackgroundBitmap, mWidth, mHeight, true);
            mAmbientBackgroundBitmap = Bitmap.createScaledBitmap(mAmbientBackgroundBitmap, mWidth, mHeight, true);

            mTimeTextPaint = createPaint(resources.getColor(R.color.white), mIsRound ? 20 : 24, Paint.Align.RIGHT, EUROSTILE_DEMI);
            mMonthPaint = createPaint(resources.getColor(R.color.white), mIsRound ? 18 : 22, Paint.Align.LEFT, EUROSTILE_DEMI_BOLD);
            mDayPaint = createPaint(resources.getColor(R.color.white), mIsRound ? 18 : 22, Paint.Align.LEFT, EUROSTILE_DEMI);
            mAMPMPaint = createPaint(resources.getColor(R.color.white), mIsRound ? 14 : 18, Paint.Align.RIGHT, EUROSTILE_DEMI);

            mPeriodTimeTextPaint = createPaint(resources.getColor(R.color.abstract_red), mIsRound ? 56 : 56, Paint.Align.CENTER, DIGITAL_7);
            mPeriodTextPaint = createPaint(resources.getColor(R.color.white), mIsRound ? 50 : 50, Paint.Align.LEFT, DIGITAL_7);
            mPeriodSuffixTextPaint = createPaint(resources.getColor(R.color.white), mIsRound ? 20 : 20, Paint.Align.LEFT, DIGITAL_7);

            mHomeScoreTextPaint = createPaint(resources.getColor(R.color.black), mIsRound ? 30 : 36, Paint.Align.CENTER, EUROSTILE_DEMI);
            mHomeScoreTextPaintAmbient = createPaint(resources.getColor(R.color.white), mIsRound ? 30 : 36, Paint.Align.CENTER, EUROSTILE_DEMI);
            mHomeCityTextPaint = createPaint(resources.getColor(R.color.white), mIsRound ? 20 : 24, Paint.Align.CENTER, CITY_BOLB);
            mHomeTeamNameTextPaint = createPaint(resources.getColor(R.color.white), mIsRound ? 26 : 30, Paint.Align.CENTER, CITY_BOLB);
            mHomeTextPaint = createPaint(resources.getColor(R.color.abstract_red), mIsRound ? 8 : 10, Paint.Align.CENTER, EUROSTILE_DEMI);

            mAwayScoreTextPaint = createPaint(resources.getColor(R.color.abstract_red), mIsRound ? 30 : 36, Paint.Align.CENTER, EUROSTILE_DEMI);
            mAwayCityTextPaint = createPaint(resources.getColor(R.color.black), mIsRound ? 20 : 24, Paint.Align.CENTER, CITY_BOLB);
            mAwayTeamNameTextPaint = createPaint(resources.getColor(R.color.black), mIsRound ? 26 : 30, Paint.Align.CENTER, CITY_BOLB);
            mAwayCityTextPaintAmbient = createPaint(resources.getColor(R.color.white), mIsRound ? 20 : 24, Paint.Align.CENTER, CITY_BOLB);
            mAwayTeamNameTextPaintAmbient = createPaint(resources.getColor(R.color.white), mIsRound ? 26 : 30, Paint.Align.CENTER, CITY_BOLB);
            mAwayTextPaint = createPaint(resources.getColor(R.color.white), mIsRound ? 8 : 10, Paint.Align.CENTER, EUROSTILE_DEMI);
        }

        Paint mHomeScoreTextPaintAmbient;
        Paint mAwayCityTextPaintAmbient;
        Paint mAwayTeamNameTextPaintAmbient;

        @Override
        public void onDraw(Canvas canvas, Rect bounds) {
            mTime.setToNow();

            // Draw the background.
            if (isInAmbientMode()) {
                canvas.drawBitmap(mAmbientBackgroundBitmap, 0, 0, null);
            } else {
                canvas.drawBitmap(mBackgroundBitmap, 0, 0, null);
            }

            String amPm = mTime.hour >= 12 ? "PM" : "AM";
            int hour = mTime.hour > 12 ? mTime.hour - 12 : mTime.hour;
            if(hour == 0){
                hour = 12;
            }
            float ratio = 1.0f / 320f * bounds.width();

            String timeText = String.format("%d:%02d", hour, mTime.minute);
            //String timeText = "12:88";

            String month = new DateFormatSymbols().getMonths()[mTime.month];
            month = month.substring(0, 3);
            String monthDay = mTime.monthDay + "";
            String monthBold = month.toUpperCase();
//            float centerX = bounds.width() / 2f;
//            float centerY = bounds.height() / 2f;

            int amPmX = mIsRound ? 250 : 300;
            canvas.drawText(amPm, amPmX * ratio, (mIsRound ? 50 : 27) * ratio, mAMPMPaint);
            float amPmLength = mAMPMPaint.measureText(amPm);
            canvas.drawText(timeText, -amPmLength + ((amPmX - 2) * ratio), (mIsRound ? 50 : 27) * ratio, mTimeTextPaint);

            int monthX = mIsRound ? 75 : 30;
            canvas.drawText(monthBold, monthX * ratio, (mIsRound ? 48 : 28) * ratio, mMonthPaint);
            float monthLength = mMonthPaint.measureText(monthBold);
            canvas.drawText(monthDay, monthLength + ((monthX + 4) * ratio), (mIsRound ? 48 : 28) * ratio, mDayPaint);

            if (currentGame.gameTimeOrStartDate.contains(":")) { //is time

                mPeriodTimeTextPaint.setTextSize((mIsRound ? 58 : 60) * ratio);
                canvas.drawText(currentGame.gameTimeOrStartDate, (mIsRound ? 133 : 125) * ratio, (mIsRound ? 180 : 185) * ratio, mPeriodTimeTextPaint);

                String period = currentGame.gameStatusOrStartTime.substring(0, currentGame.gameStatusOrStartTime.length() - 2);
                String periodSuffix = currentGame.gameStatusOrStartTime.substring(currentGame.gameStatusOrStartTime.length() - 2, currentGame.gameStatusOrStartTime.length());

                mPeriodSuffixTextPaint.setTextSize((mIsRound ? 20 : 20) * ratio);
                mPeriodSuffixTextPaint.setTextAlign(Paint.Align.LEFT);
                int periodX = (mIsRound ? 208 : 215);
                canvas.drawText(period, periodX * ratio, (mIsRound ? 178 : 182) * ratio, mPeriodTextPaint);
                float periodLength = mPeriodTextPaint.measureText(period);
                canvas.drawText(periodSuffix, periodLength + ((periodX + 0.5f) * ratio), (mIsRound ? 160 : 163) * ratio, mPeriodSuffixTextPaint);
            }
            else if (currentGame.gameStatusOrStartTime.contains("No Game")) {
                mPeriodTimeTextPaint.setTextSize((mIsRound ? 36 : 40) * ratio);
                canvas.drawText(currentGame.gameStatusOrStartTime, (mIsRound ? 135 : 135) * ratio, (mIsRound ? 170 : 178) * ratio, mPeriodTimeTextPaint);

            }
            else if (currentGame.gameStatusOrStartTime.contains("AM")
                    || currentGame.gameStatusOrStartTime.contains("PM")
                    || currentGame.gameStatusOrStartTime.contains("No Game")){
                mPeriodTimeTextPaint.setTextSize((mIsRound ? 32 : 40) * ratio);
                canvas.drawText(currentGame.gameTimeOrStartDate, (mIsRound ? 135 : 135) * ratio, (mIsRound ? 170 : 178) * ratio, mPeriodTimeTextPaint);

                String time = currentGame.gameStatusOrStartTime.split(" ")[0];
                String ampm = currentGame.gameStatusOrStartTime.split(" ")[1];
                mPeriodSuffixTextPaint.setTextSize((mIsRound ? 22 : 24) * ratio);
                mPeriodSuffixTextPaint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText(time, (mIsRound ? 230 : 240) * ratio, (mIsRound ? 157 : 165) * ratio, mPeriodSuffixTextPaint);
                canvas.drawText(ampm, (mIsRound ? 230 : 240) * ratio, (mIsRound ? 177 : 185) * ratio, mPeriodSuffixTextPaint);
            }
            else {
                mPeriodTimeTextPaint.setTextSize((mIsRound ? 36 : 40) * ratio);
                canvas.drawText(currentGame.gameTimeOrStartDate, (mIsRound ? 135 : 135) * ratio, (mIsRound ? 170 : 178) * ratio, mPeriodTimeTextPaint);

                mPeriodSuffixTextPaint.setTextAlign(Paint.Align.CENTER);

                if(currentGame.gameStatusOrStartTime.length() == 2) {
                    mPeriodSuffixTextPaint.setTextSize((mIsRound ? 30 : 30) * ratio);
                }
                else if(currentGame.gameStatusOrStartTime.length() == 3) {
                    mPeriodSuffixTextPaint.setTextSize((mIsRound ? 27 : 27) * ratio);
                }
                else {
                    mPeriodSuffixTextPaint.setTextSize((mIsRound ? 24 : 24) * ratio);
                }
                canvas.drawText(currentGame.gameStatusOrStartTime, (mIsRound ? 230 : 240) * ratio, (mIsRound ? 167 : 175) * ratio, mPeriodSuffixTextPaint);
            }

//            //away is on the top of the screen
            canvas.drawText(currentGame.awayTeam.city.toUpperCase(), (mIsRound ? 110 : 110) * ratio, (mIsRound ? 80 : 80) * ratio, mAmbient ? mAwayCityTextPaintAmbient :mAwayCityTextPaint);
            canvas.drawText(currentGame.awayTeam.name.toUpperCase(), (mIsRound ? 110 : 110) * ratio, (mIsRound ? 100 : 105) * ratio,mAmbient ? mAwayTeamNameTextPaintAmbient : mAwayTeamNameTextPaint);
            canvas.drawText(currentGame.awayTeam.score.toUpperCase(), (mIsRound ? 252 : 265) * ratio, (mIsRound ? 110 : 100) * ratio, mAwayScoreTextPaint);
            canvas.drawText("AWAY", (mIsRound ? 262 : 275) * ratio, (mIsRound ? 120 : 115) * ratio, mAwayTextPaint);
//
//            //home is on the bottom of the screen
            canvas.drawText(currentGame.homeTeam.city.toUpperCase(), (mIsRound ? 200 : 200) * ratio, (mIsRound ? 240 : 240) * ratio, mHomeCityTextPaint);
            canvas.drawText(currentGame.homeTeam.name.toUpperCase(), (mIsRound ? 200 : 200) * ratio, (mIsRound ? 260 : 265) * ratio, mHomeTeamNameTextPaint);
            canvas.drawText(currentGame.homeTeam.score.toUpperCase(), (mIsRound ? 68 : 53) * ratio, (mIsRound ? 230 : 255) * ratio, mAmbient ? mHomeScoreTextPaintAmbient : mHomeScoreTextPaint);
            canvas.drawText("HOME", (mIsRound ? 58 : 45) * ratio, (mIsRound ? 204 : 222) * ratio, mHomeTextPaint);

            if (BuildConfig.DEBUG) {
//                canvas.drawText(debugText, 25 * ratio, 200 * ratio, mTextStrokePaint);
//                canvas.drawText(debugText, 25 * ratio, 200 * ratio, mTextPaint);
            }

        }

        float mXOffset = 0;
        float mYOffset = 0;

        /**
         * Captures tap event (and tap type) and toggles the background color if the user finishes
         * a tap.
         */
        @Override
        public void onTapCommand(int tapType, int x, int y, long eventTime) {
            Resources resources = AbstractWatchFace.this.getResources();
            switch (tapType) {
                case TAP_TYPE_TOUCH:
                    if(BuildConfig.DEBUG) {
                        currentGame.changeData();
                    }
                    break;
                case TAP_TYPE_TOUCH_CANCEL:
                    // The user has started a different gesture or otherwise cancelled the tap.
                    break;
                case TAP_TYPE_TAP:
                    // The user has completed the tap gesture.
                    mYOffset -= 5;
                    break;
            }
            invalidate();
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);

            if (visible) {
                registerReceiver();

                // Update time zone in case it changed while we weren't visible.
                mTime.clear(TimeZone.getDefault().getID());
                mTime.setToNow();

                //mLoadDataHandler.sendEmptyMessage(MSG_LOAD_DATA);
            } else {
                //mLoadDataHandler.removeMessages(MSG_LOAD_DATA);
                unregisterReceiver();
            }

            // Whether the timer should be running depends on whether we're visible (as well as
            // whether we're in ambient mode), so we may need to start or stop the timer.
            updateTimer();
        }

        private void registerReceiver() {
            if (mRegisteredTimeZoneReceiver) {
                return;
            }
            mRegisteredTimeZoneReceiver = true;
            IntentFilter filter = new IntentFilter(Intent.ACTION_TIMEZONE_CHANGED);
            AbstractWatchFace.this.registerReceiver(mTimeZoneReceiver, filter);
        }

        private void unregisterReceiver() {
            if (!mRegisteredTimeZoneReceiver) {
                return;
            }
            mRegisteredTimeZoneReceiver = false;
            AbstractWatchFace.this.unregisterReceiver(mTimeZoneReceiver);
        }

        /**
         * Starts the {@link #mUpdateTimeHandler} timer if it should be running and isn't currently
         * or stops it if it shouldn't be running but currently is.
         */
        private void updateTimer() {
            mUpdateTimeHandler.removeMessages(MSG_UPDATE_TIME);
            if (shouldTimerBeRunning()) {
                mUpdateTimeHandler.sendEmptyMessage(MSG_UPDATE_TIME);
            }
        }

        /**
         * Returns whether the {@link #mUpdateTimeHandler} timer should be running. The timer should
         * only run when we're visible and in interactive mode.
         */
        private boolean shouldTimerBeRunning() {
            return isVisible() && !isInAmbientMode();
        }

        /**
         * Handle updating the time periodically in interactive mode.
         */
        private void handleUpdateTimeMessage() {
            invalidate();
            if (shouldTimerBeRunning()) {
                long timeMs = System.currentTimeMillis();
                long delayMs = INTERACTIVE_UPDATE_RATE_MS
                        - (timeMs % INTERACTIVE_UPDATE_RATE_MS);
                mUpdateTimeHandler.sendEmptyMessageDelayed(MSG_UPDATE_TIME, delayMs);
            }
        }
    }
}