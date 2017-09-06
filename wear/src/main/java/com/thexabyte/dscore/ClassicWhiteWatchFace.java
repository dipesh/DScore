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
import android.graphics.Matrix;
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
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Analog watch face with a ticking second hand. In ambient mode, the second hand isn't shown. On
 * devices with low-bit ambient mode, the hands are drawn without anti-aliasing in ambient mode.
 */
public class ClassicWhiteWatchFace extends CanvasWatchFaceService {
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
        private final WeakReference<ClassicWhiteWatchFace.Engine> mWeakReference;

        public EngineHandler(ClassicWhiteWatchFace.Engine reference) {
            mWeakReference = new WeakReference<>(reference);
        }

        @Override
        public void handleMessage(Message msg) {
            ClassicWhiteWatchFace.Engine engine = mWeakReference.get();
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

        int textCount = 1;
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

        Bitmap mHourHand;
        Bitmap mMinuteHand;

        Bitmap mHourHandAmbient;
        Bitmap mMinuteHandAmbient;
        Bitmap mSecondHand;

        /**
         * Whether the display supports fewer bits for each color in ambient mode. When true, we
         * disable anti-aliasing in ambient mode.
         */
        boolean mLowBitAmbient;

        GoogleApiClient mGoogleApiClient;
        private static final String COUNT_KEY = "com.example.key.count";
        private static final String COUNT_PATH = "/count";
        private static final String HOCKEY_PATH = "/hockey";

        SportsGame currentGame;

        Paint mInitialsPaint;
        Paint mInitialsAmbientPaint;
        Paint mInitialsPaintBorder;
        Paint mInitialAmbientPaintBorder;

        Paint mScorePaint;
        Paint mScoreAmbientPaint;
        Paint mScorePaintBorder;
        Paint mScoreAmbientPaintBorder;

        Paint mTimePaint;
        Paint mTimeAmbientPaint;
        Paint mTimePaintBorder;
        Paint mTimeAmbientPaintBorder;

        Paint mPeriodPaint;
        Paint mPeriodAmbientPaint;
        Paint mPeriodPaintBorder;
        Paint mPeriodAmbientPaintBorder;
//        Paint mAnalogInitialsPaintRed;
//        Paint mAnalogScorePaintRed;
//        Paint mTimePaintRed;

        private final Typeface WALKWAY_BLACK = Typeface.createFromAsset(mAssetManager,
                String.format(Locale.US, "fonts/%s", "Walkway Black.ttf"));

        private final Typeface WALKWAY_SEMI_BOLD_SCORE = Typeface.createFromAsset(mAssetManager,
                String.format(Locale.US, "fonts/%s", "Walkway SemiBold.ttf"));

        private final Typeface WALKWAY_ULTRA_BOLD = Typeface.createFromAsset(mAssetManager,
                String.format(Locale.US, "fonts/%s", "Walkway UltraBold.ttf"));


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
            text = "dataChanged" + textCount;
            // Loop through the events and send a message
            // to the node that created the data item.
            for (DataEvent event : dataEvents) {
                String uri = event.getDataItem().getUri().toString();

                if (uri.contains(COUNT_PATH)) {
                    DataMapItem dataItem = DataMapItem.fromDataItem(event.getDataItem());
                    int data = dataItem.getDataMap().getInt(COUNT_KEY);
                    text = "count=" + data;

                } else if (uri.contains(HOCKEY_PATH)) {
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

            setWatchFaceStyle(new WatchFaceStyle.Builder(ClassicWhiteWatchFace.this)
                    .setCardPeekMode(WatchFaceStyle.PEEK_MODE_SHORT)
                    .setBackgroundVisibility(WatchFaceStyle.BACKGROUND_VISIBILITY_INTERRUPTIVE)
                    .setShowSystemUiTime(false)
                    .setAcceptsTapEvents(true)
                    .build());

            currentGame = new SportsGame(BuildConfig.DEBUG); //test data

            mTime = new Time();
            mGoogleApiClient = new GoogleApiClient.Builder(ClassicWhiteWatchFace.this)
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
            //debugText += ratio;

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
            //debugText += "surface";
//            if (mBackgroundRoundScaledBitmap == null
//                    || mBackgroundRoundScaledBitmap.getWidth() != width
//                    || mBackgroundRoundScaledBitmap.getHeight() != height) {
//                mBackgroundRoundScaledBitmap = Bitmap.createScaledBitmap(mBackgroundRoundBitmap, width, height, true);
//                mBackgroundSquareScaledBitmap = Bitmap.createScaledBitmap(mBackgroundSquareBitmap, width, height, true);
//            }
            super.onSurfaceChanged(holder, format, width, height);
        }

        boolean mIsRound;

        @Override
        public void onApplyWindowInsets(WindowInsets insets) {
            super.onApplyWindowInsets(insets);
            //debugText += "insets";
            // Load resources that have alternate values for round watches.
            Resources resources = ClassicWhiteWatchFace.this.getResources();
            mIsRound = insets.isRound();

            if (mIsRound) {
                Drawable backgroundDrawable = resources.getDrawable(R.drawable.classique_white1_circle_640x640, null);
                mBackgroundBitmap = ((BitmapDrawable) backgroundDrawable).getBitmap();

                Drawable backgroundAmbientDrawable = resources.getDrawable(R.drawable.classique_white_ambient_mode_circle_640x640, null);
                mAmbientBackgroundBitmap = ((BitmapDrawable) backgroundAmbientDrawable).getBitmap();
            } else {
                Drawable backgroundDrawable = resources.getDrawable(R.drawable.classique_white1_square_640x640, null);
                mBackgroundBitmap = ((BitmapDrawable) backgroundDrawable).getBitmap();

                Drawable backgroundAmbientDrawable = resources.getDrawable(R.drawable.classique_white_ambient_mode_square_640x640, null);
                mAmbientBackgroundBitmap = ((BitmapDrawable) backgroundAmbientDrawable).getBitmap();
            }

            mBackgroundBitmap = Bitmap.createScaledBitmap(mBackgroundBitmap, mWidth, mHeight, true);
            mAmbientBackgroundBitmap = Bitmap.createScaledBitmap(mAmbientBackgroundBitmap, mWidth, mHeight, true);


            mHourHand = ((BitmapDrawable) resources.getDrawable(R.drawable.classique_white_hand_hour_640x640, null)).getBitmap();
            mHourHand = Bitmap.createScaledBitmap(mHourHand, mWidth, mHeight, true);
            mMinuteHand = ((BitmapDrawable) resources.getDrawable(R.drawable.classique_white_hand_minute_640x640, null)).getBitmap();
            mMinuteHand = Bitmap.createScaledBitmap(mMinuteHand, mWidth, mHeight, true);
            mSecondHand = ((BitmapDrawable) resources.getDrawable(R.drawable.classique_white_hand_second_640x640, null)).getBitmap();
            mSecondHand = Bitmap.createScaledBitmap(mSecondHand, mWidth, mHeight, true);

            mHourHandAmbient = ((BitmapDrawable) resources.getDrawable(R.drawable.classique_white_hand_hour_ambient_mode_640x640, null)).getBitmap();
            mHourHandAmbient = Bitmap.createScaledBitmap(mHourHandAmbient, mWidth, mHeight, true);
            mMinuteHandAmbient = ((BitmapDrawable) resources.getDrawable(R.drawable.classique_white_hand_minute_ambient_mode_640x640, null)).getBitmap();
            mMinuteHandAmbient = Bitmap.createScaledBitmap(mMinuteHandAmbient, mWidth, mHeight, true);

            mInitialsPaint = createPaint(resources.getColor(R.color.white), 16, Paint.Align.CENTER, WALKWAY_BLACK);
            mInitialsPaintBorder = createPaint(resources.getColor(R.color.black), 16, Paint.Align.CENTER, WALKWAY_BLACK);
            mInitialsPaintBorder.setStyle(Paint.Style.FILL_AND_STROKE);
            mInitialsPaintBorder.setStrokeWidth(2);
            mInitialsAmbientPaint = createPaint(resources.getColor(R.color.white), 16, Paint.Align.CENTER, WALKWAY_BLACK);;
            mInitialAmbientPaintBorder = createPaint(resources.getColor(R.color.classiqueWhiteAmbientGrey), 16, Paint.Align.CENTER, WALKWAY_BLACK);
            mInitialAmbientPaintBorder.setStyle(Paint.Style.FILL_AND_STROKE);
            mInitialAmbientPaintBorder.setStrokeWidth(2);

            mScorePaint = createPaint(resources.getColor(R.color.black), 40, Paint.Align.CENTER, WALKWAY_SEMI_BOLD_SCORE);
            mScorePaintBorder = createPaint(resources.getColor(R.color.white), 40, Paint.Align.CENTER, WALKWAY_SEMI_BOLD_SCORE);
            mScorePaintBorder.setStyle(Paint.Style.FILL_AND_STROKE);
            mScorePaintBorder.setStrokeWidth(2);
            mScoreAmbientPaint = createPaint(resources.getColor(R.color.white), 40, Paint.Align.CENTER, WALKWAY_SEMI_BOLD_SCORE);
            mScoreAmbientPaintBorder = createPaint(resources.getColor(R.color.classiqueWhiteAmbientGrey), 40, Paint.Align.CENTER, WALKWAY_SEMI_BOLD_SCORE);
            mScoreAmbientPaintBorder.setStyle(Paint.Style.FILL_AND_STROKE);
            mScoreAmbientPaintBorder.setStrokeWidth(2);

            mTimePaint = createPaint(resources.getColor(R.color.black), 18, Paint.Align.CENTER, WALKWAY_ULTRA_BOLD);
            mTimePaintBorder = createPaint(resources.getColor(R.color.classiqueWhiteGrey), 18, Paint.Align.CENTER, WALKWAY_ULTRA_BOLD);
            mTimePaintBorder.setStyle(Paint.Style.FILL_AND_STROKE);
            mTimePaintBorder.setStrokeWidth(2);
            mTimeAmbientPaint = createPaint(resources.getColor(R.color.white), 18, Paint.Align.CENTER, WALKWAY_ULTRA_BOLD);
            mTimeAmbientPaintBorder = createPaint(resources.getColor(R.color.classiqueWhiteAmbientGrey), 18, Paint.Align.CENTER, WALKWAY_ULTRA_BOLD);
            mTimeAmbientPaintBorder.setStyle(Paint.Style.FILL_AND_STROKE);
            mTimeAmbientPaintBorder.setStrokeWidth(2);

            mPeriodPaint = createPaint(resources.getColor(R.color.black), 18, Paint.Align.CENTER, WALKWAY_ULTRA_BOLD);
            mPeriodPaintBorder = createPaint(resources.getColor(R.color.classiqueWhiteGrey), 18, Paint.Align.CENTER, WALKWAY_ULTRA_BOLD);
            mPeriodPaintBorder.setStyle(Paint.Style.FILL_AND_STROKE);
            mPeriodPaintBorder.setStrokeWidth(2);
            mPeriodAmbientPaint = createPaint(resources.getColor(R.color.white), 18, Paint.Align.CENTER, WALKWAY_ULTRA_BOLD);
            mPeriodAmbientPaintBorder = createPaint(resources.getColor(R.color.classiqueWhiteAmbientGrey), 18, Paint.Align.CENTER, WALKWAY_ULTRA_BOLD);
            mPeriodAmbientPaintBorder.setStyle(Paint.Style.FILL_AND_STROKE);
            mPeriodAmbientPaintBorder.setStrokeWidth(2);
        }


        @Override
        public void onDraw(Canvas canvas, Rect bounds) {
            mTime.setToNow();
            // Draw the background.
            if (isInAmbientMode()) {
                //canvas.drawColor(Color.BLACK);
                canvas.drawBitmap(mAmbientBackgroundBitmap, 0, 0, null);
            } else {
                canvas.drawBitmap(mBackgroundBitmap, 0, 0, null);
            }

            String amPm = mTime.hour > 12 ? "PM" : "AM";
            int hour = mTime.hour > 12 ? mTime.hour - 12 : mTime.hour;

            float ratio = 1.0f / 320f * bounds.width();

            // Find the center. Ignore the window insets so that, on round watches with a
            // "chin", the watch face is centered on the entire screen, not just the usable
            // portion.
            float centerX = bounds.width() / 2f;
            float centerY = bounds.height() / 2f;


            if (!isInAmbientMode()) {
                Matrix hourHandRotator = new Matrix();
                float hourAngle = ((float) hour / 12 * 360f) + ((float) mTime.minute / 60 * 360 / 12);
                hourHandRotator.postRotate(hourAngle, centerX, centerY);
                canvas.drawBitmap(mHourHand, hourHandRotator, null);

                Matrix minuteHandRotator = new Matrix();
                float minuteAngle = ((float) mTime.minute / 60 * 360f) + ((float) mTime.second / 60 * 360 / 60);
                minuteHandRotator.postRotate(minuteAngle, centerX, centerY);
                canvas.drawBitmap(mMinuteHand, minuteHandRotator, null);


                Matrix secondHandRotator = new Matrix();
                float secondAngle = (float) mTime.second / 60 * 360f;
                secondHandRotator.postRotate(secondAngle, centerX, centerY);
                canvas.drawBitmap(mSecondHand, secondHandRotator, null);

            } else {
                Matrix hourHandRotator = new Matrix();
                float hourAngle = ((float) hour / 12 * 360f) + ((float) mTime.minute / 60 * 360 / 12);
                hourHandRotator.postRotate(hourAngle, centerX, centerY);
                canvas.drawBitmap(mHourHandAmbient, hourHandRotator, null);

                Matrix minuteHandRotator = new Matrix();
                float minuteAngle = ((float) mTime.minute / 60 * 360f) + ((float) mTime.second / 60 * 360 / 60);
                minuteHandRotator.postRotate(minuteAngle, centerX, centerY);
                canvas.drawBitmap(mMinuteHandAmbient, minuteHandRotator, null);

            }

            //keep the text above the hands
            drawTextWithBorder(canvas, currentGame.homeTeam.initials, 93, 139, ratio,
                    mAmbient ? mInitialsAmbientPaint :  mInitialsPaint,
                    mAmbient ? null :  mInitialsPaintBorder, null);
            drawTextWithBorder(canvas, currentGame.homeTeam.score, 92, 177, ratio,
                    mAmbient ? mScoreAmbientPaint :  mScorePaint,
                    mAmbient ? null : mScorePaintBorder, null);
            drawTextWithBorder(canvas, currentGame.awayTeam.initials, 227, 139, ratio,
                    mAmbient ? mInitialsAmbientPaint :  mInitialsPaint,
                    mAmbient ? null :  mInitialsPaintBorder, null);
            drawTextWithBorder(canvas, currentGame.awayTeam.score, 226, 177, ratio,
                    mAmbient ? mScoreAmbientPaint :  mScorePaint,
                    mAmbient ? null : mScorePaintBorder, null);
            drawTextWithBorder(canvas, currentGame.gameTimeOrStartDate, 160, 215, ratio,
                    mAmbient ? mTimeAmbientPaint :  mTimePaint,
                    mAmbient ? null :mTimePaintBorder, null);
            drawTextWithBorder(canvas, currentGame.gameStatusOrStartTime, 160, 232, ratio,
                    mAmbient ? mPeriodAmbientPaint : mPeriodPaint,
                    mAmbient ? null : mPeriodPaintBorder, null);
            //canvas.drawText(currentGame.homeTeam.score, 92 * ratio, 177 * ratio, mScoreAmbientPaintBorder); //border goes behind text

            if (BuildConfig.DEBUG) {
            }
        }

        public void drawTextWithBorder(Canvas canvas, String text, float x, float y, float ratio, Paint paint, Paint paintBorder, Paint paintNotConnected) {
            if(paintBorder != null) {
                canvas.drawText(text, x * ratio, y * ratio, paintBorder); //border goes behind text
            }
//            if(currentGame.awayTeam.score.equals("000")){
//                canvas.drawText(text, x * ratio, y * ratio, paintNotConnected);
//            }
//            else {
            canvas.drawText(text, x * ratio, y * ratio, paint);
//            }
        }

        float mXOffset = 77;
        float mYOffset = 150;

        /**
         * Captures tap event (and tap type) and toggles the background color if the user finishes
         * a tap.
         */
        @Override
        public void onTapCommand(int tapType, int x, int y, long eventTime) {
            Resources resources = ClassicWhiteWatchFace.this.getResources();
            switch (tapType) {
                case TAP_TYPE_TOUCH:
//                    if (currentGame.gameTimeOrStartDate == "PRE_GAME") {
//                        currentGame.gameTimeOrStartDate = "11:45";
//                        currentGame.gameStatusOrStartTime = "3rd";
//                    } else {
//                        currentGame.gameTimeOrStartDate = "PRE_GAME";
//                        currentGame.gameStatusOrStartTime = "7:00 AM";
//                    }
                    break;
                case TAP_TYPE_TOUCH_CANCEL:
                    // The user has started a different gesture or otherwise cancelled the tap.
                    break;
                case TAP_TYPE_TAP:
                    // The user has completed the tap gesture.
                    mTapCount++;
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
            ClassicWhiteWatchFace.this.registerReceiver(mTimeZoneReceiver, filter);
        }

        private void unregisterReceiver() {
            if (!mRegisteredTimeZoneReceiver) {
                return;
            }
            mRegisteredTimeZoneReceiver = false;
            ClassicWhiteWatchFace.this.unregisterReceiver(mTimeZoneReceiver);
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
