package ua.gram.munhauzen.interaction.servants.hire;

import java.util.ArrayList;

import ua.gram.munhauzen.entity.StoryAudio;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class HireScenario {

    public String name;
    public ArrayList<HireStoryImage> images;
    public ArrayList<StoryAudio> audio;

    public HireStoryImage firstImage() {
        if (images.size() == 0) return null;
        return images.get(0);
    }

    public StoryAudio firstAudio() {
        if (audio.size() == 0) return null;
        return audio.get(0);
    }

    public HireStoryImage lastImage() {
        if (images.size() == 0) return null;
        return images.get(images.size() - 1);
    }

    public StoryAudio lastAudio() {
        if (audio.size() == 0) return null;
        return audio.get(audio.size() - 1);
    }

}