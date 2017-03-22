package com.android.example.kittenwallpaper;

import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

/**
 * Created by AREG on 20.03.2017.
 */

public class WallpaperAdapter extends RecyclerView.Adapter<WallpaperAdapter.ViewHolder> {

    private final String TAG = "WallpaperAdapter";
    private final int WALLPAPER_SIZE = 10;

    private Context mContext;
    private int[] mBackgroundColors;
    private RecyclerView mRecyclerView;
    private String[] mDataSet = new String[10];

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mText;
        public ImageView mImage;
        public LinearLayout mLinearLayout;

        public ViewHolder(View v) {
            super(v);
            mText  = (TextView) v.findViewById(R.id.text_id);
            mImage = (ImageView) v.findViewById(R.id.image_id);
            mLinearLayout = (LinearLayout) v.findViewById(R.id.linear_layout);
        }
    }

    public WallpaperAdapter(Context context, RecyclerView recyclerView) {
        int i = 0;
        mContext = context;
        mRecyclerView = recyclerView;
        mBackgroundColors = context.getResources().getIntArray(R.array.recycler_view_colors);
        CharSequence[] sets = context.getResources().getTextArray(R.array.wallpaper_names);
        for(CharSequence ch : sets) {
            mDataSet[i++] = ch.toString();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mText.setText(mDataSet[position]);
        Log.d(TAG, "onBindViewHolder() position: " + position);
        holder.mText.setTextColor(mContext.getResources().getColor(R.color.colorBlack));
        holder.mLinearLayout.setBackgroundColor(mBackgroundColors[position]);
        LoadImageView(holder, position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int itemPosition = mRecyclerView.getChildLayoutPosition(v);
                Log.d(TAG, "onClick() position: " + itemPosition);

                // store current wallpaper
                SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                        SetWallpaperActivity.SHARED_PREFERENCES_MAIN_KEY, Context.MODE_PRIVATE);
                sharedPreferences.edit().putInt(SetWallpaperActivity.
                        SHARED_PREFERENCES_POSITION_KEY, itemPosition).commit();

                // call wallpaper preview
                Intent mLiveWallpaperService = new Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
                mLiveWallpaperService.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT, new ComponentName(mContext, LiveWallpaperService.class));
                mContext.startActivity(mLiveWallpaperService);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return WALLPAPER_SIZE;
    }

    private void LoadImageView(ViewHolder holder, int position) {
        switch (position) {
            case 0:
                Glide.with(mContext).load(R.drawable.p1).into(holder.mImage);
                break;
            case 1:
                Glide.with(mContext).load(R.drawable.p2).into(holder.mImage);
                break;
            case 2:
                Glide.with(mContext).load(R.drawable.p3).into(holder.mImage);
                break;
            case 3:
                Glide.with(mContext).load(R.drawable.p4).into(holder.mImage);
                break;
            case 4:
                Glide.with(mContext).load(R.drawable.p5).into(holder.mImage);
                break;
            case 5:
                Glide.with(mContext).load(R.drawable.p6).into(holder.mImage);
                break;
            case 6:
                Glide.with(mContext).load(R.drawable.p7).into(holder.mImage);
                break;
            case 7:
                Glide.with(mContext).load(R.drawable.p8).into(holder.mImage);
                break;
            case 8:
                Glide.with(mContext).load(R.drawable.p9).into(holder.mImage);
                break;
            case 9:
                Glide.with(mContext).load(R.drawable.p10).into(holder.mImage);
                break;
        }
    }
}
