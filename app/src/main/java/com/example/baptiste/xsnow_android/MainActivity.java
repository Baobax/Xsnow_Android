package com.example.baptiste.xsnow_android;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.SurfaceView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int drawable1 = R.drawable.flocon1;
        int drawable2 = R.drawable.flocon2;

        CustomView surfaceView = new CustomView(this, drawable1, drawable2);
        surfaceView.setZOrderOnTop(true);
        surfaceView.getHolder().setFormat(PixelFormat.TRANSPARENT);
        // Setup your ImageView
        ImageView bgImagePanel = new ImageView(this);
        bgImagePanel.setBackgroundResource(R.drawable.fond_test); // use any Bitmap or BitmapDrawable you want
        // Use a RelativeLayout to overlap both SurfaceView and ImageView
        RelativeLayout.LayoutParams matchParentLayout = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        RelativeLayout rootPanel = new RelativeLayout(this);
        rootPanel.setLayoutParams(matchParentLayout);
        rootPanel.addView(surfaceView, matchParentLayout);
        rootPanel.addView(bgImagePanel, matchParentLayout);

        setContentView(rootPanel);
    }
}
