package ua.gram.munhauzen.interaction.picture;

import com.badlogic.gdx.utils.Array;

import ua.gram.munhauzen.entity.Decision;
import ua.gram.munhauzen.entity.StoryAudio;
import ua.gram.munhauzen.entity.StoryImage;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class PictureScenario {

    public String name;
    public Array<StoryAudio> audio;
    public Array<StoryImage> images;
    public Array<Decision> decisions;
    public boolean isBegin;
    public boolean isExit;
    public String action;

    public StoryImage firstImage() {
        if (images.size == 0) return null;
        return images.get(0);
    }

    public StoryAudio firstAudio() {
        if (audio.size == 0) return null;
        return audio.get(0);
    }

    public StoryImage lastImage() {
        if (images.size == 0) return null;
        return images.get(images.size - 1);
    }

    public StoryAudio lastAudio() {
        if (audio.size == 0) return null;
        return audio.get(audio.size - 1);
    }

}