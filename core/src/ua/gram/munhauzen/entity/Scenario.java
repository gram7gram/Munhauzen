package ua.gram.munhauzen.entity;

import com.badlogic.gdx.utils.Array;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class Scenario {

    public static final String DEATH = "DEATH";
    public static final String VICTORY = "VICTORY";
    public static final String FADE_IN = "FADE";

    public Array<StoryImage> images;
    public Array<StoryAudio> audio;
    public Array<Decision> decisions;
    public Array<ScenarioTranslation> translations;
    public boolean isBegin;
    public String name;
    public String chapter;
    public String type;
    public String interaction;
    public String action;
    public String source;

    public StoryImage lastImage() {
        return images.get(images.size - 1);
    }

    public StoryAudio lastAudio() {
        return audio.get(audio.size - 1);
    }

    public boolean isVictory() {
        return VICTORY.equals(name);
    }

    public boolean isDefeat() {
        return DEATH.equals(name);
    }
}