package com.android.example.kittenwallpaper;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Random;

/**
 * Created by AREG on 21.03.2017.
 */

public class MovingImage {

    private int mX;
    private int mY;

    private int mSpeed;
    private double mAngle;
    private Bitmap mBmp;

    public MovingImage(Bitmap bmp) {
        mBmp = bmp;

        Random rnd = new Random(System.currentTimeMillis());
        mY = 1000;
        mX = rnd.nextInt(800);
        mSpeed = rnd.nextInt(15 - 5) + 15;
        mAngle = getRandomAngle();
    }

    public void onDraw(Canvas c) {
        update();
        c.drawBitmap(mBmp, mX, mY, null);
    }

    public int getX() {
        return mX;
    }

    public int getY() {
        return mY;
    }

    private int getRandomAngle() {
        Random rnd = new Random(System.currentTimeMillis());
        return rnd.nextInt(1) * 90 + 90 / 2 + rnd.nextInt(15) + 5;
    }

    private void update() {
        mY -= Math.abs(mSpeed * Math.cos(mAngle));
        mX -= Math.abs(mSpeed * Math.sin(mAngle));
    }
}
