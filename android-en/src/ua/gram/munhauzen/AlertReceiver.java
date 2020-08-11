package ua.gram.munhauzen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import static com.facebook.FacebookSdk.getApplicationContext;

public class AlertReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationHelper.displayInternalNotification(getApplicationContext(), SharedPreferencesHelper.getKeyNotification1Title(getApplicationContext()),SharedPreferencesHelper.getKeyNotification1Message(getApplicationContext())+ SharedPreferencesHelper.getLastVisitedDescription(getApplicationContext()), SharedPreferencesHelper.getLastVisitedIcon(getApplicationContext()));

    }
}
