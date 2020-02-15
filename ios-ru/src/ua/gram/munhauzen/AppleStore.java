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
            Gdx.net.openURI("https://apps.apple.com/app/id1498389554");
        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }

    @Override
    public void openRateUrl() {
        openUrl();
    }
}
