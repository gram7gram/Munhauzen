package ua.gram.munhauzen.interaction.timer;

import ua.gram.munhauzen.entity.StoryImage;
import ua.gram.munhauzen.interaction.TimerInteraction;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.service.InteractionImageService;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class TimerImageService extends InteractionImageService {

    final TimerInteraction interaction;

    public TimerImageService(GameScreen gameScreen, TimerInteraction interaction) {
        super(gameScreen);
        this.interaction = interaction;
    }

    @Override
    public void onPrepared(StoryImage item) {

        if (item.isActive) return;

        TimerStory story = interaction.storyManager.story;
        if (story != null) {
            for (TimerStoryScenario scenarioOption : story.scenarios) {
                for (StoryImage image : scenarioOption.scenario.images) {
                    image.isActive = false;
                }
            }
        }

        saveCurrentBackground(item);

        displayImage(item);
    }

    @Override
    protected void displayImage(StoryImage item) {

        gameScreen.hideImageFragment();

        item.isActive = true;

        interaction.imageFragment.backgroundImage.setBackgroundDrawable(item.drawable);
    }
}
