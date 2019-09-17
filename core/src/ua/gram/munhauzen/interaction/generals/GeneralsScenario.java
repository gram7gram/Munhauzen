package ua.gram.munhauzen.interaction.generals;

import java.util.ArrayList;

import ua.gram.munhauzen.entity.Decision;
import ua.gram.munhauzen.entity.StoryAudio;
import ua.gram.munhauzen.entity.Translation;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class GeneralsScenario {

    public String name;
    public ArrayList<StoryAudio> audio;
    public ArrayList<GeneralsStoryImage> images;
    public ArrayList<Decision> decisions;
    public ArrayList<Translation> translations;
    public boolean isBegin;
    public boolean isExit;
    public String action;

    public GeneralsStoryImage firstImage() {
        if (images.size() == 0) return null;
        return images.get(0);
    }

    public StoryAudio firstAudio() {
        if (audio.size() == 0) return null;
        return audio.get(0);
    }

    public GeneralsStoryImage lastImage() {
        if (images.size() == 0) return null;
        return images.get(images.size() - 1);
    }

    public StoryAudio lastAudio() {
        if (audio.size() == 0) return null;
        return audio.get(audio.size() - 1);
    }

}