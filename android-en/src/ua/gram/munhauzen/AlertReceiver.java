package ua.gram.munhauzen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import static com.facebook.FacebookSdk.getApplicationContext;

public class AlertReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationHelper.displayInternalNotification(getApplicationContext(), "Continue Listening","You have stopped at Chapter: "+ SharedPreferencesHelper.getDescription(getApplicationContext()), SharedPreferencesHelper.getIcon(getApplicationContext()));

    }
}
