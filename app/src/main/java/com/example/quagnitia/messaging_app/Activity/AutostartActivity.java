package com.example.quagnitia.messaging_app.Activity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;

import com.example.quagnitia.messaging_app.R;
import com.example.quagnitia.messaging_app.Storage.Preferences;
import com.example.quagnitia.messaging_app.util.AutoStartHelper;

/*
- created by nikita
 */


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
                showDialogEnableAlarms();
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
            if(new Preferences(this).getString("UT").equalsIgnoreCase("admin")) {
                Intent i = new Intent(this, SchoolActivity.class);
                i.putExtra("FromLogin", true);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
                finish();
            }else {
                Intent i = new Intent(this, MessageTabActivity.class);
                i.putExtra("FromLogin", true);
//                i.putExtra("schoolId",getIntent().getStringExtra("schoolId")+"");
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
                finish();
            }
        }
    }
}
