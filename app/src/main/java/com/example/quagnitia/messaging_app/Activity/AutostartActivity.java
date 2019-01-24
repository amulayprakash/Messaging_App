package com.example.quagnitia.messaging_app.Activity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.quagnitia.messaging_app.AutoStartHelper;
import com.example.quagnitia.messaging_app.R;


public class AutostartActivity extends AppCompatActivity {
    public static int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 5469;

    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autostart);

        if (getIntent().getExtras().containsKey("AUTOSTART")) {
            checkPermission();


        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE) {
            if (!Settings.canDrawOverlays(this)) {
                // You don't have permission
                checkPermission();
            } else {
                // Do as per your logic
                showDialogEnableAlarms();
            }

        }

    }

    public void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE);
            } else {
                showDialogEnableAlarms();
            }
        } else {
            showDialogEnableAlarms();
        }
    }

    private void showDialogEnableAlarms() {

        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle("Enable Autostart feature");
        builder1.setMessage(Html.fromHtml(getResources().getString(R.string.enableautoinfo) + ""));
        builder1.setCancelable(false);

        builder1.setPositiveButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        i = 1;
//            Toast.makeText(this,"Navigating you to enable Autostart feature of app.",Toast.LENGTH_SHORT).show();
                        AutoStartHelper.getInstance().getAutoStartPermission(AutostartActivity.this);
                    }
                });

        builder1.setNegativeButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (i == 1) {
            Intent i = new Intent(this, WelcomeActivity.class);
            i.putExtra("FromLogin", true);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(i);
            finish();
        }
    }
}
