package ua.gram.munhauzen.interaction.picture;

import ua.gram.munhauzen.entity.StoryImage;
import ua.gram.munhauzen.interaction.PictureInteraction;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.service.InteractionImageService;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class PictureImageService extends InteractionImageService {

    final PictureInteraction interaction;

    public PictureImageService(GameScreen gameScreen, PictureInteraction interaction) {
        super(gameScreen);
        this.interaction = interaction;
    }

    public void displayImage(final StoryImage item) {

        Log.i(tag, "displayImage " + getResource(item));

        PictureStory story = interaction.storyManager.story;
        if (story != null) {
            for (PictureStoryScenario scenarioOption : story.scenarios) {
                for (StoryImage image : scenarioOption.scenario.images) {
                    image.isActive = false;
                }
            }
        }

        item.isActive = true;

        interaction.imageFragment.backgroundImage.setBackgroundDrawable(item.drawable);
    }
}
