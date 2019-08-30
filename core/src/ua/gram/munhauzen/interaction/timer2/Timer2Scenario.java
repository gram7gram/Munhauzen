package ua.gram.munhauzen.interaction.timer2;

import java.util.ArrayList;

import ua.gram.munhauzen.entity.Decision;
import ua.gram.munhauzen.entity.ScenarioTranslation;
import ua.gram.munhauzen.entity.StoryAudio;
import ua.gram.munhauzen.entity.StoryImage;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class Timer2Scenario {

    public String name;
    public ArrayList<StoryImage> images;
    public ArrayList<StoryAudio> audio;
    public ArrayList<Decision> decisions;
    public ArrayList<ScenarioTranslation> translations;
    public boolean isBegin;
    public boolean isExit;
    public boolean isWin;
    public String action;

    public StoryImage firstImage() {
        if (images.size() == 0) return null;
        return images.get(0);
    }

    public StoryAudio firstAudio() {
        if (audio.size() == 0) return null;
        return audio.get(0);
    }

    public StoryImage lastImage() {
        if (images.size() == 0) return null;
        return images.get(images.size() - 1);
    }

    public StoryAudio lastAudio() {
        if (audio.size() == 0) return null;
        return audio.get(audio.size() - 1);
    }

}