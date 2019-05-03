package com.example.quagnitia.messaging_app;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.text.Html;

import com.example.quagnitia.messaging_app.Activity.MessageListActivity;
import com.example.quagnitia.messaging_app.Activity.SchoolActivity;
import com.example.quagnitia.messaging_app.Storage.Preferences;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.UUID;

import me.leolin.shortcutbadger.ShortcutBadger;


public class MyAndroidFirebaseMsgService extends FirebaseMessagingService {

    static int reqid;

    public void onMessageReceived(RemoteMessage remoteMessage) {

        Preferences pref = new Preferences(this);
//        dbHelper = new DBHelper(this);
//        AlarmLogTable alogger = new AlarmLogTable(this, dbHelper);

        if (pref.isLogin()) {
//            if (!pref.getString("UT").equalsIgnoreCase("admin")) {

            try {
//                AlarmLogTable.insertLogData("Step 1: FCM received", remoteMessage.getNotification().getTitle().toString());

//                    JSONObject jsonobj = new JSONObject(remoteMessage.getNotification().getBody().toString());
//                    showNoti(jsonobj.optString("subject"), jsonobj.optString("body"));
//                    ArrayList<String> ar = pref.getListString("SCH");
//                    ar.add()
//                    pref.putListString();
                sendNotification(remoteMessage.getNotification().getTitle().toString(), remoteMessage.getNotification().getBody().toString());

                pref.setBadgeCount(1);
                ShortcutBadger.applyCount(this, 1);
//                    ShortcutBadger.with(getApplicationContext()).count(badgeCount); //for 1.1.3
//                    Intent in = new Intent(this, MessageListActivity.class);
//                    in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(in);


//                AlarmLogTable.insertLogData("Step 3: In fun to open Ok activity", remoteMessage.getNotification().getTitle().toString());

            } catch (Exception e) {
//                AlarmLogTable.insertLogData("Error in fcm fun", "try catch data parsing");

                e.printStackTrace();
//                showNoti("Text", "New status!");
//                Intent in = new Intent(this, WelcomeActivity.class);
//                in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(in);
            }
        }
//        }
    }

    private void sendNotification(String title, String messageBody)
    {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "st123");
        Intent intent;

        if (!new Preferences(this).getString("UT").equalsIgnoreCase("admin")) {
            intent = new Intent(this, MessageListActivity.class);
        } else {
            intent = new Intent(this, SchoolActivity.class);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        Uri notificationSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        ///**For sound**/ assignmentNotification.defaults |= Notification.DEFAULT_SOUND;
        long[] vibrate = {0, 100, 200, 300};

        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.bigText(title);
        bigText.setBigContentTitle(messageBody);

        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSmallIcon(R.drawable.aqi);
        mBuilder.setContentTitle(title);
        mBuilder.setContentText(messageBody);
        mBuilder.setPriority(Notification.PRIORITY_MAX);
        mBuilder.setAutoCancel(true);
        mBuilder.setStyle(bigText);
        mBuilder.setShowWhen(true);
        mBuilder.setSound(notificationSoundUri);
        mBuilder.setVibrate(vibrate);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel channel = new NotificationChannel("st123", "st456", NotificationManager.IMPORTANCE_DEFAULT);
            mNotificationManager.createNotificationChannel(channel);
        }

        mNotificationManager.notify(0, mBuilder.build());
    }

    private void showNoti(String title, String msg) {

//        new Preferences(this).setSomeVariable(1);//nikita
//        Intent i = new Intent("com.quagnitia.zapfin.RECEIVE_BADGES").putExtra("some_msg", "NEW NOTIFICATION");
//        this.sendBroadcast(i);//nikita

        String messageBody = Html.fromHtml(msg).toString().replace("\n", " ");
        Intent intent;

        if (!new Preferences(this).getString("UT").equalsIgnoreCase("admin")) {
            intent = new Intent(this, MessageListActivity.class);
        } else {
            intent = new Intent(this, SchoolActivity.class);
        }
        //nikita
        intent.setAction(UUID.randomUUID().toString());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK); // Intent.FLAG_ACTIVITY_SINGLE_TOP or

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.aqi);
        Uri notificationSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        ///**For sound**/ assignmentNotification.defaults |= Notification.DEFAULT_SOUND;
        long[] vibrate = {0, 100, 200, 300};
        ///**For vibrate**/ assignmentNotification.vibrate = vibrate;
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setLargeIcon(bitmap)
                .setContentTitle(title)
                .setTicker(messageBody)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))
                .setContentText(messageBody)
                .setAutoCancel(true).setShowWhen(true)
                .setContentIntent(pendingIntent);


        notificationBuilder.setPriority(Notification.PRIORITY_HIGH);
        notificationBuilder.setSound(notificationSoundUri);
        notificationBuilder.setVibrate(vibrate);


        NotificationManager notificationManager =
                (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

//        notificationManager.notify(reqid++ /* ID of notification */, notificationBuilder.build());

        NotificationManager mNotificationManager =
                (NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            if (notificationSoundUri != null) {
                // Changing Default mode of notification
                notificationBuilder.setDefaults(Notification.DEFAULT_VIBRATE);

                // Creating an Audio Attribute
                AudioAttributes audioAttributes = new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .setUsage(AudioAttributes.USAGE_UNKNOWN)
                        .build();

                // Creating Channel
                NotificationChannel notificationChannel = new NotificationChannel("123", "Notifications", NotificationManager.IMPORTANCE_HIGH);
                notificationChannel.setSound(notificationSoundUri, audioAttributes);
                mNotificationManager.createNotificationChannel(notificationChannel);
            }
        }
        mNotificationManager.notify(reqid, notificationBuilder.build());

//        AlarmLogTable.insertLogData("Step 2: Notification shown", title);
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

