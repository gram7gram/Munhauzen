package ua.gram.munhauzen;

import com.badlogic.gdx.Gdx;

import ua.gram.munhauzen.utils.AppStore;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class AppleStore implements AppStore {

    final String tag = getClass().getSimpleName();
    final PlatformParams params;

    public AppleStore(PlatformParams params) {
        this.params = params;
    }

    @Override
    public void openUrl() {
        try {
            Gdx.net.openURI("https://www.apple.com/ios/app-store?id=" + params.applicationId);
        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }

    @Override
    public void openRateUrl() {
        openUrl();
    }
}
