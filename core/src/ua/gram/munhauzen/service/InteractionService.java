package ua.gram.munhauzen.service;

import ua.gram.munhauzen.entity.Scenario;
import ua.gram.munhauzen.entity.Story;
import ua.gram.munhauzen.entity.StoryInteraction;
import ua.gram.munhauzen.interaction.InteractionFactory;
import ua.gram.munhauzen.screen.GameScreen;
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
        try {
            Story story = gameScreen.getStory();
            if (story == null) return;

            if (story.currentScenario == null) return;

            String name = InteractionFactory.BALLOONS;// story.currentScenario.scenario.interaction;
            if (name == null) return;

            StoryInteraction interaction = story.currentInteraction;

            if (interaction != null) {

                if (interaction.isCompleted) return;

                if (interaction.isLocked) {
                    interaction.interaction.update();
                    return;
                }

                destroy();
            }

            interaction = new StoryInteraction();
            interaction.name = name;

            interaction.interaction = InteractionFactory.create(gameScreen, interaction.name);
            interaction.isLocked = true;
            interaction.isCompleted = false;

            Log.i(tag, "create " + interaction.name);

            story.currentInteraction = interaction;

            story.progress = story.currentScenario.finishesAt;

            gameScreen.hideAndDestroyScenarioFragment();

            interaction.interaction.start();

        } catch (Throwable e) {
            Log.e(tag, e);

            gameScreen.onCriticalError(e);
        }
    }

    public void findStoryAfterInteraction() {
        try {
            Story story = gameScreen.getStory();
            if (story == null) return;
            if (story.currentScenario == null) return;

            Scenario nextAfterInteraction = gameScreen.storyManager.getNextScenarioFromDecisions(story.currentScenario.scenario);
            if (nextAfterInteraction == null) return;

            Story newStory = gameScreen.storyManager.create(nextAfterInteraction.name);

            gameScreen.setStory(newStory);

        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }

    public void complete() {
        Story story = gameScreen.getStory();
        if (story.currentScenario == null) return;

        StoryInteraction interaction = story.currentInteraction;
        if (interaction == null) return;

        Log.i(tag, "complete " + interaction.name);

        interaction.interaction.dispose();
        interaction.isCompleted = true;

        destroy();
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

        if (gameScreen.gameLayers.storyDecisionsLayer != null) {
            gameScreen.gameLayers.storyDecisionsLayer.destroy();
            gameScreen.gameLayers.storyDecisionsLayer = null;
        }

        if (gameScreen.gameLayers.interactionProgressBarLayer != null) {
            gameScreen.gameLayers.interactionProgressBarLayer.destroy();
            gameScreen.gameLayers.interactionProgressBarLayer = null;
        }
    }
}
