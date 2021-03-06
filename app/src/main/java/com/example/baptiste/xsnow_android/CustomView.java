package com.example.baptiste.xsnow_android;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Random;

public class CustomView extends SurfaceView implements SurfaceHolder.Callback {
    // holder
    private final SurfaceHolder mSurfaceHolder;
    // thread in which the drawing will be done
    private DrawingThread mThread;
    private static final String TAG = "MyActivity";

    private ZoneDessin mZoneDessin[][];
    private int mWidth, mHeight;
    private int mCount;
    private Bitmap mImage1, mImage2;
    private Random mRand = new Random();
    private final SynchronizeObject mSynchronizedObject;
    private int mTailleCase;

    public CustomView(Context context, int drawable1, int drawable2) {
        super(context);
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
        mThread = new DrawingThread();
        mSynchronizedObject = new SynchronizeObject();
        int tailleImage = getResources().getDimensionPixelSize(R.dimen.tailleImage);
        mTailleCase = getResources().getDimensionPixelSize(R.dimen.tailleCase);
        mImage1 = BitmapFactory.decodeResource(context.getResources(), drawable1);
        mImage1 = getResizedBitmap(mImage1, tailleImage, tailleImage);
        mImage2 = BitmapFactory.decodeResource(context.getResources(), drawable2);
        mImage2 = getResizedBitmap(mImage2, tailleImage, tailleImage);
        mCount = 1;
    }

    @Override
    public void draw(Canvas canvas) {
        Log.i(TAG, "------------Draw------------");
        super.draw(canvas);
        Log.i(TAG, "Width : " + mWidth + ", Height : " + mHeight);
        Log.i(TAG, "CWidth : " + canvas.getWidth() + ", CHeight : " + canvas.getHeight());
        canvas.drawColor(0, PorterDuff.Mode.CLEAR);
        for (int y = (mHeight / mTailleCase) - 1; y >= 0; y--) {
            for (int x = (mWidth / mTailleCase) - 1; x >= 0; x--) {
                if ((mZoneDessin[x][y].isImage())) {
                    canvas.drawBitmap(mZoneDessin[x][y].getImage(), x * mTailleCase, y * mTailleCase, null);
                }
            }
        }
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
    }

    public void createElement(boolean vent) {
        if (vent) {
            int y = mRand.nextInt(mHeight / mTailleCase);
            if (mRand.nextInt(2) == 0) {
                mZoneDessin[(mWidth / mTailleCase) - 1][y].setImage(mImage1);
            } else {
                mZoneDessin[(mWidth / mTailleCase) - 1][y].setImage(mImage2);
            }
            int maxVitesse = mRand.nextInt(31 - 20) + 20;
            int direction = mRand.nextInt(maxVitesse);
            mZoneDessin[(mWidth / mTailleCase) - 1][y].setDirectionVitesse(direction, maxVitesse);
        } else {
            int x = mRand.nextInt(mWidth / mTailleCase);
            if (mRand.nextInt(2) == 0) {
                mZoneDessin[x][0].setImage(mImage1);
            } else {
                mZoneDessin[x][0].setImage(mImage2);
            }
            int maxVitesse = mRand.nextInt(31 - 20) + 20;
            int direction = mRand.nextInt(maxVitesse);
            mZoneDessin[x][0].setDirectionVitesse(direction, maxVitesse);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        synchronized (mSynchronizedObject) {
            mSynchronizedObject.pauseOtherThread();
            Log.i(TAG, "------------Size  changed------------");
            super.onSizeChanged(w, h, oldw, oldh);
            mWidth = w;
            mHeight = h;
            mZoneDessin = new ZoneDessin[mWidth / mTailleCase][mHeight / mTailleCase];
            for (int y = (mHeight / mTailleCase) - 1; y >= 0; y--) {
                for (int x = (mWidth / mTailleCase) - 1; x >= 0; x--) {
                    int n = mRand.nextInt(10000);
                    if (n == 0) {
                        if (mRand.nextInt(2) == 0) {
                            mZoneDessin[x][y] = new ZoneDessin(mImage1);
                        } else {
                            mZoneDessin[x][y] = new ZoneDessin(mImage2);
                        }
                    } else {
                        mZoneDessin[x][y] = new ZoneDessin(null);
                    }
                    int maxVitesse = mRand.nextInt(31 - 20) + 20;
                    int direction = mRand.nextInt(maxVitesse);
                    mZoneDessin[x][y].setDirectionVitesse(direction, maxVitesse);
                }
            }
            Log.i(TAG, "Width : " + mWidth + ", Height : " + mHeight);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.i(TAG, "-----------Surface created-----------");
        mThread.keepDrawing = true;
        mThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.i(TAG, "-----------Surface changed-----------");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.i(TAG, "----------Surface destroyed----------");
        /*Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
        Thread[] threadArray = threadSet.toArray(new Thread[threadSet.size()]);
        for(int i = 0; i < threadArray.length; i++) {
            Log.i(TAG, threadArray[i].toString());
        }*/
        mThread.keepDrawing = false;

        boolean joined = false;
        while (!joined) {
            try {
                mThread.join();
                joined = true;
            } catch (InterruptedException e) {
                Log.e(TAG, "Erreur : Thread.join");
            }
        }
        mThread = new DrawingThread();

    }

    private class DrawingThread extends Thread {
        // to stop drawing when needed
        boolean keepDrawing = true;

        @Override
        public void run() {
            final int DELAI_CREATION_NOUVELLE_PARTICULE = 20;
            final int DELAI_CREATION_NOUVELLE_PARTICULE_VENT = 5;
            final boolean VENT = true;
            final boolean PAS_DE_VENT = false;
            int particleDirection;

            while (keepDrawing) {
                Canvas canvas = null;
                synchronized (mSynchronizedObject) {
                    mSynchronizedObject.pauseOtherThread();
                    if (mCount % 2500 >= 2400 && mCount % 2500 <= 2499) { // wind to reinitialize the drawing zone
                        for (int x = 0; x < mWidth / mTailleCase; x++) {
                            for (int y = (mHeight / mTailleCase) - 1; y >= 0; y--) {
                                if ((mZoneDessin[x][y].isImage())) {
                                    if (x - 4 >= 0) {
                                        if (mCount % 2000 > 1980) {
                                            mZoneDessin[x - 2][y].deplacerElement(mZoneDessin[x][y].getImage(), mZoneDessin[x][y].getDirection(), mZoneDessin[x][y].getMaxVitesseDirection());
                                        } else if (mCount % 2000 > 1960) {
                                            mZoneDessin[x - 3][y].deplacerElement(mZoneDessin[x][y].getImage(), mZoneDessin[x][y].getDirection(), mZoneDessin[x][y].getMaxVitesseDirection());
                                        } else {
                                            mZoneDessin[x - 4][y].deplacerElement(mZoneDessin[x][y].getImage(), mZoneDessin[x][y].getDirection(), mZoneDessin[x][y].getMaxVitesseDirection());
                                        }
                                    }
                                    mZoneDessin[x][y].setImage(null);
                                }
                            }
                        }

                        if ((mCount % DELAI_CREATION_NOUVELLE_PARTICULE_VENT) == 0) {
                            createElement(VENT);
                        }
                    } else { // normal behavior without wind
                        for (int y = (mHeight / mTailleCase) - 2; y >= 0; y--) {
                            for (int x = (mWidth / mTailleCase) - 1; x >= 0; x--) {
                                if ((mZoneDessin[x][y].isImage())) {
                                    particleDirection = mZoneDessin[x][y].getUpdatedDirection();
                                    calculMouvementParticule(particleDirection, x, y);
                                }
                            }
                        }

                        if ((mCount % DELAI_CREATION_NOUVELLE_PARTICULE) == 0) {
                            createElement(PAS_DE_VENT);
                        }
                    }
                }
                mCount++;

                try {
                    // we take the canvas to draw
                    canvas = mSurfaceHolder.lockCanvas();
                    // we make sure that no other thread is accessing the holder
                    synchronized (mSurfaceHolder) {
                        if (!keepDrawing) {
                            break;
                        }
                        synchronized (mSynchronizedObject) {
                            mSynchronizedObject.pauseOtherThread();
                            draw(canvas);
                        }
                    }
                } finally {
                    // releasing the canvas to draw
                    if (canvas != null) {
                        mSurfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }

                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    Log.e(TAG, "Erreur : Thread.sleep");
                }
            }
        }

        private void calculMouvementParticule(int particleDirection, int x, int y) {
            if (x + particleDirection < mWidth / mTailleCase && x + particleDirection >= 0) {
                if (mZoneDessin[x + particleDirection][y + 1].isImage()) {
                    if (!mZoneDessin[x][y + 1].isImage()) {
                        mZoneDessin[x][y + 1].deplacerElement(mZoneDessin[x][y].getImage(), mZoneDessin[x][y].getDirection(), mZoneDessin[x][y].getMaxVitesseDirection());
                        mZoneDessin[x][y].setImage(null);
                    }
                } else {
                    mZoneDessin[x + particleDirection][y + 1].deplacerElement(mZoneDessin[x][y].getImage(), mZoneDessin[x][y].getDirection(), mZoneDessin[x][y].getMaxVitesseDirection());
                    mZoneDessin[x][y].setImage(null);
                }
            } else {
                if (!mZoneDessin[x][y + 1].isImage()) {
                    mZoneDessin[x][y + 1].deplacerElement(mZoneDessin[x][y].getImage(), mZoneDessin[x][y].getDirection(), mZoneDessin[x][y].getMaxVitesseDirection());
                    mZoneDessin[x][y].setImage(null);
                }
            }
        }
    }
}
