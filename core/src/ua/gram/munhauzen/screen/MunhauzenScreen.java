package ua.gram.munhauzen.screen;

import com.badlogic.gdx.Screen;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public abstract class MunhauzenScreen implements Screen {

    protected final String tag = getClass().getSimpleName();
    public final MunhauzenGame game;

    public MunhauzenScreen(MunhauzenGame game) {
        this.game = game;
    }

    public void openAdultGateBanner(Runnable task) {
        Log.i(tag, "openAdultGateBanner");

    }

    public void destroyBanners() {

    }
}
