package com.android.example.kittenwallpaper;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

/**
 * Created by AREG on 17.03.2017.
 */

public class SetWallpaperActivity extends Activity {

    public final static String SHARED_PREFERENCES_MAIN_KEY = "SharedPreferences_MainKey";
    public final static String SHARED_PREFERENCES_POSITION_KEY = "SharedPreferences_PositionKey";

    private final String TAG = "SetWallpaperActivity";

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selection_wallpaper);

        Log.d(TAG, "onCreate() called");

        getActionBar().setBackgroundDrawable(new ColorDrawable(getResources()
                .getColor(R.color.colorActionBar)));
        mRecyclerView = (RecyclerView) findViewById(R.id.wallpaper_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new WallpaperAdapter(this, mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
    }
}
