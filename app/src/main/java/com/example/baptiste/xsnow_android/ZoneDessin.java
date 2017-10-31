package com.example.baptiste.xsnow_android;

import android.graphics.Bitmap;

class ZoneDessin {
    private Bitmap mImage;
    private int mDirection;
    private int mMaxVitesseDirection;

    ZoneDessin(Bitmap image) {
        mImage = image;
    }

    Bitmap getImage() {
        return mImage;
    }

    void setImage(Bitmap image) {
        mImage = image;
    }

    void deplacerElement(Bitmap image, int direction, int maxVitesseDirection) {
        mImage = image;
        mDirection = direction;
        mMaxVitesseDirection = maxVitesseDirection;
    }

    boolean isImage() {
        return mImage != null;
    }

    int getUpdatedDirection() {
        if (mDirection == mMaxVitesseDirection) {
            mDirection = 0;
            return -1;
        } else if (mDirection < mMaxVitesseDirection / 2) {
            mDirection = getDirection() + 1;
            return -1;
        } else {
            mDirection = getDirection() + 1;
            return 1;
        }
    }

    int getDirection() {
        return mDirection;
    }

    void setDirectionVitesse(int direction, int maxVitesseDirection) {
        mDirection = direction;
        mMaxVitesseDirection = maxVitesseDirection;
    }

    int getMaxVitesseDirection() {
        return mMaxVitesseDirection;
    }
}
