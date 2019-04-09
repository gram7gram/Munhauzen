package ua.gram.munhauzen.entity;

import com.badlogic.gdx.utils.Array;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class Option extends Entity {

    public static final String BEGIN = "BEGIN";
    public static final String DEATH = "DEATH";
    public static final String VICTORY = "VICTORY";

    public Array<Image> images;
    public Array<Audio> audio;
    public Array<BackgroundAudio> backgroundAudio;
    public Array<Decision> decisions;
    public String type;
    public String interaction;
    public String text;
    public String inventoryAdd;
    public String action;

    public boolean isFinal() {
        return DEATH.equals(id) || VICTORY.equals(id);
    }
}