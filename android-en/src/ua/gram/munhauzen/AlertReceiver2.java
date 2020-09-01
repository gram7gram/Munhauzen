package ua.gram.munhauzen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import static com.facebook.FacebookSdk.getApplicationContext;

public class AlertReceiver2 extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationHelper.displayInternalNotification(getApplicationContext(), SharedPreferencesHelper.getKeyNotification2Title(getApplicationContext()), SharedPreferencesHelper.getKeyNotification2Message(getApplicationContext()), "chapter/b_full_version_1.png");

    }
}
