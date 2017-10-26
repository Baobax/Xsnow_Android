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

public class MainActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button themeNeige = findViewById(R.id.btnNeige);
        Button themeHalloween = findViewById(R.id.btnHalloween);
        Button themeAutomne = findViewById(R.id.btnAutomne);
        themeNeige.setOnClickListener(this);
        themeHalloween.setOnClickListener(this);
        themeAutomne.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, SnowActivity.class);
        int fond, image1, image2;

        switch (v.getId()) {
            case R.id.btnNeige:
                fond = R.drawable.fond_test;
                image1 = R.drawable.flocon1;
                image2 = R.drawable.flocon2;
                intent.putExtra("fond", fond);
                intent.putExtra("image1", image1);
                intent.putExtra("image2", image2);
                startActivity(intent);
                break;
            case R.id.btnHalloween:
                fond = R.drawable.fond_test;
                image1 = R.drawable.citrouille;
                image2 = R.drawable.fantome;
                intent.putExtra("fond", fond);
                intent.putExtra("image1", image1);
                intent.putExtra("image2", image2);
                startActivity(intent);
                break;
            case R.id.btnAutomne:
                fond = R.drawable.fond_test;
                image1 = R.drawable.feuille1;
                image2 = R.drawable.feuille2;
                intent.putExtra("fond", fond);
                intent.putExtra("image1", image1);
                intent.putExtra("image2", image2);
                startActivity(intent);
                break;
        }
    }
}
