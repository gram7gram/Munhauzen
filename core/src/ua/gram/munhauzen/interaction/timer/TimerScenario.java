package ua.gram.munhauzen.interaction.timer;

import com.badlogic.gdx.utils.Array;

import ua.gram.munhauzen.entity.Decision;
import ua.gram.munhauzen.entity.ScenarioTranslation;
import ua.gram.munhauzen.entity.StoryAudio;
import ua.gram.munhauzen.entity.StoryImage;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class TimerScenario {

    public String name;
    public Array<StoryImage> images;
    public Array<StoryAudio> audio;
    public Array<Decision> decisions;
    public Array<ScenarioTranslation> translations;
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