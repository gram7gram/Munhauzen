package ua.gram.munhauzen;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import ua.gram.munhauzen.utils.AppStore;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class AndroidAppStore implements AppStore {

    final Context context;
    final PlatformParams params;

    public AndroidAppStore(PlatformParams params, Context context) {
        this.params = params;
        this.context = context;
    }

    @Override
    public void openUrl() {
        Uri uri = Uri.parse("market://details?id=" + params.applicationId);
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);

        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=" + params.applicationId)));
        }
    }

    @Override
    public void openRateUrl() {
        openUrl();
    }
}
