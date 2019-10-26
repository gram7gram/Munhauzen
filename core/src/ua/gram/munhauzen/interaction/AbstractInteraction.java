package ua.gram.munhauzen.interaction;

import com.badlogic.gdx.utils.Disposable;

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
    public ExpansionAssetManager assetManager;

    public AbstractInteraction(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    public String t(String key) {
        return gameScreen.game.t(key);
    }

    public void start() {
        Log.i(tag, "start");

        assetManager = new ExpansionAssetManager();

        GameState.unpause(tag);

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

        GameState.clearTimer();

        GameState.unpause(tag);
    }
}
