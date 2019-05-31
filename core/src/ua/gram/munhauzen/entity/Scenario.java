package ua.gram.munhauzen.entity;

import com.badlogic.gdx.utils.Array;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class Scenario {

    public static final String BEGIN = "a1_0";
    public static final String DEATH = "DEATH";
    public static final String VICTORY = "VICTORY";
    public static final String FADE_IN = "FADE_IN";

    public Array<StoryImage> images;
    public Array<StoryAudio> audio;
    public Array<Decision> decisions;
    public Array<ScenarioTranslation> translations;
    public String name;
    public String chapter;
    public String type;
    public String interaction;
    public String text;
    public String inventoryAdd;
    public String inventoryGlobalAdd;
    public String action;


    public boolean isFinal() {
        return DEATH.equals(name) || VICTORY.equals(name);
    }
}