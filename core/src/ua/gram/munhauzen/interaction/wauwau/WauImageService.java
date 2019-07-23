package ua.gram.munhauzen.interaction.wauwau;

import ua.gram.munhauzen.entity.StoryImage;
import ua.gram.munhauzen.interaction.WauInteraction;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.service.InteractionImageService;
import ua.gram.munhauzen.utils.Log;

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

        gameScreen.hideImageFragment();

        WauStory story = interaction.storyManager.story;
        if (story != null) {
            for (WauStoryScenario scenarioOption : story.scenarios) {
                for (StoryImage image : scenarioOption.scenario.images) {
                    image.isActive = false;
                }
            }
        }

        super.onPrepared(item);
    }

    public void displayImage(final StoryImage item) {

        Log.i(tag, "displayImage " + getResource(item));

        item.isActive = true;

        interaction.imageFragment.backgroundImage.setBackgroundDrawable(item.drawable);
    }
}
