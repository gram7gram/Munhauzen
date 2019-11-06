package ua.gram.munhauzen.interaction.generals;

import ua.gram.munhauzen.entity.StoryImage;
import ua.gram.munhauzen.interaction.GeneralsInteraction;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.service.InteractionImageService;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class GeneralsImageService extends InteractionImageService {

    final GeneralsInteraction interaction;

    public GeneralsImageService(GameScreen gameScreen, GeneralsInteraction interaction) {
        super(gameScreen);
        this.interaction = interaction;
    }

    @Override
    public void onPrepared(StoryImage item) {

        if (item.isActive) return;

        GeneralsStory story = interaction.storyManager.story;
        if (story != null) {
            for (GeneralsStoryScenario scenarioOption : story.scenarios) {
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
