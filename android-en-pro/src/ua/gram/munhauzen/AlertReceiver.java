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

public class AlertReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {


        //for setting random values for notificaitons messages

        String notificationJson = readNotificationJsonFile();

        try {
            JSONObject notificationJsonObject = new JSONObject(notificationJson);

            //for Continue notification
            JSONObject continueNotifObject = notificationJsonObject.getJSONObject("continue_notification");

            String continue_notification = continueNotifObject.getString("continue_notification_text_" + (((int) (Math.random() * 7)) + 1));

            SharedPreferencesHelper.setKeyNotification1Message(getApplicationContext(), continue_notification);

        } catch (Exception e) {
            e.printStackTrace();
        }

        //for setting random values for notificaitons messages ends


        NotificationHelper.displayInternalNotification(getApplicationContext(), SharedPreferencesHelper.getKeyNotification1Title(getApplicationContext()), SharedPreferencesHelper.getKeyNotification1Message(getApplicationContext()) + SharedPreferencesHelper.getLastVisitedDescription(getApplicationContext()), SharedPreferencesHelper.getLastVisitedIcon(getApplicationContext()));

        //for rescheduling alarm

        Calendar c = Calendar.getInstance();
        c.add(Calendar.SECOND, SharedPreferencesHelper.getNotification1Time(getApplicationContext()));

        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent1 = new Intent(getApplicationContext(), AlertReceiver.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 1, intent1, 0);
        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
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
