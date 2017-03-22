package com.android.example.kittenwallpaper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.WindowManager;

/**
 * Created by AREG on 17.03.2017.
 */

public class LiveWallpaperService extends WallpaperService {

    private final String TAG = "LiveWallpaperService";

    private int mWallpaper;

    @Override
    public WallpaperService.Engine onCreateEngine() {
        // get current wallpaper
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(
                SetWallpaperActivity.SHARED_PREFERENCES_MAIN_KEY, Context.MODE_PRIVATE);
        mWallpaper = sharedPreferences.getInt(SetWallpaperActivity.SHARED_PREFERENCES_POSITION_KEY, 0);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        sharedPreferences.edit().putString(getString(R.string.
                picture_preference_key), String.valueOf(mWallpaper)).commit();

        Log.d(TAG, "onCreateEngine() called, current wallpaper: " + mWallpaper);

        return new KittenEngine(mWallpaper);
    }

    private class KittenEngine extends WallpaperService.Engine implements SharedPreferences.OnSharedPreferenceChangeListener {

        private final String TAG = "KittenEngine";

        private LiveWallpaperPainting mLiveWallpaperPainting;
        private SharedPreferences mSharedPreferences;
        private int mWallpaper;

        public KittenEngine(int wallpaper) {
            mWallpaper = wallpaper;
            SurfaceHolder holder = getSurfaceHolder();
            mLiveWallpaperPainting = new LiveWallpaperPainting(holder, getApplicationContext(), mWallpaper);
        }

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            mSharedPreferences.registerOnSharedPreferenceChangeListener(this);
            mLiveWallpaperPainting.setDelay((int)mSharedPreferences.getFloat(getString(R.string.
                    seek_bar_preference_key), 50f));
            mLiveWallpaperPainting.setMovingObject(mSharedPreferences.getString(getString(R.string.
                    list_preference_key), "2"));
            mLiveWallpaperPainting.setBackgroundImageView(mSharedPreferences.getString(getString(R.string.
                    picture_preference_key), "0"));
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            mSharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
            mLiveWallpaperPainting.stopPainting();
        }

        @Override
        public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {
            Log.d(TAG, "onOffsetsChanged() called");
            mLiveWallpaperPainting.createPainting();
            if (mLiveWallpaperPainting.getState() == Thread.State.NEW) {
                mLiveWallpaperPainting.start();
            }
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            Log.d(TAG, "onVisibilityChanged() called, visible: " + visible);
            if (visible) {
                mLiveWallpaperPainting.resumePainting();
            } else {
                // remove listeners and callbacks here
                mLiveWallpaperPainting.pausePainting();
            }
        }

        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
            mLiveWallpaperPainting.setSurfaceHolder(holder);
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            mLiveWallpaperPainting.setSurfaceHolder(null);
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
            Log.d(TAG, "onSurfaceChanged() called, width: " + width + " height: " + height);
            mLiveWallpaperPainting.setSurfaceSize(width, height);
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (key.equals(getResources().getString(R.string.list_preference_key))) {
                mLiveWallpaperPainting.setMovingObject(sharedPreferences.getString(key, "2"));
                Log.d("SettingsFragment", key + " " + sharedPreferences.getString(key, "2"));
            } else if (key.equals(getResources().getString(R.string.seek_bar_preference_key))) {
                mLiveWallpaperPainting.setDelay((int)sharedPreferences.getFloat(key, 50f));
                Log.d("SettingsFragment", key + " " + String.valueOf(sharedPreferences.getFloat(key, 50f)));
            } else if (key.equals(getResources().getString(R.string.picture_preference_key))) {
                WindowManager window = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
                Display display = window.getDefaultDisplay();
                mLiveWallpaperPainting.setBackgroundImageView(sharedPreferences.getString(key, "1"));
                mLiveWallpaperPainting.setSurfaceSize(display.getWidth(), display.getHeight());
                Log.d("SettingsFragment", key + " " + sharedPreferences.getString(key, "1"));
            }
        }
    }
}
