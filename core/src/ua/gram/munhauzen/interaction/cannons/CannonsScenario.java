package ua.gram.munhauzen.interaction.cannons;

import com.badlogic.gdx.utils.Array;

import ua.gram.munhauzen.entity.Decision;
import ua.gram.munhauzen.entity.ScenarioTranslation;
import ua.gram.munhauzen.entity.StoryAudio;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class CannonsScenario {

    public String name;
    public Array<StoryAudio> audio;
    public Array<CannonsStoryImage> images;
    public Array<Decision> decisions;
    public Array<ScenarioTranslation> translations;
    public boolean isBegin;
    public boolean isExit;
    public String action;

    public CannonsStoryImage firstImage() {
        if (images.size == 0) return null;
        return images.get(0);
    }

    public StoryAudio firstAudio() {
        if (audio.size == 0) return null;
        return audio.get(0);
    }

    public CannonsStoryImage lastImage() {
        if (images.size == 0) return null;
        return images.get(images.size - 1);
    }

    public StoryAudio lastAudio() {
        if (audio.size == 0) return null;
        return audio.get(audio.size - 1);
    }

}