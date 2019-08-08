package ua.gram.munhauzen.interaction;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Timer;

import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.utils.ExpansionAssetManager;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public abstract class AbstractInteraction implements Disposable {

    final String tag = getClass().getSimpleName();
    public final GameScreen gameScreen;
    public AssetManager assetManager;

    public AbstractInteraction(GameScreen gameScreen) {
        this.gameScreen = gameScreen;

        assetManager = new ExpansionAssetManager();
    }

    public void start() {
        Log.i(tag, "start");

        GameState.unpause();

    }

    public void drawOnTop() {

    }

    public void update() {
        if (assetManager != null) {
            assetManager.update();
        }
    }

    @Override
    public void dispose() {
        Log.i(tag, "dispose");

        if (assetManager != null) {
            assetManager.dispose();
            assetManager = null;
        }

        Timer.instance().clear();

        GameState.unpause();
    }
}
