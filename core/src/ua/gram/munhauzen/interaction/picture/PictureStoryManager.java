package ua.gram.munhauzen.interaction.picture;

import com.badlogic.gdx.utils.Timer;

import ua.gram.munhauzen.entity.Decision;
import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.entity.StoryAudio;
import ua.gram.munhauzen.entity.StoryImage;
import ua.gram.munhauzen.interaction.PictureInteraction;
import ua.gram.munhauzen.interaction.picture.fragment.PictureScenarioFragment;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.utils.Log;

public class PictureStoryManager {

    private final String tag = getClass().getSimpleName();
    private final GameScreen gameScreen;
    private final PictureInteraction interaction;

    public PictureStory story;

    public PictureStoryManager(GameScreen gameScreen, PictureInteraction interaction) {
        this.gameScreen = gameScreen;
        this.interaction = interaction;
    }

    public PictureStory create(String begin) {

        reset();

        PictureStory story = new PictureStory();
        story.id = begin;

        for (PictureScenario pictureScenario : interaction.scenarioRegistry) {
            if (pictureScenario.name.equals(begin)) {
                findNext(pictureScenario, story);
                break;
            }
        }

        story.init();

        return story;
    }

    public void resume() {

        if (story == null) {
            Log.e(tag, "Story is not valid. Resetting");

            for (PictureScenario pictureScenario : interaction.scenarioRegistry) {
                if (pictureScenario.isBegin) {
                    story = create(pictureScenario.name);
                    break;
                }
            }
        }

        Log.i(tag, "resume " + story.id);

    }

    private void findNext(PictureScenario from, PictureStory pictureStory) {

        Log.i(tag, "findNext " + from.name + " #" + pictureStory.scenarios.size());

        PictureStoryScenario storyScenario = new PictureStoryScenario(gameScreen.game.gameState);
        storyScenario.scenario = from;
        storyScenario.duration = 0;

        pictureStory.scenarios.add(storyScenario);

        if ("GOTO".equals(from.action)) {
            for (Decision decision : from.decisions) {
                for (PictureScenario pictureScenario : interaction.scenarioRegistry) {
                    if (pictureScenario.name.equals(decision.scenario)) {

                        findNext(pictureScenario, pictureStory);

                        break;
                    }
                }
            }
        }
    }

    public void update(float progress, int duration) {
        story.update(progress, duration);
    }

    public void startLoadingAudio() {
        try {

            if (story == null) return;

            PictureStoryScenario scenario = story.currentScenario;
            if (scenario == null) return;

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

    public void startLoadingImage() {
        try {

            displayCurrentImage();

            if (story == null) return;

            PictureStoryScenario scenario = story.currentScenario;
            if (scenario == null) return;

            final StoryImage image = scenario.currentImage;
            if (image != null) {
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

    public void displayCurrentImage() {
        if (story == null) return;

        try {

            StoryImage image = story.currentScenario.currentImage;
            if (image != null) {
                if (image.isLocked)
                    interaction.imageService.prepareAndDisplay(image);
            }

        } catch (Throwable e) {
            Log.e(tag, e);

            gameScreen.onCriticalError(e);
        }
    }

    public void startLoadingResources() {
        startLoadingAudio();

        startLoadingImage();
    }

    public void onCompleted() {
        try {
            displayCurrentImage();

            Log.i(tag, "onCompleted " + story.id);

            GameState gameState = gameScreen.game.gameState;

            gameState.addVisitedScenario(story.id);

            for (PictureStoryScenario storyScenario : story.scenarios) {
                gameState.addVisitedScenario(storyScenario.scenario.name);
            }

            for (StoryAudio audio : story.currentScenario.scenario.audio) {
                if (audio.player != null) {
                    audio.player.pause();
                }
            }

            if (story.currentScenario.scenario.isExit) {

                Log.i(tag, "Exit reached");

                if (interaction.progressBarFragment != null) {
                    interaction.progressBarFragment.destroy();
                    interaction.progressBarFragment = null;
                }

                if (interaction.scenarioFragment != null) {
                    interaction.scenarioFragment.destroy();
                    interaction.scenarioFragment = null;
                }

                complete();

                return;
            }

            if (interaction.scenarioFragment == null) {
                interaction.scenarioFragment = new PictureScenarioFragment(interaction);
            }

            interaction.progressBarFragment.fadeIn();

            interaction.scenarioFragment.create();

            gameScreen.gameLayers.setStoryDecisionsLayer(
                    interaction.scenarioFragment
            );

            interaction.scenarioFragment.fadeIn();
        } catch (Throwable e) {
            Log.e(tag, e);

            interaction.gameScreen.onCriticalError(e);
        }
    }

    public void complete() {

        Log.i(tag, "complete");

        try {
            gameScreen.interactionService.complete();

            gameScreen.interactionService.findStoryAfterInteraction();

            interaction.gameScreen.restoreProgressBarIfDestroyed();
        } catch (Throwable e) {
            Log.e(tag, e);

            interaction.gameScreen.onCriticalError(e);
        }
    }

    public void reset() {

        if (story == null) return;

        Log.i(tag, "reset " + story.id);

        for (PictureStoryScenario storyScenario : story.scenarios) {
            storyScenario.reset();
        }

        story.reset();
    }

}