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
    public final static String RANDOM = "RANDOM";
    public final static String BALLOONS = "BALLOONS";
    public final static String SWAMP = "SWAMP";
    public final static String TIMER = "TIMER";

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
            case SWAMP:
                return new SwampInteraction(gameScreen);
        }

        if (type.indexOf(TIMER) == 0) {
            String[] params = type.replace(TIMER, "").replace("(", "").replace(")", "")
                    .toLowerCase().split(",");

            return new TimerInteraction(gameScreen, params[0], Float.parseFloat(params[1]));
        }

        if (type.indexOf(RANDOM) == 0) {
            String[] scenarios = type.replace(RANDOM, "").replace("(", "").replace(")", "")
                    .toLowerCase().split(",");

            return new RandomInteraction(gameScreen, scenarios[0], scenarios[1]);
        }

        throw new GdxRuntimeException("No such interaction " + type);
    }
}
