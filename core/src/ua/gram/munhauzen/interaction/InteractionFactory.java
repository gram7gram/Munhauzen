package ua.gram.munhauzen.interaction;

import com.badlogic.gdx.utils.GdxRuntimeException;

import ua.gram.munhauzen.screen.GameScreen;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class InteractionFactory {

    public final static String HARE = "HARE";
    public final static String CONTINUE = "CONTINUE";
    public final static String GENERALS = "GENERALS";
    public final static String PUZZLE = "PUZZLE";
    public final static String CHAPTER = "CHAPTER";

    public static AbstractInteraction create(GameScreen gameScreen, String type) {

        switch (type) {
            case HARE: return new HareInteraction(gameScreen);
            case CONTINUE: return new ContinueInteraction(gameScreen);
            case GENERALS: return new GeneralsInteraction(gameScreen);
            case PUZZLE: return new PuzzleInteraction(gameScreen);
            case CHAPTER: return new ChapterInteraction(gameScreen);
        }

        throw new GdxRuntimeException("No such interaction " + type);
    }
}
