package ua.gram.munhauzen.interaction;

import com.badlogic.gdx.utils.GdxRuntimeException;

import ua.gram.munhauzen.screen.GameScreen;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class InteractionFactory {

    public final static String HARE = "HARE";

    public static AbstractInteraction create(GameScreen gameScreen, String type) {

        switch (type) {
            case HARE: return new HareInteraction(gameScreen);
        }

        throw new GdxRuntimeException("No such interaction " + type);
    }
}
