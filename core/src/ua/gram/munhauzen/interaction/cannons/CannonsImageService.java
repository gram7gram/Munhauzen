package ua.gram.munhauzen.interaction.cannons;

import ua.gram.munhauzen.entity.StoryImage;
import ua.gram.munhauzen.interaction.CannonsInteraction;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.service.InteractionImageService;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class CannonsImageService extends InteractionImageService {

    final CannonsInteraction interaction;

    public CannonsImageService(GameScreen gameScreen, CannonsInteraction interaction) {
        super(gameScreen);
        this.interaction = interaction;
    }

    @Override
    public void onPrepared(StoryImage item) {

        if (item.isActive) return;

        CannonsStory story = interaction.storyManager.story;
        if (story != null) {
            for (CannonsStoryScenario scenarioOption : story.scenarios) {
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
