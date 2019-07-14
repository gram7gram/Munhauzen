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

    public StoryImage lastImage() {
        return images.get(images.size - 1);
    }

    public StoryAudio lastAudio() {
        return audio.get(audio.size - 1);
    }

}