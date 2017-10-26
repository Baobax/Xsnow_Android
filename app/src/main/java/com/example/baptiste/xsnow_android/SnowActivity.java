package com.example.baptiste.xsnow_android;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.sip.SipAudioCall;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class SnowActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        int fond = intent.getIntExtra("fond", 0);
        int drawable1 = intent.getIntExtra("image1", 0);
        int drawable2 = intent.getIntExtra("image2", 0);

        CustomView surfaceView = new CustomView(this, drawable1, drawable2);
        surfaceView.setZOrderOnTop(true);
        surfaceView.getHolder().setFormat(PixelFormat.TRANSPARENT);
        // Setup your ImageView
        ImageView bgImagePanel = new ImageView(this);
        bgImagePanel.setBackgroundResource(fond); // use any Bitmap or BitmapDrawable you want
        // Use a RelativeLayout to overlap both SurfaceView and ImageView
        RelativeLayout.LayoutParams matchParentLayout = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        RelativeLayout rootPanel = new RelativeLayout(this);
        rootPanel.setLayoutParams(matchParentLayout);
        rootPanel.addView(surfaceView, matchParentLayout);
        rootPanel.addView(bgImagePanel, matchParentLayout);

        setContentView(rootPanel);
    }
}
