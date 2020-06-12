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

//        gameScreen.audioService.stop();

        assetManager = new ExpansionAssetManager(gameScreen.game);

        GameState.unpause(tag);

    }

    public void update() {
        try {
            if (assetManager != null) {
                assetManager.update();
            }
        } catch (Throwable ignore) {
        }
    }

    @Override
    public void dispose() {
        Log.i(tag, "dispose");

//        gameScreen.audioService.stop();

        if (assetManager != null) {
            assetManager.dispose();
            assetManager = null;
        }

        GameState.clearTimer(tag);

        GameState.unpause(tag);
    }

    public abstract boolean isValid();
}
