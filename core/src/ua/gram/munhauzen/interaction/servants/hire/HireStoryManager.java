package ua.gram.munhauzen.interaction.servants.hire;

import com.badlogic.gdx.utils.GdxRuntimeException;

import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.entity.ServantsState;
import ua.gram.munhauzen.entity.StoryAudio;
import ua.gram.munhauzen.entity.StoryImage;
import ua.gram.munhauzen.interaction.ServantsInteraction;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.utils.Log;

public class HireStoryManager {

    private final String tag = getClass().getSimpleName();
    private final GameScreen gameScreen;
    private final ServantsInteraction interaction;

    public HireStory story;

    public HireStoryManager(GameScreen gameScreen, ServantsInteraction interaction) {
        this.gameScreen = gameScreen;
        this.interaction = interaction;
    }

    public HireStory create(String begin) {

        try {
            reset();

            HireStory story = null;

            for (HireScenario scenario : interaction.hireScenarioRegistry) {
                if (scenario.name.equals(begin)) {

                    story = new HireStory();
                    story.id = scenario.name;

                    HireStoryScenario storyScenario = new HireStoryScenario(gameScreen.game.gameState);
                    storyScenario.scenario = scenario;

                    story.scenarios.add(storyScenario);

                    break;
                }
            }

            if (story == null) {
                throw new GdxRuntimeException("No story found for " + begin);
            }

            story.init();

            return story;

        } catch (Throwable e) {
            Log.e(tag, e);

            gameScreen.onCriticalError(e);

            return null;
        }
    }

    public void update(float progress, int duration) {
        story.update(progress, duration);
    }

    public void startLoadingAudio() {

        if (story == null) return;

        HireStoryScenario scenario = story.currentScenario;
        if (scenario == null) return;

        try {

            StoryAudio audio = scenario.currentAudio;
            if (audio != null) {
                gameScreen.audioService.prepareAndPlay(audio);
            }
        } catch (Throwable e) {
            Log.e(tag, e);

            interaction.gameScreen.onCriticalError(e);
        }
    }

    public void startLoadingImages() {

        if (story == null) return;

        HireStoryScenario scenario = story.currentScenario;
        if (scenario == null) return;

        try {
            StoryImage image = scenario.currentImage;
            if (image != null) {
                interaction.imageService.prepareAndDisplay(image);
            }
        } catch (Throwable e) {
            Log.e(tag, e);

            interaction.gameScreen.onCriticalError(e);
        }

    }

    public void startLoadingResources() {

        if (interaction.hireFragment != null) {
            String servant = interaction.hireFragment.hireDialog.servantName;
            if (servant != null) {

                startLoadingImages();

                if (!interaction.hireFragment.hasServant(servant)) {
                    startLoadingAudio();
                }
            }
        }
    }

    public void onCompleted() {
        try {
            startLoadingImages();

            Log.i(tag, "onCompleted " + story.id);

            GameState gameState = gameScreen.game.gameState;

            gameState.addVisitedScenario(story.id);

            for (HireStoryScenario storyScenario : story.scenarios) {
                gameState.addVisitedScenario(storyScenario.scenario.name);
            }

            ServantsState state = interaction.gameScreen.getActiveSave().servantsInteractionState;

            state.viewedServants.add(interaction.hireFragment.hireDialog.servantName);

            interaction.hireFragment.hireDialog.fadeIn();

            GameState.pause(tag);
        } catch (Throwable e) {
            Log.e(tag, e);

            interaction.gameScreen.onCriticalError(e);
        }
    }

    public void reset() {

        if (story == null) return;

        Log.i(tag, "reset " + story.id);

        for (HireStoryScenario storyScenario : story.scenarios) {
            storyScenario.reset();
        }

        story.reset();
    }
}