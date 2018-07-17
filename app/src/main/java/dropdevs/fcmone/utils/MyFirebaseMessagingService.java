package dropdevs.fcmone.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.util.Map;

import dropdevs.fcmone.MainActivity;
import dropdevs.fcmone.R;
import dropdevs.fcmone.fcm.FirebaseCloudMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d("message","In_onMessageReceived_method");
        String notificationBody = "";
        String notificationTitle = "";
        String notificationData = "";

        try{
            notificationTitle = remoteMessage.getData().get("title");
            notificationBody = remoteMessage.getData().get("message");
            notificationData = remoteMessage.getData().toString();

//            showNotification(remoteMessage);
            sendNotification(remoteMessage);
        }catch (Exception e){
            Log.d("message","Exception: "+e.toString());
        }

        Log.d("received_message","notificationTitle: "+notificationTitle);
        Log.d("received_message","notificationBody: "+notificationBody);
        Log.d("received_message","notificationData: "+notificationData);
    }

    public void sendNotification(RemoteMessage remoteMessage){
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications",NotificationManager.IMPORTANCE_DEFAULT);

            // Configure the notification channel.
            notificationChannel.setDescription("Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);

        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.notillogo)
                .setTicker("Hearty365")
                //     .setPriority(Notification.PRIORITY_MAX)
                .setContentTitle(remoteMessage.getData().get("title"))
                .setContentText(remoteMessage.getData().get("message"));

        notificationManager.notify(/*notification id*/1, notificationBuilder.build());
    }

    private void showNotification(RemoteMessage message) {
        Log.d("message","In_show_notification_method");
        Intent i=new Intent(this,MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_UPDATE_CURRENT);

        if (message.getData().size() > 0){
            Map<String, String> params = message.getData();
            JSONObject object = new JSONObject(params);
            Log.e("JSON_OBJECT", object.toString());
        }




        Notification notification = new Notification.Builder(this)
                .setContentTitle(message.getData().get("title"))
                .setContentText(message.getData().get("message"))
                .setSmallIcon(R.drawable.notillogo)
                .setContentIntent(pendingIntent)
                .build();
        NotificationManagerCompat manager = NotificationManagerCompat.from(getApplicationContext());
        manager.notify(123, notification);
    }

}
