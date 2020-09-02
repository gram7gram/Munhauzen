package ua.gram.munhauzen;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;

import static com.facebook.FacebookSdk.getApplicationContext;

public class AlertReceiver2 extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {



        //for setting random values for notificaitons messages

        String notificationJson = readNotificationJsonFile();

        try{
            JSONObject notificationJsonObject = new JSONObject(notificationJson);

            //for download notification
            JSONObject downloadNotifObject = notificationJsonObject.getJSONObject("download_notification");

            String download_notification = downloadNotifObject.getString("download_notification_text_" + (((int) (Math.random() * 7)) + 1));

            SharedPreferencesHelper.setKeyNotification2Message(getApplicationContext(), download_notification);

        }catch (Exception e){
            e.printStackTrace();
        }

        //for setting random values for notificaitons messages ends

        NotificationHelper.displayInternalNotification(getApplicationContext(), SharedPreferencesHelper.getKeyNotification2Title(getApplicationContext()), SharedPreferencesHelper.getKeyNotification2Message(getApplicationContext()), "chapter/b_full_version_1.png");

        //for rescheduling notification
        if(AndroidLauncher.needToDownloadStatic == true) {
            Calendar c1 = Calendar.getInstance();
            c1.add(Calendar.SECOND, SharedPreferencesHelper.getNotification2Time(getApplicationContext()));


            AlarmManager alarmManager1 = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
            Intent intent1 = new Intent(getApplicationContext(), AlertReceiver2.class);
            intent1.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            PendingIntent pendingIntent1 = PendingIntent.getBroadcast(getApplicationContext(), 2, intent1, 0);
            if (alarmManager1 != null) {
                alarmManager1.setExact(AlarmManager.RTC_WAKEUP, c1.getTimeInMillis(), pendingIntent1);
            }
        }

        //rescheduling ends

    }

    private String readNotificationJsonFile() {
        String json = null;
        try {
            InputStream is = getApplicationContext().getAssets().open("notification_texts.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }
}
