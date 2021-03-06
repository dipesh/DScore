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
import android.graphics.Color;
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
import com.thexabyte.mylibrary.SportsTeam;

import java.lang.ref.WeakReference;
import java.text.DateFormatSymbols;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
/**
 * Analog watch face with a ticking second hand. In ambient mode, the second hand isn't shown. On
 * devices with low-bit ambient mode, the hands are drawn without anti-aliasing in ambient mode.
 */
public class VanierWatchFace extends CanvasWatchFaceService {
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
        private final WeakReference<VanierWatchFace.Engine> mWeakReference;

        public EngineHandler(VanierWatchFace.Engine reference) {
            mWeakReference = new WeakReference<>(reference);
        }

        @Override
        public void handleMessage(Message msg) {
            VanierWatchFace.Engine engine = mWeakReference.get();
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

        // graphic objects
//        Bitmap mBackgroundRoundBitmap;
//        Bitmap mBackgroundSquareBitmap;
//        Bitmap mBackgroundRoundScaledBitmap;
//        Bitmap mBackgroundSquareScaledBitmap;
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

        String messageText = "";

        Paint mTextPaint;

        Paint mAnalogInitialsPaint;
        Paint mAnalogInitialsPaintBorder;

        Paint mAnalogScorePaint;
        Paint mAnalogScorePaintBorder;

        Paint mAnalogInitialsPaintRed;
        Paint mAnalogScorePaintRed;

        Paint mTextStrokePaint;

        Paint mTimeTextPaint;
        Paint mMonthPaint;
        Paint mAMPMPaint;

        Paint mScoreTextPaint;
        Paint mPeriodTextPaint;
        Paint mHockeyTimeTextPaint;
        Paint mCityTextPaint;
        Paint mTeamNameTextPaint;

        Paint mHandPaint;

        private final Typeface EUROSTILE = Typeface.createFromAsset(mAssetManager,
                String.format(Locale.US, "fonts/%s", "Eurostile.ttf"));

        private final Typeface WALKWAY_BLACK_TEAM_NAME = Typeface.createFromAsset(mAssetManager,
                String.format(Locale.US, "fonts/%s", "Walkway Black.ttf"));

        private final Typeface WALKWAY_SEMI_BOLD_SCORE = Typeface.createFromAsset(mAssetManager,
                String.format(Locale.US, "fonts/%s", "Walkway SemiBold.ttf"));

        private final Typeface EUROSTILE_BOLD = Typeface.create(EUROSTILE, Typeface.BOLD);

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

            setWatchFaceStyle(new WatchFaceStyle.Builder(VanierWatchFace.this)
                    .setCardPeekMode(WatchFaceStyle.PEEK_MODE_SHORT)
                    .setBackgroundVisibility(WatchFaceStyle.BACKGROUND_VISIBILITY_INTERRUPTIVE)
                    .setShowSystemUiTime(false)
                    .setAcceptsTapEvents(true)
                    .build());

            currentGame = new SportsGame(BuildConfig.DEBUG); //test data

            mTime = new Time();
            mGoogleApiClient = new GoogleApiClient.Builder(VanierWatchFace.this)
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
                // if (mLowBitAmbient) {
                //mHandPaint.setAntiAlias(!inAmbientMode);

                //todo add the paints
                mTimeTextPaint.setAntiAlias(!mAmbient);
                mTextPaint.setAntiAlias(!mAmbient);

                mTimeTextPaint.setAntiAlias(!mAmbient);
                mMonthPaint.setAntiAlias(!mAmbient);
                mAMPMPaint.setAntiAlias(!mAmbient);

                mScoreTextPaint.setAntiAlias(!mAmbient);
                mPeriodTextPaint.setAntiAlias(!mAmbient);
                mHockeyTimeTextPaint.setAntiAlias(!mAmbient);
                mCityTextPaint.setAntiAlias(!mAmbient);
                mTeamNameTextPaint.setAntiAlias(!mAmbient);
                // }
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
            Resources resources = VanierWatchFace.this.getResources();
            mIsRound = insets.isRound();


//Dscore digital
//            if(mIsRound) {
//                Drawable backgroundDrawable = resources.getDrawable(R.drawable.bg_circle, null);
//                mBackgroundBitmap = ((BitmapDrawable) backgroundDrawable).getBitmap();
//
//                Drawable backgroundAmbientDrawable = resources.getDrawable(R.drawable.bg_circle_ambient, null);
//                mAmbientBackgroundBitmap = ((BitmapDrawable) backgroundAmbientDrawable).getBitmap();
//            }
//            else{
//                Drawable backgroundDrawable = resources.getDrawable(R.drawable.bg_square, null);
//                mBackgroundBitmap = ((BitmapDrawable) backgroundDrawable).getBitmap();
//
//                Drawable backgroundAmbientDrawable = resources.getDrawable(R.drawable.bg_square_ambient, null);
//                mAmbientBackgroundBitmap = ((BitmapDrawable) backgroundAmbientDrawable).getBitmap();
//            }

//            Drawable backgroundDrawable = resources.getDrawable(R.drawable.analog_bg, null);
//            mBackgroundBitmap = ((BitmapDrawable) backgroundDrawable).getBitmap();
//
//            Drawable backgroundAmbientDrawable = resources.getDrawable(R.drawable.analog_bg_ambient, null);
//            mAmbientBackgroundBitmap = ((BitmapDrawable) backgroundAmbientDrawable).getBitmap();
            if (mIsRound) {
                Drawable backgroundDrawable = resources.getDrawable(R.drawable.analog_bg, null);
                mBackgroundBitmap = ((BitmapDrawable) backgroundDrawable).getBitmap();

                Drawable backgroundAmbientDrawable = resources.getDrawable(R.drawable.analog_bg_ambient, null);
                mAmbientBackgroundBitmap = ((BitmapDrawable) backgroundAmbientDrawable).getBitmap();
            } else {
                Drawable backgroundDrawable = resources.getDrawable(R.drawable.vanier_square, null);
                mBackgroundBitmap = ((BitmapDrawable) backgroundDrawable).getBitmap();

                Drawable backgroundAmbientDrawable = resources.getDrawable(R.drawable.vanier_ambient_mode_square, null);
                mAmbientBackgroundBitmap = ((BitmapDrawable) backgroundAmbientDrawable).getBitmap();
            }

            mHourHand = ((BitmapDrawable) resources.getDrawable(R.drawable.analog_hand_hour, null)).getBitmap();
            mHourHand = Bitmap.createScaledBitmap(mHourHand, mWidth, mHeight, true);
            mMinuteHand = ((BitmapDrawable) resources.getDrawable(R.drawable.analog_hand_minute, null)).getBitmap();
            mMinuteHand = Bitmap.createScaledBitmap(mMinuteHand, mWidth, mHeight, true);
            mSecondHand = ((BitmapDrawable) resources.getDrawable(R.drawable.analog_hand_second, null)).getBitmap();
            mSecondHand = Bitmap.createScaledBitmap(mSecondHand, mWidth, mHeight, true);

            mHourHandAmbient = ((BitmapDrawable) resources.getDrawable(R.drawable.analog_hand_hour_ambient, null)).getBitmap();
            mHourHandAmbient = Bitmap.createScaledBitmap(mHourHandAmbient, mWidth, mHeight, true);
            mMinuteHandAmbient = ((BitmapDrawable) resources.getDrawable(R.drawable.analog_hand_minute_ambient, null)).getBitmap();
            mMinuteHandAmbient = Bitmap.createScaledBitmap(mMinuteHandAmbient, mWidth, mHeight, true);

            //keep this part
            mBackgroundBitmap = Bitmap.createScaledBitmap(mBackgroundBitmap, mWidth, mHeight, true);
            mAmbientBackgroundBitmap = Bitmap.createScaledBitmap(mAmbientBackgroundBitmap, mWidth, mHeight, true);

            mTextPaint = createPaint(Color.WHITE, 16, Paint.Align.LEFT, Typeface.DEFAULT);

            mAnalogInitialsPaint = createPaint(resources.getColor(R.color.white), 22, Paint.Align.CENTER, WALKWAY_BLACK_TEAM_NAME);
            mAnalogInitialsPaintBorder = createPaint(resources.getColor(R.color.black), 22, Paint.Align.CENTER, WALKWAY_BLACK_TEAM_NAME);
            mAnalogInitialsPaintBorder.setStyle(Paint.Style.FILL_AND_STROKE);
            mAnalogInitialsPaintBorder.setStrokeWidth(2);

            mAnalogScorePaint = createPaint(resources.getColor(R.color.white), 26, Paint.Align.CENTER, WALKWAY_SEMI_BOLD_SCORE);
            mAnalogScorePaintBorder = createPaint(resources.getColor(R.color.black), 26, Paint.Align.CENTER, WALKWAY_SEMI_BOLD_SCORE);
            mAnalogScorePaintBorder.setStyle(Paint.Style.FILL_AND_STROKE);
            mAnalogScorePaintBorder.setStrokeWidth(2);

            mTextStrokePaint = createPaint(Color.DKGRAY, 16, Paint.Align.LEFT, Typeface.DEFAULT);
            mTextStrokePaint.setStyle(Paint.Style.FILL_AND_STROKE);
            mTextStrokePaint.setStrokeWidth(4);

            mTimeTextPaint = createPaint(resources.getColor(R.color.white), mIsRound ? 36 : 52, Paint.Align.CENTER, EUROSTILE);
            mMonthPaint = createPaint(resources.getColor(R.color.white), mIsRound ? 24 : 24, Paint.Align.CENTER, EUROSTILE_BOLD);
            mAMPMPaint = createPaint(resources.getColor(R.color.white), mIsRound ? 20 : 24, Paint.Align.CENTER, EUROSTILE_BOLD);

            mScoreTextPaint = createPaint(resources.getColor(R.color.dscore_blue), mIsRound ? 34 : 42, Paint.Align.CENTER, EUROSTILE);
            mPeriodTextPaint = createPaint(resources.getColor(R.color.white), mIsRound ? 36 : 42, Paint.Align.RIGHT, EUROSTILE);
            mHockeyTimeTextPaint = createPaint(resources.getColor(R.color.white), mIsRound ? 24 : 24, Paint.Align.RIGHT, EUROSTILE_BOLD);

            mCityTextPaint = createPaint(resources.getColor(R.color.white), mIsRound ? 16 : 22, Paint.Align.CENTER, EUROSTILE);
            mTeamNameTextPaint = createPaint(resources.getColor(R.color.dscore_blue), mIsRound ? 28 : 32, Paint.Align.CENTER, EUROSTILE_BOLD);

            mAnalogInitialsPaintRed = createPaint(resources.getColor(R.color.red), 22, Paint.Align.CENTER, WALKWAY_BLACK_TEAM_NAME);
            mAnalogScorePaintRed = createPaint(resources.getColor(R.color.red), 26, Paint.Align.CENTER, WALKWAY_SEMI_BOLD_SCORE);

            mHandPaint = new Paint();
            mHandPaint.setColor(resources.getColor(R.color.analog_hands));
            mHandPaint.setStrokeWidth(resources.getDimension(R.dimen.analog_hand_stroke));
            mHandPaint.setAntiAlias(true);
            mHandPaint.setStrokeCap(Paint.Cap.ROUND);

        }


        @Override
        public void onDraw(Canvas canvas, Rect bounds) {
            mTime.setToNow();

//            if (!debugText.contains("draw")) {
//                debugText += "draw";
//            }

            // Draw the background.
            if (isInAmbientMode()) {
                //canvas.drawColor(Color.BLACK);
                canvas.drawBitmap(mAmbientBackgroundBitmap, 0, 0, null);
            } else {
                canvas.drawBitmap(mBackgroundBitmap, 0, 0, null);
            }
            // canvas.drawBitmap(mBackgroundScaledBitmap, 0, 0, null);
//            canvas.drawColor(Color.WHITE);

            String amPm = mTime.hour > 12 ? "PM" : "AM";
            int hour = mTime.hour > 12 ? mTime.hour - 12 : mTime.hour;

            float ratio = 1.0f / 320f * bounds.width();

            float ampmOffset = 0;
            if (hour >= 10) {
                ampmOffset = 15 * ratio;
            }

            String timeText = String.format("%d:%02d", hour, mTime.minute);
//                    mAmbient
//                    ? String.format("%d:%02d", hour, mTime.minute)
//                    : String.format("%d:%02d:%02d", hour, mTime.minute, mTime.second);


            String month = new DateFormatSymbols().getMonths()[mTime.month];
            month = month.substring(0, 3);
            String monthText = month.toUpperCase() + " " + mTime.monthDay;

            // Find the center. Ignore the window insets so that, on round watches with a
            // "chin", the watch face is centered on the entire screen, not just the usable
            // portion.
            float centerX = bounds.width() / 2f;
            float centerY = bounds.height() / 2f;


            if (!isInAmbientMode()) {
                Matrix minuteHandRotator = new Matrix();
                float minuteAngle = ((float) mTime.minute / 60 * 360f) + ((float) mTime.second / 60 * 360 / 60);
                minuteHandRotator.postRotate(minuteAngle, centerX, centerY);
                canvas.drawBitmap(mMinuteHand, minuteHandRotator, null);

                Matrix hourHandRotator = new Matrix();
                float hourAngle = ((float) hour / 12 * 360f) + ((float) mTime.minute / 60 * 360 / 12);
                hourHandRotator.postRotate(hourAngle, centerX, centerY);
                canvas.drawBitmap(mHourHand, hourHandRotator, null);

                Matrix secondHandRotator = new Matrix();
                float secondAngle = (float) mTime.second / 60 * 360f;
                secondHandRotator.postRotate(secondAngle, centerX, centerY);
                canvas.drawBitmap(mSecondHand, secondHandRotator, null);

            } else {
                Matrix minuteHandRotator = new Matrix();
                float minuteAngle = ((float) mTime.minute / 60 * 360f) + ((float) mTime.second / 60 * 360 / 60);
                minuteHandRotator.postRotate(minuteAngle, centerX, centerY);
                canvas.drawBitmap(mMinuteHandAmbient, minuteHandRotator, null);

                Matrix hourHandRotator = new Matrix();
                float hourAngle = ((float) hour / 12 * 360f) + ((float) mTime.minute / 60 * 360 / 12);
                hourHandRotator.postRotate(hourAngle, centerX, centerY);
                canvas.drawBitmap(mHourHandAmbient, hourHandRotator, null);

            }

            //keep the text above the hands
            drawTextWithBorder(canvas, currentGame.homeTeam.initials, 102, 135, ratio, mAnalogInitialsPaint, mAnalogInitialsPaintBorder );
            drawTextWithBorder(canvas, currentGame.homeTeam.score, 102, 155, ratio, mAnalogScorePaint, mAnalogScorePaintBorder);
            drawTextWithBorder(canvas, currentGame.awayTeam.initials, 219, 135, ratio, mAnalogInitialsPaint, mAnalogInitialsPaintBorder  );
            drawTextWithBorder(canvas, currentGame.awayTeam.score, 219, 155, ratio, mAnalogScorePaint, mAnalogScorePaintBorder);
            drawTextWithBorder(canvas, currentGame.gameTimeOrStartDate, 160, 210, ratio, mAnalogInitialsPaint, mAnalogInitialsPaintBorder  );
            drawTextWithBorder(canvas, currentGame.gameStatusOrStartTime, 160, 230, ratio, mAnalogInitialsPaint, mAnalogInitialsPaintBorder  );
            //float minX = (float) Math.sin(minRot) * minLength;
            //float minY = (float) -Math.cos(minRot) * minLength;
            // canvas.drawLine(centerX, centerY, centerX + minX, centerY + minY, mHandPaint);

            //float hrX = (float) Math.sin(hrRot) * hrLength;
            //float hrY = (float) -Math.cos(hrRot) * hrLength;
            //canvas.drawLine(centerX, centerY, centerX + hrX, centerY + hrY, mHandPaint);


            if (BuildConfig.DEBUG) {
                //canvas.drawText("0", 50 * ratio, 250 * ratio, mTextStrokePaint);
            }
            //canvas.drawBitmap(mSecondHand, 0, 0, null);
//for DScore digital
//            if (mIsRound) {
//
//                canvas.drawText(timeText, 77 * ratio, 185 * ratio, mTimeTextPaint);
//                canvas.drawText(monthText, 77 * ratio, 155 * ratio, mMonthPaint);
//                canvas.drawText(amPm, 135 * ratio + ampmOffset, 185 * ratio, mAMPMPaint);
//
//                canvas.drawText(gameTime, 300 * ratio, 160 * ratio, mPeriodTimeTextPaint);
//                canvas.drawText(gamePeriod, 290 * ratio, 185 * ratio, mPeriodTextPaint);
//
//                //home is on the bottom of the screen
//                canvas.drawText(awayTeamCity.toUpperCase(), 120 * ratio, 80 * ratio, mCityTextPaint);
//                canvas.drawText(homeTeamCity.toUpperCase(), 185 * ratio, 235 * ratio, mCityTextPaint);
//
//                canvas.drawText(awayTeamName.toUpperCase(), 120 * ratio, 105 * ratio, mTeamNameTextPaint);
//                canvas.drawText(homeTeamName.toUpperCase(), 185 * ratio, 260 * ratio, mTeamNameTextPaint);
//
//                canvas.drawText(homeTeamScore.toUpperCase(), 62 * ratio, 240 * ratio, mScoreTextPaint);
//                canvas.drawText(awayTeamScore.toUpperCase(), 262 * ratio, 107 * ratio, mScoreTextPaint);
//
//                if (BuildConfig.DEBUG) {
//                    canvas.drawText(debugText, 25 * ratio, 200 * ratio, mTextStrokePaint);
//                    canvas.drawText(debugText, 25 * ratio, 200 * ratio, mTextPaint);
//                }
//            } else {
//                canvas.drawText(timeText, 77 * ratio, 195 * ratio, mTimeTextPaint);
//                canvas.drawText(monthText, 77 * ratio, 150 * ratio, mMonthPaint);
//                canvas.drawText(amPm, 150 * ratio + ampmOffset, 195 * ratio, mAMPMPaint);
//
//                canvas.drawText(gameTime, 310 * ratio, 160 * ratio, mPeriodTimeTextPaint);
//                canvas.drawText(gamePeriod, 300 * ratio, 190 * ratio, mPeriodTextPaint);
//
//                //away is on the top of the screen
//                canvas.drawText(awayTeamCity.toUpperCase(), 110 * ratio, 55 * ratio, mCityTextPaint);
//                canvas.drawText(awayTeamName.toUpperCase(), 110 * ratio, 85 * ratio, mTeamNameTextPaint);
//                canvas.drawText(awayTeamScore.toUpperCase(), 280 * ratio, 80 * ratio, mScoreTextPaint);
//
//                //home is on the bottom of the screen
//                canvas.drawText(homeTeamCity.toUpperCase(), 200 * ratio, 250 * ratio, mCityTextPaint);
//                canvas.drawText(homeTeamName.toUpperCase(), 200 * ratio, 280 * ratio, mTeamNameTextPaint);
//                canvas.drawText(homeTeamScore.toUpperCase(), 40 * ratio, 270 * ratio, mScoreTextPaint);
//
//                if (BuildConfig.DEBUG) {
//                    canvas.drawText(debugText, 25 * ratio, 215 * ratio, mTextStrokePaint);
//                    canvas.drawText(debugText, 25 * ratio, 215 * ratio, mTextPaint);
//                }
//            }


        }

        public void drawTextWithBorder(Canvas canvas, String text, float x, float y, float ratio, Paint paint, Paint paintBorder) {
            canvas.drawText(text, x * ratio, y * ratio, paintBorder); //border goes behind text

                canvas.drawText(text, x * ratio, y * ratio, paint);

        }

        float mXOffset = 77;
        float mYOffset = 150;

        /**
         * Captures tap event (and tap type) and toggles the background color if the user finishes
         * a tap.
         */
        @Override
        public void onTapCommand(int tapType, int x, int y, long eventTime) {
            Resources resources = VanierWatchFace.this.getResources();
            switch (tapType) {
                case TAP_TYPE_TOUCH:
                    mXOffset += 5;
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
            VanierWatchFace.this.registerReceiver(mTimeZoneReceiver, filter);
        }

        private void unregisterReceiver() {
            if (!mRegisteredTimeZoneReceiver) {
                return;
            }
            mRegisteredTimeZoneReceiver = false;
            VanierWatchFace.this.unregisterReceiver(mTimeZoneReceiver);
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
