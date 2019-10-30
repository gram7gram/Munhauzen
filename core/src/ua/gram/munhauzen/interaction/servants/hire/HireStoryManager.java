package ua.gram.munhauzen.interaction.servants.hire;

import com.badlogic.gdx.utils.Timer;

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

        reset();

        HireStory story = new HireStory();

        for (HireScenario scenario : interaction.hireScenarioRegistry) {
            if (scenario.name.equals(begin)) {

                story.id = scenario.name;

                HireStoryScenario storyScenario = new HireStoryScenario(gameScreen.game.gameState);
                storyScenario.scenario = scenario;

                story.scenarios.add(storyScenario);

                break;
            }
        }

        story.init();

        return story;
    }

    public void update(float progress, int duration) {
        story.update(progress, duration);
    }

    public void startLoadingAudio() {

        if (story == null) return;

        HireStoryScenario scenario = story.currentScenario;
        if (scenario == null) return;

        try {

            final StoryAudio audio = scenario.currentAudio;
            if (audio != null) {
                gameScreen.audioService.prepare(audio, new Timer.Task() {
                    @Override
                    public void run() {
                        try {
                            if (gameScreen.audioService != null)
                                gameScreen.audioService.playAudio(audio);
                        } catch (Throwable e) {
                            Log.e(tag, e);

                            interaction.gameScreen.onCriticalError(e);
                        }
                    }
                });

                if (audio.next != null) {
                    gameScreen.audioService.prepare(audio.next, new Timer.Task() {
                        @Override
                        public void run() {

                        }
                    });
                }
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
            final StoryImage image = scenario.currentImage;
            if (image != null) {
                interaction.imageService.prepare(image, new Timer.Task() {
                    @Override
                    public void run() {
                        try {
                            if (interaction.imageService != null)
                                interaction.imageService.onPrepared(image);
                        } catch (Throwable e) {
                            Log.e(tag, e);

                            interaction.gameScreen.onCriticalError(e);
                        }
                    }
                });

                if (image.next != null) {
                    interaction.imageService.prepare(image.next, new Timer.Task() {
                        @Override
                        public void run() {

                        }
                    });
                }
            }
        } catch (Throwable e) {
            Log.e(tag, e);

            interaction.gameScreen.onCriticalError(e);
        }

    }

    public void startLoadingResources() {

        if (interaction.hireFragment != null) {
            if (interaction.hireFragment.hireDialog.servantName != null) {

                startLoadingImages();

                if (!interaction.hireFragment.hasServant(interaction.hireFragment.hireDialog.servantName)) {
                    startLoadingAudio();
                }
            }
        }
    }

    public void onCompleted() {

        startLoadingImages();

        Log.i(tag, "onCompleted " + story.id);

        ServantsState state = interaction.gameScreen.getActiveSave().servantsInteractionState;

        state.viewedServants.add(interaction.hireFragment.hireDialog.servantName);

        interaction.hireFragment.hireDialog.fadeIn();

        GameState.pause(tag);
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