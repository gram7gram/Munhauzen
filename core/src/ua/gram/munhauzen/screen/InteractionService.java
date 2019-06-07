package ua.gram.munhauzen.screen;

import ua.gram.munhauzen.entity.Story;
import ua.gram.munhauzen.entity.StoryInteraction;
import ua.gram.munhauzen.interaction.InteractionFactory;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class InteractionService {

    final String tag = getClass().getSimpleName();
    final GameScreen gameScreen;

    public InteractionService(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    public void update() {

        Story story = gameScreen.getStory();
        if (story.currentScenario == null) return;

        String name = InteractionFactory.HARE;//story.currentScenario.scenario.interaction;
        if (name == null) return;

        StoryInteraction interaction = story.currentInteraction;

        if (interaction != null) {

            if (interaction.isLocked) {
                interaction.interaction.update();
                return;
            } else {
                destroy();
            }
        }

        interaction = new StoryInteraction();
        interaction.name = name;

        interaction.interaction = InteractionFactory.create(gameScreen, interaction.name);
        interaction.isLocked = true;

        Log.i(tag, "create " + interaction.name);

        story.currentInteraction = interaction;

        interaction.interaction.start();
    }

    public void destroy() {
        Story story = gameScreen.getStory();
        if (story.currentScenario == null) return;

        StoryInteraction interaction = story.currentInteraction;
        if (interaction == null) return;

        Log.i(tag, "destroy " + interaction.name);

        interaction.interaction.dispose();
        interaction.isLocked = false;

        if (gameScreen.gameLayers.interactionLayer != null) {
            gameScreen.gameLayers.interactionLayer.destroy();
            gameScreen.gameLayers.interactionLayer = null;
        }
    }
}
