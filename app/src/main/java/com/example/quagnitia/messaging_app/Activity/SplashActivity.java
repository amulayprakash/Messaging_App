package com.example.quagnitia.messaging_app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.example.quagnitia.messaging_app.Preferences.Preferences;
import com.example.quagnitia.messaging_app.R;

public class SplashActivity extends AppCompatActivity {
    com.example.quagnitia.messaging_app.Preferences.Preferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        TextView txt = findViewById(R.id.txt);

        txt.setAnimation(inFromLeftAnimation());

        preferences = new Preferences(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (preferences.isLogin()) {
                    Intent in = new Intent(SplashActivity.this, WelcomeActivity.class);
                    startActivity(in);
                } else {
                    Intent in = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(in);
                }
                finish();
            }
        }, 1000);


    }

    private Animation inFromLeftAnimation() {
        Animation inFromLeft = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, -1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        inFromLeft.setDuration(800);
        inFromLeft.setInterpolator(new AccelerateInterpolator());
        return inFromLeft;
    }

}
