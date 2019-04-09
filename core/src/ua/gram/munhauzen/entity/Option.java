package ua.gram.munhauzen.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class Option extends Entity {

    public static final String BEGIN = "BEGIN";
    public static final String DEATH = "DEATH";
    public static final String VICTORY = "VICTORY";

    public List<Image> images;
    public List<Audio> audio;
    public List<BackgroundAudio> backgroundAudio;
    public List<Decision> decisions;
    public String type;
    public String interaction;
    public String text;
    public String inventory_add;
    public String action;

    public Option() {
        images = new ArrayList<>(2);
        audio = new ArrayList<>(2);
        backgroundAudio = new ArrayList<>(2);
        decisions = new ArrayList<>(2);
    }

    public boolean isFinal() {
        return DEATH.equals(id) || VICTORY.equals(id);
    }
}