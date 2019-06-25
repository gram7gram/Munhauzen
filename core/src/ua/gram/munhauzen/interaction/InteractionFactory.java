package ua.gram.munhauzen.interaction;

import com.badlogic.gdx.utils.GdxRuntimeException;

import ua.gram.munhauzen.screen.GameScreen;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class InteractionFactory {

    public final static String HARE = "HARE";
    public final static String CONTINUE = "CONTINUE";
    public final static String GENERAL = "GENERAL";
    public final static String PUZZLE = "PUZZLE";
    public final static String CHAPTER = "CHAPTER";
    public final static String STICK = "STICK";
    public final static String BALLOONS = "BALLOONS";

    public static AbstractInteraction create(GameScreen gameScreen, String type) {

        switch (type) {
            case HARE:
                return new HareInteraction(gameScreen);
            case CONTINUE:
                return new ContinueInteraction(gameScreen);
            case GENERAL:
                return new GeneralsInteraction(gameScreen);
            case PUZZLE:
                return new PuzzleInteraction(gameScreen);
            case CHAPTER:
                return new ChapterInteraction(gameScreen);
            case BALLOONS:
                return new BalloonsInteraction(gameScreen);
        }

        if (type.indexOf(STICK) == 0) {
            String[] scenarios = type.replace(STICK, "").replace("(", "").replace(")", "")
                    .toLowerCase().split(",");

            return new StickInteraction(gameScreen, scenarios[0], scenarios[1]);
        }

        throw new GdxRuntimeException("No such interaction " + type);
    }
}
