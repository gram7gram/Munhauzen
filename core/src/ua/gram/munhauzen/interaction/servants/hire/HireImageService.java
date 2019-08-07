package ua.gram.munhauzen.interaction.servants.hire;

import com.badlogic.gdx.utils.Timer;

import ua.gram.munhauzen.entity.StoryImage;
import ua.gram.munhauzen.interaction.ServantsInteraction;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.service.InteractionImageService;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class HireImageService extends InteractionImageService {

    final ServantsInteraction interaction;

    public HireImageService(GameScreen gameScreen, ServantsInteraction interaction) {
        super(gameScreen);
        this.interaction = interaction;
    }

    @Override
    public void prepareAndDisplay(StoryImage item) {

        HireStoryImage storyImage = (HireStoryImage) item;

        HireStoryScenario scenario = interaction.storyManager.story.currentScenario;

        storyImage.image = interaction.hireFragment.hasServant(scenario.scenario.name)
                ? storyImage.imageEnabled
                : storyImage.imageDisabled;

        super.prepareAndDisplay(storyImage);
    }

    @Override
    public void prepare(StoryImage item, Timer.Task onComplete) {

        HireStoryImage storyImage = (HireStoryImage) item;

        HireStoryScenario scenario = interaction.storyManager.story.currentScenario;

        storyImage.image = interaction.hireFragment.hasServant(scenario.scenario.name)
                ? storyImage.imageEnabled
                : storyImage.imageDisabled;

        super.prepare(storyImage, onComplete);
    }

    @Override
    public void displayImage(StoryImage item) {

        Log.i(tag, "displayImage " + item.resource);

        HireStory story = interaction.storyManager.story;
        if (story != null) {
            for (HireStoryScenario scenarioOption : story.scenarios) {
                for (StoryImage image : scenarioOption.scenario.images) {
                    image.isActive = false;
                }
            }
        }

        item.isActive = true;

        interaction.hireFragment.setBackground(item.drawable, item.resource);
    }
}
