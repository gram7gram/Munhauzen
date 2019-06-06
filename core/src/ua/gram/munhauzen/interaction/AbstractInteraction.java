package ua.gram.munhauzen.interaction;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Disposable;

import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public abstract class AbstractInteraction implements Disposable {

    final String tag = getClass().getSimpleName();
    final GameScreen gameScreen;
    final AssetManager assetManager;

    public AbstractInteraction(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        assetManager = new AssetManager();
    }

    public void start() {
        Log.i(tag, "start");

    }

    public void update() {

    }

    @Override
    public void dispose() {
        Log.i(tag, "dispose");
        assetManager.dispose();
    }
}
