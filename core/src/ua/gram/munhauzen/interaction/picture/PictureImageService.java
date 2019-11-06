package ua.gram.munhauzen.interaction.picture;

import ua.gram.munhauzen.entity.StoryImage;
import ua.gram.munhauzen.interaction.PictureInteraction;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.service.InteractionImageService;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class PictureImageService extends InteractionImageService {

    final PictureInteraction interaction;

    public PictureImageService(GameScreen gameScreen, PictureInteraction interaction) {
        super(gameScreen);
        this.interaction = interaction;
    }

    @Override
    public void onPrepared(StoryImage item) {

        if (item.isActive) return;

        PictureStory story = interaction.storyManager.story;
        if (story != null) {
            for (PictureStoryScenario scenarioOption : story.scenarios) {
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
