package ua.gram.munhauzen;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;

import java.io.File;

import ru.munchausen.fingertipsandcompany.full.R;


public class NotificationHelper {


    public static void displayNotification(Context context, String title, String body){

        sendNotification(context,title,body);

    }

    public static void displayInternalNotification(Context context, String title, String body, String icon){

        sendInternalNotification(context,title,body, icon);

    }



    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private static void sendNotification(Context context, String title, String messageBody) {
        Intent intent = new Intent(context, AndroidLauncher.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT
        );

        String channelId = MunhauzenApp.CHANNEL_ID;


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);



        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel( channelId, "Munhauzen Notifications",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    private static void sendInternalNotification(Context context, String title, String messageBody, String icon) {
        Intent intent = new Intent(context, AndroidLauncher.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT
        );

        String channelId = MunhauzenApp.CHANNEL_ID;


        //for image

        File imageFile = new File(Environment.getExternalStorageDirectory() + "/.Munchausen/en.munchausen.fingertipsandcompany.any/expansion/" , icon);
        //for image compression
        Bitmap bitmap=null;
        try {
            bitmap = BitmapFactory.decodeFile(imageFile.getPath());
        } catch (Throwable t) {
            t.printStackTrace();
        }


        //for image ends


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(bitmap)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);



        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel( channelId, "Munhauzen Notifications",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

}
