package ua.gram.munhauzen;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

import static com.facebook.FacebookSdk.getApplicationContext;

public class AlertReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationHelper.displayInternalNotification(getApplicationContext(), SharedPreferencesHelper.getKeyNotification1Title(getApplicationContext()),SharedPreferencesHelper.getKeyNotification1Message(getApplicationContext())+ SharedPreferencesHelper.getLastVisitedDescription(getApplicationContext()), SharedPreferencesHelper.getLastVisitedIcon(getApplicationContext()));

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
}
