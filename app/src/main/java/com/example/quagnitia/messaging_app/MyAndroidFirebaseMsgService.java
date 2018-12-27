package com.example.quagnitia.messaging_app;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.text.Html;

import com.example.quagnitia.messaging_app.Activity.WelcomeActivity;
import com.example.quagnitia.messaging_app.Preferences.Preferences;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.util.UUID;


public class MyAndroidFirebaseMsgService extends FirebaseMessagingService {
    private static final String CHANNEL_ID = "1";
    static int reqid;
    //    DBHelper dbHelper;
    int currentHour = 0, currentMin = 0, currentZone = 0;
    boolean isCorrect = false;

    public void onMessageReceived(RemoteMessage remoteMessage) {
//        Log.v("GOTMSG", remoteMessage.toString());
//        Log.v("GOTMSG", remoteMessage.getNotification().getBody().toString());
//        Log.v("GOTMSG", remoteMessage.getData().toString());

        Preferences pref = new Preferences(this);

        if (pref.isLogin()) {
            try {
                JSONObject jsonobj = new JSONObject(remoteMessage.getNotification().getBody().toString());
                showNoti(jsonobj.optString("subject"), jsonobj.optString("body"));
                Intent in = new Intent(this, WelcomeActivity.class);
                in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(in);
            } catch (Exception e) {
                e.printStackTrace();
                showNoti("Message", "New status!");
                Intent in = new Intent(this, WelcomeActivity.class);
                in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(in);
            }
        }

    }

    private void showNoti(String title, String msg) {

//        new Preferences(this).setSomeVariable(1);//nikita
//        Intent i = new Intent("com.quagnitia.zapfin.RECEIVE_BADGES").putExtra("some_msg", "NEW NOTIFICATION");
//        this.sendBroadcast(i);//nikita

        String messageBody = Html.fromHtml(msg).toString();
        Intent intent = new Intent(this, WelcomeActivity.class);

        //nikita
        intent.setAction(UUID.randomUUID().toString());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK); // Intent.FLAG_ACTIVITY_SINGLE_TOP or

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.logo3);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(android.R.drawable.ic_btn_speak_now)
                .setLargeIcon(bitmap)
                .setContentTitle(title)
                .setTicker(messageBody)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(reqid++ /* ID of notification */, notificationBuilder.build());
    }

    public void handleIntent(Intent intent) {
        try {
            if (intent.getExtras() != null) {
                RemoteMessage.Builder builder = new RemoteMessage.Builder("MyFirebaseMessagingService");

                for (String key : intent.getExtras().keySet()) {
                    builder.addData(key, intent.getExtras().get(key).toString());
                }

                onMessageReceived(builder.build());
            } else {
                super.handleIntent(intent);
            }
        } catch (Exception e) {
            super.handleIntent(intent);
        }
    }

}

