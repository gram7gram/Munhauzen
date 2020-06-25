package ua.gram.munhauzen.screen;

import com.badlogic.gdx.Screen;

import ua.gram.munhauzen.MunhauzenGame;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public abstract class MunhauzenScreen implements Screen {

    protected final String tag = getClass().getSimpleName();
    public final MunhauzenGame game;

    public MunhauzenScreen(MunhauzenGame game) {
        this.game = game;
    }

}
