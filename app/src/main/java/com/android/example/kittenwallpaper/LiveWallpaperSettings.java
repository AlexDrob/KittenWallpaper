package com.android.example.kittenwallpaper;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.util.Log;

/**
 * Created by AREG on 17.03.2017.
 */

public class LiveWallpaperSettings extends PreferenceActivity {

    private final String TAG = "LiveWallpaperSettings";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate() called");
        addPreferencesFromResource(R.xml.preference);
    }
}
