package ua.gram.munhauzen.interaction.wauwau;

import ua.gram.munhauzen.entity.StoryImage;
import ua.gram.munhauzen.interaction.WauInteraction;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.service.InteractionImageService;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class WauImageService extends InteractionImageService {

    final WauInteraction interaction;

    public WauImageService(GameScreen gameScreen, WauInteraction interaction) {
        super(gameScreen);
        this.interaction = interaction;
    }

    @Override
    public void onPrepared(StoryImage item) {

        if (item.isActive) return;

        WauStory story = interaction.storyManager.story;
        if (story != null) {
            for (WauStoryScenario scenarioOption : story.scenarios) {
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
