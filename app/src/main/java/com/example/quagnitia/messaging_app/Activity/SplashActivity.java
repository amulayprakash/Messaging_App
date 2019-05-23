package com.example.quagnitia.messaging_app.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.example.quagnitia.messaging_app.R;
import com.example.quagnitia.messaging_app.Storage.Preferences;

import java.io.File;

import me.leolin.shortcutbadger.ShortcutBadger;

public class SplashActivity extends AppCompatActivity {
    com.example.quagnitia.messaging_app.Storage.Preferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Preferences(this).setBadgeCount(0);
        ShortcutBadger.removeCount(this);

        TextView txt = findViewById(R.id.txt);

        txt.setAnimation(inFromLeftAnimation());

        preferences = new Preferences(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

//                accessPermission();
                if (preferences.isLogin()) {
                    if(new Preferences(SplashActivity.this).getString("UT").equalsIgnoreCase("admin")) {
                        Intent in = new Intent(SplashActivity.this, SchoolActivity.class);
                        startActivity(in);
                    }else {
                        Intent in = new Intent(SplashActivity.this, MessageTabActivity.class);
                        in.putExtra("schoolId",new Preferences(SplashActivity.this).getString("schoolId"));
                        startActivity(in);
                    }

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

    private static final int REQUEST_CALL_PERMISSION = 100;

    @SuppressLint("NewApi")
    private void accessPermission() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M &&
                ContextCompat.checkSelfPermission(getApplicationContext(),
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                ||
                ContextCompat.checkSelfPermission(getApplicationContext(),
                        android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                ||
                ContextCompat.checkSelfPermission(getApplicationContext(),
                        android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED

                ) {
            requestPermissions(new String[]{
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, REQUEST_CALL_PERMISSION);

        } else {

            try {
                File dir = new File(Environment.getExternalStorageDirectory(), "/ESFAQI/");
                if (!dir.exists()) {
                    dir.mkdirs();
                } else {
                    try {
//                                File dir = new File(Environment.getExternalStorageDirectory()+"Dir_name_here");
                        if (dir.isDirectory())
                        {
                            String[] children = dir.list();
                            for (int i = 0; i < children.length; i++)
                            {
                                new File(dir, children[i]).delete();
                            }
                        }
                        dir.mkdirs();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (preferences.isLogin()) {
                if(new Preferences(SplashActivity.this).getString("UT").equalsIgnoreCase("admin")) {
                    Intent in = new Intent(SplashActivity.this, SchoolActivity.class);
                    startActivity(in);
                }else {
                    Intent in = new Intent(SplashActivity.this, MessageTabActivity.class);
                    in.putExtra("schoolId",new Preferences(SplashActivity.this).getString("schoolId"));
                    startActivity(in);
                }
            } else {
                Intent in = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(in);
            }
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CALL_PERMISSION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    try {
                        File dir = new File(Environment.getExternalStorageDirectory(), "/ESFAQI/");
                        if (!dir.exists()) {
                            dir.mkdirs();
                        } else {
                            try {
//                                File dir = new File(Environment.getExternalStorageDirectory()+"Dir_name_here");
                                if (dir.isDirectory())
                                {
                                    String[] children = dir.list();
                                    for (int i = 0; i < children.length; i++)
                                    {
                                        new File(dir, children[i]).delete();
                                    }
                                }
                                dir.mkdirs();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (preferences.isLogin()) {
                        if(new Preferences(SplashActivity.this).getString("UT").equalsIgnoreCase("admin")) {
                            Intent in = new Intent(SplashActivity.this, SchoolActivity.class);
                            startActivity(in);
                        }else {
                            Intent in = new Intent(SplashActivity.this, MessageTabActivity.class);
                            in.putExtra("schoolId",new Preferences(SplashActivity.this).getString("schoolId"));
                            startActivity(in);
                        }
                    } else {
                        Intent in = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(in);
                    }
                    finish();

                } else {
                    accessPermission();
                }
                return;
            }
        }
    }
}
