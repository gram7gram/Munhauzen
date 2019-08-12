package ua.gram.munhauzen.interaction.timer;

import ua.gram.munhauzen.entity.StoryImage;
import ua.gram.munhauzen.interaction.TimerInteraction;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.service.InteractionImageService;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class TimerImageService extends InteractionImageService {

    final TimerInteraction interaction;

    public TimerImageService(GameScreen gameScreen, TimerInteraction interaction) {
        super(gameScreen);
        this.interaction = interaction;
    }

    public void displayImage(StoryImage item) {

        Log.i(tag, "displayImage " + getResource(item));

        TimerStory story = interaction.storyManager.story;
        if (story != null) {
            for (TimerStoryScenario scenarioOption : story.scenarios) {
                for (StoryImage image : scenarioOption.scenario.images) {
                    image.isActive = false;
                }
            }
        }

        item.isActive = true;

        interaction.imageFragment.backgroundImage.setBackgroundDrawable(item.drawable);
    }
}
