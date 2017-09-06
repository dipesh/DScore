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
public class AbacusWatchFace extends CanvasWatchFaceService {
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
        private final WeakReference<AbacusWatchFace.Engine> mWeakReference;

        public EngineHandler(AbacusWatchFace.Engine reference) {
            mWeakReference = new WeakReference<>(reference);
        }

        @Override
        public void handleMessage(Message msg) {
            AbacusWatchFace.Engine engine = mWeakReference.get();
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
        Paint mHourPaint;
        Paint mDayPaint;

        Paint mPeriodTimeTextPaint;
        Paint mPeriodSuffixTextPaint;
        Paint mPeriodTextPaint;
        Paint mPeriodTimeTextPaintAmbient;
        Paint mPeriodSuffixTextPaintAmbient;
        Paint mPeriodTextPaintAmbient;

        Paint mHomeScoreTextPaint;
        Paint mHomeCityTextPaint;
        Paint mHomeTeamNameTextPaint;
        Paint mHomeTextPaint;

        Paint mAwayScoreTextPaint;
        Paint mAwayCityTextPaint;
        Paint mAwayTeamNameTextPaint;
        Paint mAwayTextPaint;

        Paint mAwayCityTextPaintAmbient;
        Paint mAwayTeamNameTextPaintAmbient;
        Paint mAwayTextPaintAmbient;

        private final Typeface AVENIR_HEAVY = Typeface.createFromAsset(mAssetManager,
                String.format(Locale.US, "fonts/%s", "AvenirLTStd-Heavy.otf"));
        private final Typeface AVENIR_HEAVY_OBLIQUE = Typeface.createFromAsset(mAssetManager,
                String.format(Locale.US, "fonts/%s", "AvenirLTStd-HeavyOblique.otf"));
        private final Typeface AVENIR_LIGHT = Typeface.createFromAsset(mAssetManager,
                String.format(Locale.US, "fonts/%s", "AvenirLTStd-Light.otf"));
        private final Typeface AVENIR_OBLIQUE = Typeface.createFromAsset(mAssetManager,
                String.format(Locale.US, "fonts/%s", "AvenirLTStd-Oblique.otf"));
        private final Typeface VERVE = Typeface.createFromAsset(mAssetManager,
                String.format(Locale.US, "fonts/%s", "Verve-Std.ttf"));
        private final Typeface VERVE_BOLD = Typeface.createFromAsset(mAssetManager,
                String.format(Locale.US, "fonts/%s", "VerveStd-Bold.otf"));

        private final Typeface AVENIR_LIGHT_BOLD = Typeface.create(AVENIR_LIGHT, Typeface.BOLD);
        private final Typeface AVENIR_OBLIQUE_BOLD = Typeface.create(AVENIR_OBLIQUE, Typeface.BOLD);
        private final Typeface VERVE_BOLD_BOLD = Typeface.create(VERVE_BOLD, Typeface.BOLD);

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

            setWatchFaceStyle(new WatchFaceStyle.Builder(AbacusWatchFace.this)
                    .setCardPeekMode(WatchFaceStyle.PEEK_MODE_SHORT)
                    .setBackgroundVisibility(WatchFaceStyle.BACKGROUND_VISIBILITY_INTERRUPTIVE)
                    .setShowSystemUiTime(false)
                    .setAcceptsTapEvents(true)
                    .build());

            currentGame = new SportsGame(BuildConfig.DEBUG);

            mTime = new Time();
            mGoogleApiClient = new GoogleApiClient.Builder(AbacusWatchFace.this)
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
            Resources resources = AbacusWatchFace.this.getResources();
            mIsRound = insets.isRound();

            if (mIsRound) {
                Drawable backgroundDrawable = resources.getDrawable(R.drawable.abacus_circle_640x640, null);
                mBackgroundBitmap = ((BitmapDrawable) backgroundDrawable).getBitmap();

                Drawable backgroundAmbientDrawable = resources.getDrawable(R.drawable.abacus_circle_ambient_mode, null);
                mAmbientBackgroundBitmap = ((BitmapDrawable) backgroundAmbientDrawable).getBitmap();
            } else {
                Drawable backgroundDrawable = resources.getDrawable(R.drawable.abacus_square_640x640, null);
                mBackgroundBitmap = ((BitmapDrawable) backgroundDrawable).getBitmap();

                Drawable backgroundAmbientDrawable = resources.getDrawable(R.drawable.abacus_square_ambient_mode, null);
                mAmbientBackgroundBitmap = ((BitmapDrawable) backgroundAmbientDrawable).getBitmap();
            }

            mBackgroundBitmap = Bitmap.createScaledBitmap(mBackgroundBitmap, mWidth, mHeight, true);
            mAmbientBackgroundBitmap = Bitmap.createScaledBitmap(mAmbientBackgroundBitmap, mWidth, mHeight, true);

            mTimeTextPaint = createPaint(resources.getColor(R.color.abacus_red), mIsRound ? 52 : 52, Paint.Align.CENTER, AVENIR_LIGHT);

            mHourPaint = createPaint(resources.getColor(R.color.abacus_red), mIsRound ? 52 : 52, Paint.Align.LEFT, AVENIR_LIGHT_BOLD);
            mMonthPaint = createPaint(resources.getColor(R.color.white), mIsRound ? 24 : 24, Paint.Align.LEFT, AVENIR_OBLIQUE_BOLD);
            mDayPaint = createPaint(resources.getColor(R.color.white), mIsRound ? 24 : 24, Paint.Align.LEFT, AVENIR_OBLIQUE);
            mAMPMPaint = createPaint(resources.getColor(R.color.abacus_green), mIsRound ? 24 : 24, Paint.Align.RIGHT, AVENIR_HEAVY);

            periodTimeFontSize = mIsRound ? 68 : 68;
            mPeriodTimeTextPaint = createPaint(resources.getColor(R.color.abacus_grey), periodTimeFontSize, Paint.Align.CENTER, VERVE_BOLD_BOLD);
            mPeriodTextPaint = createPaint(resources.getColor(R.color.abacus_grey), mIsRound ? 50 : 50, Paint.Align.CENTER, VERVE_BOLD_BOLD);
            mPeriodSuffixTextPaint = createPaint(resources.getColor(R.color.abacus_grey), mIsRound ? 24 : 20, Paint.Align.CENTER, VERVE);

            mPeriodTimeTextPaintAmbient = createPaint(resources.getColor(R.color.white), periodTimeFontSize, Paint.Align.CENTER, VERVE_BOLD_BOLD);
            mPeriodTextPaintAmbient = createPaint(resources.getColor(R.color.white), mIsRound ? 50 : 50, Paint.Align.CENTER, VERVE_BOLD_BOLD);
            mPeriodSuffixTextPaintAmbient = createPaint(resources.getColor(R.color.white), mIsRound ? 24 : 20, Paint.Align.CENTER, VERVE);


            mHomeScoreTextPaint = createPaint(resources.getColor(R.color.black), mIsRound ? 42 : 42, Paint.Align.CENTER, VERVE_BOLD);
            mHomeCityTextPaint = createPaint(resources.getColor(R.color.white), mIsRound ? 18 : 22, Paint.Align.RIGHT, VERVE_BOLD);
            mHomeTeamNameTextPaint = createPaint(resources.getColor(R.color.white), mIsRound ? 20 : 24, Paint.Align.RIGHT, VERVE_BOLD);
            mHomeTextPaint = createPaint(resources.getColor(R.color.white), mIsRound ? 14 : 16, Paint.Align.RIGHT, AVENIR_HEAVY_OBLIQUE);

            mAwayScoreTextPaint = createPaint(resources.getColor(R.color.white), mIsRound ? 42 : 42, Paint.Align.CENTER, VERVE_BOLD);
            mAwayCityTextPaint = createPaint(resources.getColor(R.color.black), mIsRound ? 18 : 22, Paint.Align.LEFT, VERVE_BOLD);
            mAwayTeamNameTextPaint = createPaint(resources.getColor(R.color.black), mIsRound ? 20 : 24, Paint.Align.LEFT, VERVE_BOLD);
            mAwayTextPaint = createPaint(resources.getColor(R.color.black), mIsRound ? 14 : 16, Paint.Align.LEFT, AVENIR_HEAVY_OBLIQUE);

            mAwayCityTextPaintAmbient = createPaint(resources.getColor(R.color.white), mIsRound ? 18 : 22, Paint.Align.LEFT, VERVE_BOLD);
            mAwayTeamNameTextPaintAmbient = createPaint(resources.getColor(R.color.white), mIsRound ? 20 : 24, Paint.Align.LEFT, VERVE_BOLD);
            mAwayTextPaintAmbient = createPaint(resources.getColor(R.color.white), mIsRound ? 14 : 16, Paint.Align.LEFT, AVENIR_HEAVY_OBLIQUE);

        }

        int periodTimeFontSize;

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

            String month = new DateFormatSymbols().getMonths()[mTime.month];
            month = month.substring(0, 3);
            String monthText = month.toUpperCase() + " " + mTime.monthDay;
            String monthBold = month.toUpperCase();
            float centerX = bounds.width() / 2f;
            //float centerY = bounds.height() / 2f;

            //if (mIsRound) {
            int timeY = mIsRound ? 203 : 208;
            mTimeTextPaint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(":",
                    centerX, timeY * ratio,
                    mTimeTextPaint);
            mHourPaint.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText(timeText.split(":")[0],
                    155 * ratio, timeY * ratio,
                    mHourPaint);
            mTimeTextPaint.setTextAlign(Paint.Align.LEFT);
            canvas.drawText(timeText.split(":")[1],
                    165 * ratio, timeY * ratio,
                    mTimeTextPaint);

            int monthY = mIsRound ? 175 : 180;
            canvas.save();
            canvas.rotate(13, 20 * ratio, monthY * ratio);
            canvas.drawText(monthText, 20 * ratio, monthY * ratio, mDayPaint);
            canvas.drawText(monthBold, 20 * ratio, monthY * ratio, mMonthPaint);
            canvas.restore();

            int AmPmY = mIsRound ? 180 : 185;
            canvas.save();
            canvas.rotate(-13, 280 * ratio, AmPmY * ratio);
            canvas.drawText(amPm, 280 * ratio, AmPmY * ratio, mAMPMPaint);
            canvas.restore();

            int periodTimeY = mIsRound ? 280 : 290;
            if (currentGame.gameTimeOrStartDate.contains(":")) {

                mPeriodTimeTextPaint.setTextSize((mIsRound ? 68 : 68) * ratio);
                mPeriodTimeTextPaintAmbient.setTextSize((mIsRound ? 68 : 68) * ratio);
                mPeriodTimeTextPaint.setTextAlign(Paint.Align.CENTER);
                mPeriodTimeTextPaintAmbient.setTextAlign(Paint.Align.CENTER);
                canvas.drawText(":", centerX, periodTimeY * ratio, mAmbient ? mPeriodTimeTextPaintAmbient : mPeriodTimeTextPaint);
                mPeriodTimeTextPaint.setTextAlign(Paint.Align.RIGHT);
                mPeriodTimeTextPaintAmbient.setTextAlign(Paint.Align.RIGHT);
                canvas.drawText(currentGame.gameTimeOrStartDate.split(":")[0], 155 * ratio, periodTimeY * ratio, mAmbient ? mPeriodTimeTextPaintAmbient : mPeriodTimeTextPaint);
                mPeriodTimeTextPaint.setTextAlign(Paint.Align.LEFT);
                mPeriodTimeTextPaintAmbient.setTextAlign(Paint.Align.LEFT);
                canvas.drawText(currentGame.gameTimeOrStartDate.split(":")[1], 165 * ratio, periodTimeY * ratio, mAmbient ? mPeriodTimeTextPaintAmbient : mPeriodTimeTextPaint);

                mPeriodSuffixTextPaint.setTextSize( (mIsRound ? 24 : 24) * ratio);
                mPeriodSuffixTextPaintAmbient.setTextSize( (mIsRound ? 24 : 24) * ratio);
                String period = currentGame.gameStatusOrStartTime.substring(0, currentGame.gameStatusOrStartTime.length() - 2);
                String periodSuffix = currentGame.gameStatusOrStartTime.substring(currentGame.gameStatusOrStartTime.length() - 2, currentGame.gameStatusOrStartTime.length());
                canvas.drawText(period, (mIsRound ? 60 : 25) * ratio, (mIsRound ? 260 : 280) * ratio, mAmbient ? mPeriodTextPaintAmbient : mPeriodTextPaint);
                canvas.drawText(periodSuffix, (mIsRound ? 75 : 42) * ratio, (mIsRound ? 242 : 262) * ratio,  mAmbient ? mPeriodSuffixTextPaintAmbient :mPeriodSuffixTextPaint);
            }
            else if (currentGame.gameStatusOrStartTime.contains("No Game")){
                mPeriodTimeTextPaint.setTextSize((mIsRound ? 42 : 52) * ratio);
                mPeriodTimeTextPaint.setTextAlign(Paint.Align.CENTER);
                mPeriodTimeTextPaintAmbient.setTextSize((mIsRound ? 42 : 52) * ratio);
                mPeriodTimeTextPaintAmbient.setTextAlign(Paint.Align.CENTER);
                canvas.drawText(currentGame.gameStatusOrStartTime, centerX, periodTimeY * ratio, mAmbient ? mPeriodTimeTextPaintAmbient : mPeriodTimeTextPaint);
            }
            else if (currentGame.gameStatusOrStartTime.contains("AM")
                    || currentGame.gameStatusOrStartTime.contains("PM")){
                mPeriodTimeTextPaint.setTextSize((mIsRound ? 42 : 52) * ratio);
                mPeriodTimeTextPaint.setTextAlign(Paint.Align.CENTER);
                mPeriodTimeTextPaintAmbient.setTextSize((mIsRound ? 42 : 52) * ratio);
                mPeriodTimeTextPaintAmbient.setTextAlign(Paint.Align.CENTER);
                canvas.drawText(currentGame.gameTimeOrStartDate, centerX, periodTimeY * ratio, mAmbient ? mPeriodTimeTextPaintAmbient : mPeriodTimeTextPaint);


                mPeriodSuffixTextPaint.setTextSize( (mIsRound ? 26 : 30) * ratio);
                mPeriodSuffixTextPaintAmbient.setTextSize( (mIsRound ? 30 : 30) * ratio);
                String time = currentGame.gameStatusOrStartTime.split(" ")[0];
                String ampm = currentGame.gameStatusOrStartTime.split(" ")[1];
                canvas.drawText(time,
                        (mIsRound ? 70 : 33) * ratio, (mIsRound ? 245 : 260) * ratio,
                        mAmbient ? mPeriodSuffixTextPaintAmbient : mPeriodSuffixTextPaint);
                canvas.drawText(ampm,
                        (mIsRound ? 70 : 33) * ratio, (mIsRound ? 270 : 285) * ratio,
                        mAmbient ? mPeriodSuffixTextPaintAmbient : mPeriodSuffixTextPaint);
            }
            else {
                mPeriodTimeTextPaint.setTextSize((mIsRound ? 42 : 52) * ratio);
                mPeriodTimeTextPaint.setTextAlign(Paint.Align.CENTER);
                mPeriodTimeTextPaintAmbient.setTextSize((mIsRound ? 42 : 52) * ratio);
                mPeriodTimeTextPaintAmbient.setTextAlign(Paint.Align.CENTER);
                canvas.drawText(currentGame.gameTimeOrStartDate, centerX, periodTimeY * ratio, mAmbient ? mPeriodTimeTextPaintAmbient : mPeriodTimeTextPaint);

                if(currentGame.gameStatusOrStartTime.length() == 2) {
                    mPeriodSuffixTextPaint.setTextSize((mIsRound ? 38 : 38) * ratio);
                    mPeriodSuffixTextPaintAmbient.setTextSize((mIsRound ? 38 : 38) * ratio);
                }
                else if(currentGame.gameStatusOrStartTime.length() == 3) {
                    mPeriodSuffixTextPaint.setTextSize((mIsRound ? 34 : 34) * ratio);
                    mPeriodSuffixTextPaintAmbient.setTextSize((mIsRound ? 34 : 34) * ratio);
                }
                else {
                    mPeriodSuffixTextPaint.setTextSize((mIsRound ? 30 : 30) * ratio);
                    mPeriodSuffixTextPaintAmbient.setTextSize((mIsRound ? 30 : 30) * ratio);
                }
                canvas.drawText(currentGame.gameStatusOrStartTime,
                        (mIsRound ? 60 : 35) * ratio, (mIsRound ? 255 : 270) * ratio,
                        mAmbient ? mPeriodSuffixTextPaintAmbient : mPeriodSuffixTextPaint);
            }

            canvas.drawText(currentGame.awayTeam.city.toUpperCase(), (mIsRound ? 30 : 10) * ratio, (mIsRound ? 90 : 105) * ratio, mAmbient ? mAwayCityTextPaintAmbient : mAwayCityTextPaint);
            canvas.drawText(currentGame.awayTeam.name.toUpperCase(), (mIsRound ? 30 : 10) * ratio, (mIsRound ? 110 : 130) * ratio,mAmbient ? mAwayTeamNameTextPaintAmbient : mAwayTeamNameTextPaint);
            canvas.drawText(currentGame.awayTeam.score.toUpperCase(), (mIsRound ? 145 : 143) * ratio, (mIsRound ? 130 : 140) * ratio, mAwayScoreTextPaint);
            canvas.drawText("AWAY", (mIsRound ? 30 : 10) * ratio, (mIsRound ? 130 : 40) * ratio, mAmbient ? mAwayTextPaintAmbient : mAwayTextPaint);

            mYOffset = 75;
            //home is on the bottom of the screen
            canvas.drawText(currentGame.homeTeam.city.toUpperCase(), (mIsRound ? 290 : 310) * ratio, (mIsRound ? 90 : 105) * ratio, mHomeCityTextPaint);
            canvas.drawText(currentGame.homeTeam.name.toUpperCase(), (mIsRound ? 290 : 310) * ratio, (mIsRound ? 110 : 130) * ratio, mHomeTeamNameTextPaint);
            canvas.drawText(currentGame.homeTeam.score.toUpperCase(), (mIsRound ? 175 : 175) * ratio, (mIsRound ? 53 : 53) * ratio, mHomeScoreTextPaint);
            canvas.drawText("HOME", (mIsRound ? 290 : 310) * ratio, (mIsRound ? 130 : 40) * ratio, mHomeTextPaint);
        }

        float mXOffset = 0;
        float mYOffset = 0;

        /**
         * Captures tap event (and tap type) and toggles the background color if the user finishes
         * a tap.
         */
        @Override
        public void onTapCommand(int tapType, int x, int y, long eventTime) {
            Resources resources = AbacusWatchFace.this.getResources();
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
            AbacusWatchFace.this.registerReceiver(mTimeZoneReceiver, filter);
        }

        private void unregisterReceiver() {
            if (!mRegisteredTimeZoneReceiver) {
                return;
            }
            mRegisteredTimeZoneReceiver = false;
            AbacusWatchFace.this.unregisterReceiver(mTimeZoneReceiver);
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
