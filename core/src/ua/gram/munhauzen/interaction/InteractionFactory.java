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
    public final static String TIMER_2 = "TIMER_2";
    public final static String SLAP = "SLAP";
    public final static String PICTURE = "PICTURE";
    public final static String HORN = "HORN";
    public final static String DATE = "DATE";
    public final static String LIONS = "LIONS";
    public final static String SERVANTS = "SERVANTS";
    public final static String WAUWAU = "WAUWAU";
    public final static String CANNONS = "CANNONS";

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
            case SLAP:
                return new SlapInteraction(gameScreen);
            case PICTURE:
                return new PictureInteraction(gameScreen);
            case HORN:
                return new HornInteraction(gameScreen);
            case DATE:
                return new DateInteraction(gameScreen);
            case LIONS:
                return new LionsInteraction(gameScreen);
            case SERVANTS:
                return new ServantsInteraction(gameScreen);
            case WAUWAU:
                return new WauInteraction(gameScreen);
            case CANNONS:
                return new CannonsInteraction(gameScreen);
        }

        if (type.indexOf(TIMER_2) == 0) {
            String[] params = type.replace(TIMER_2, "").replace("(", "").replace(")", "")
                    .toLowerCase().split(",");

            if (params.length != 2) {
                throw new GdxRuntimeException("Invalid TIMER_2 params "+ type);
            }

            return new Timer2Interaction(gameScreen, params[0], Float.parseFloat(params[1]));
        }

        if (type.indexOf(TIMER) == 0) {
            String[] params = type.replace(TIMER, "").replace("(", "").replace(")", "")
                    .toLowerCase().split(",");

            if (params.length != 2) {
                throw new GdxRuntimeException("Invalid TIMER params " + type);
            }

            return new TimerInteraction(gameScreen, params[0], Float.parseFloat(params[1]));
        }

        if (type.indexOf(RANDOM) == 0) {
            String[] params = type.replace(RANDOM, "").replace("(", "").replace(")", "")
                    .toLowerCase().split(",");

            if (params.length != 2) {
                throw new GdxRuntimeException("Invalid RANDOM params "+ type);
            }

            return new RandomInteraction(gameScreen, params[0], params[1]);
        }

        throw new GdxRuntimeException("No such interaction " + type);
    }
}
