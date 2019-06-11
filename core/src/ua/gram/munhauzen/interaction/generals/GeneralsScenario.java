package ua.gram.munhauzen.interaction.generals;

import com.badlogic.gdx.utils.Array;

import ua.gram.munhauzen.entity.Decision;
import ua.gram.munhauzen.entity.ScenarioTranslation;
import ua.gram.munhauzen.entity.StoryAudio;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class GeneralsScenario {

    public String name;
    public Array<StoryAudio> audio;
    public Array<GeneralsStoryImage> images;
    public Array<Decision> decisions;
    public Array<ScenarioTranslation> translations;
    public boolean isBegin;
    public boolean isExit;
    public String action;

}