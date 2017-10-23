package com.example.baptiste.xsnow_android;

import android.graphics.Bitmap;

public class ZoneDessin {
    private Bitmap mImage;
    private int mDirection;
    private int mMaxVitesseDirection;

    public ZoneDessin(Bitmap image) {
        mImage = image;
    }

    public Bitmap getImage() {
        return mImage;
    }

    public void setImage(Bitmap image) {
        mImage = image;
    }

    public void deplacerElement(Bitmap image, int direction, int maxVitesseDirection) {
        mImage = image;
        mDirection = direction;
        mMaxVitesseDirection = maxVitesseDirection;
    }

    public boolean isImage() {
        return mImage != null;
    }

    public int getUpdatedDirection() {
        if (mDirection == mMaxVitesseDirection) {
            setDirection(0);
            return -1;
        } else if (mDirection < mMaxVitesseDirection / 2) {
            setDirection(getDirection() + 1);
            return -1;
        } else {
            setDirection(getDirection() + 1);
            return 1;
        }
    }

    public int getDirection() {
        return mDirection;
    }

    public void setDirection(int direction) {
        mDirection = direction;
    }

    public void setDirectionVitesse(int direction, int maxVitesseDirection) {
        mDirection = direction;
        mMaxVitesseDirection = maxVitesseDirection;
    }

    public int getMaxVitesseDirection() {
        return mMaxVitesseDirection;
    }
}
