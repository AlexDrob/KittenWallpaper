package com.android.example.kittenwallpaper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.SurfaceHolder;

/**
 * Created by AREG on 21.03.2017.
 */

public class LiveWallpaperPainting extends Thread implements Runnable {

    private final String TAG = "LiveWallpaperPainting";
    private final int MOVING_IMAGE_LENGTH = 200;

    private boolean mRun;
    private boolean mWait;

    private int mDelay;
    private int mWallpaper;
    private Context mContext;
    private static Bitmap mBackgroundImage;
    private static Bitmap mMovingImageView;
    private SurfaceHolder mSurfaceHolder;

    private MovingImage[] mMovingImage = new MovingImage[MOVING_IMAGE_LENGTH];

    public LiveWallpaperPainting(SurfaceHolder surfaceHolder, Context context, int wallpaper) {

        mSurfaceHolder = surfaceHolder;
        mWallpaper = wallpaper;
        mContext = context;
        mWait = true;
        mDelay = 10;

        LoadBackgroundImageView();
    }

    public void setDelay(int delay) {
        mDelay = delay;
    }

    public void setSurfaceSize(int width, int height) {
        synchronized(this) {
            this.notify();
            mBackgroundImage = Bitmap.createScaledBitmap(mBackgroundImage, width, height, true);
        }
    }

    public void stopPainting() {
        mRun = false;
        synchronized(this) {
            this.notify();
        }
    }

    public void createPainting() {
        Canvas c = null;
        if (mSurfaceHolder != null) {
            try {
                c = mSurfaceHolder.lockCanvas();
                if (c != null) {
                    c.drawBitmap(mBackgroundImage, 0, 0, null);
                }
            } finally {
                if (c != null) {
                    mSurfaceHolder.unlockCanvasAndPost(c);
                }
            }
        }
    }

    public void resumePainting() {
        mWait = false;
        synchronized(this) {
            this.notify();
        }
    }

    public void pausePainting() {
        mWait = true;
        synchronized(this) {
            this.notify();
        }
    }

    public void setMovingObject(String object) {
        switch (object) {
            case "1":
                mMovingImageView = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.o1);
                break;
            case "2":
                mMovingImageView = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.o2);
                break;
            case "3":
                mMovingImageView = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.o3);
                break;
            case "4":
                mMovingImageView = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.o4);
                break;
            case "5":
                mMovingImageView = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.o5);
                break;
        }
    }

    public void setSurfaceHolder(SurfaceHolder surfaceHolder) {
        mSurfaceHolder = surfaceHolder;
    }

    public void setBackgroundImageView(String currentImage) {
        mWallpaper = Integer.valueOf(currentImage);
        LoadBackgroundImageView();
    }

    @Override
    public void run() {
        super.run();
        mRun = true;
        Canvas c = null;
        while (mRun) {
            try {
                Thread.sleep(mDelay);
                if (mSurfaceHolder != null) {
                    c = mSurfaceHolder.lockCanvas();
                    synchronized (mSurfaceHolder) {
                        CreateNewMovingElement();
                        doDraw(c);
                        if ((c != null) && (mSurfaceHolder != null)) {
                            mSurfaceHolder.unlockCanvasAndPost(c);
                        }
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } //finally {
            //    if (c != null) {
            //        mSurfaceHolder.unlockCanvasAndPost(c);
            //    }
            //}
            // pause if no need to animate
            synchronized (this) {
                if (mWait) {
                    try {
                        wait();
                    } catch (Exception e) {}
                }
            }
        }
    }

    private void LoadBackgroundImageView() {
        try {
            switch (mWallpaper) {
                case 0:
                    mBackgroundImage = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.p1);
                    break;
                case 1:
                    mBackgroundImage = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.p2);
                    break;
                case 2:
                    mBackgroundImage = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.p3);
                    break;
                case 3:
                    mBackgroundImage = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.p4);
                    break;
                case 4:
                    mBackgroundImage = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.p5);
                    break;
                case 5:
                    mBackgroundImage = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.p6);
                    break;
                case 6:
                    mBackgroundImage = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.p7);
                    break;
                case 7:
                    mBackgroundImage = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.p8);
                    break;
                case 8:
                    mBackgroundImage = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.p9);
                    break;
                case 9:
                    mBackgroundImage = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.p10);
                    break;
            }
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
    }

    private void doDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(mBackgroundImage, 0,0, null);
        for (int i = 0; i < MOVING_IMAGE_LENGTH; i++) {
            if (mMovingImage[i] != null) {
                if (mMovingImage[i].getY() <= 0) {
                    mMovingImage[i] = null;
                } else if((mMovingImage[i].getY() > 0) || (mMovingImage[i].getX() <= 0)) {
                    mMovingImage[i].onDraw(canvas);
                } else {
                    mMovingImage[i] = null;
                }
            }
        }
    }

    private void CreateNewMovingElement() {
        for (int i = 0; i < MOVING_IMAGE_LENGTH; i++) {
            if (mMovingImage[i] == null) {
                mMovingImage[i] = new MovingImage(mMovingImageView);
                break;
            }
        }
    }
}
