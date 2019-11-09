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

            StoryInteraction interaction = story.currentInteraction;

            if (interaction != null) {

                if (interaction.isLocked) {
                    if (interaction.interaction != null)
                        interaction.interaction.update();
                }
            }

        } catch (Throwable e) {
            Log.e(tag, e);

            gameScreen.onCriticalError(e);
        }
    }

    public void create(String name) {
        try {

            Log.i(tag, "create " + name);

            Story story = gameScreen.getStory();
            if (story == null) return;

            StoryInteraction interaction = story.currentInteraction;

            if (interaction != null) {
                destroy();
            }

            interaction = new StoryInteraction();
            interaction.name = name;

            interaction.interaction = InteractionFactory.create(gameScreen, interaction.name);
            interaction.isLocked = true;

            story.currentInteraction = interaction;

            story.progress = story.currentScenario.finishesAt;

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

            //Trigger next interaction without delay
            if (newStory.isInteraction()) {
                newStory.progress = newStory.totalDuration;
            }

        } catch (Throwable e) {
            Log.e(tag, e);

            gameScreen.onCriticalError(e);
        }
    }

    public void complete() {
        Story story = gameScreen.getStory();
        if (story == null) return;

        StoryInteraction interaction = story.currentInteraction;
        if (interaction == null) return;

        Log.i(tag, "complete " + interaction.name);

//        interaction.isCompleted = true;

        destroy();
    }

    public void destroy() {
        Story story = gameScreen.getStory();
        if (story == null) return;

        Log.i(tag, "destroy");

        try {
            StoryInteraction interaction = story.currentInteraction;
            if (interaction != null) {

                if (interaction.interaction != null) {
                    interaction.interaction.dispose();
                    interaction.interaction = null;
                }

                interaction.isLocked = false;

                story.currentInteraction = null;
            }

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
        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }
}
