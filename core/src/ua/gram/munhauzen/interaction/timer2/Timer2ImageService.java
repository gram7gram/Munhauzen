package ua.gram.munhauzen.interaction.timer2;

import ua.gram.munhauzen.entity.StoryImage;
import ua.gram.munhauzen.interaction.Timer2Interaction;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.service.InteractionImageService;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class Timer2ImageService extends InteractionImageService {

    final Timer2Interaction interaction;

    public Timer2ImageService(GameScreen gameScreen, Timer2Interaction interaction) {
        super(gameScreen);
        this.interaction = interaction;
    }

    @Override
    public void onPrepared(StoryImage item) {

        if (item.isActive) return;

        Timer2Story story = interaction.storyManager.story;
        if (story != null) {
            for (Timer2StoryScenario scenarioOption : story.scenarios) {
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
