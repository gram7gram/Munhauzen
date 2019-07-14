package ua.gram.munhauzen.interaction.picture;

import com.badlogic.gdx.utils.Timer;

import ua.gram.munhauzen.entity.Decision;
import ua.gram.munhauzen.entity.StoryAudio;
import ua.gram.munhauzen.entity.StoryImage;
import ua.gram.munhauzen.interaction.PictureInteraction;
import ua.gram.munhauzen.interaction.picture.fragment.PictureScenarioFragment;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.utils.Log;
import ua.gram.munhauzen.utils.StringUtils;

public class PictureStoryManager {

    private final String tag = getClass().getSimpleName();
    private final GameScreen gameScreen;
    private final PictureInteraction interaction;

    public PictureStory pictureStory;

    public PictureStoryManager(GameScreen gameScreen, PictureInteraction interaction) {
        this.gameScreen = gameScreen;
        this.interaction = interaction;
    }

    public PictureStory create(String begin) {

        reset();

        PictureStory story = new PictureStory();
        story.id = StringUtils.cid();

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

        if (pictureStory == null) {
            Log.e(tag, "Story is not valid. Resetting");

            for (PictureScenario pictureScenario : interaction.scenarioRegistry) {
                if (pictureScenario.isBegin) {
                    pictureStory = create(pictureScenario.name);
                    break;
                }
            }
        }

        Log.i(tag, "resume " + pictureStory.id);

    }

    private void findNext(PictureScenario from, PictureStory pictureStory) {

        Log.i(tag, "findNext " + from.name + " #" + pictureStory.scenarios.size);

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
        pictureStory.update(progress, duration);
    }

    public void startLoadingAudio() {
        try {
            PictureStory story = interaction.storyManager.pictureStory;

            PictureStoryScenario scenario = story.currentScenario;
            if (scenario == null) return;

            final StoryAudio audio = scenario.currentAudio;
            if (audio != null) {
                gameScreen.audioService.prepare(audio, new Timer.Task() {
                    @Override
                    public void run() {
                        try {
                            gameScreen.audioService.onPrepared(audio);
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
            PictureStory story = interaction.storyManager.pictureStory;

            PictureStoryScenario scenario = story.currentScenario;
            if (scenario == null) return;

            final StoryImage image = scenario.currentImage;
            if (image != null) {
                interaction.imageService.prepare(image, new Timer.Task() {
                    @Override
                    public void run() {
                        try {
                            interaction.imageService.onPrepared(image);
                        } catch (Throwable e) {
                            Log.e(tag, e);
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
        startLoadingAudio();

        startLoadingImage();
    }

    public void onCompleted() {

        startLoadingImage();

        Log.i(tag, "onCompleted " + pictureStory.id);

        for (StoryAudio audio : pictureStory.currentScenario.scenario.audio) {
            if (audio.player != null) {
                audio.player.pause();
            }
        }

        if (pictureStory.currentScenario.scenario.isExit) {

            Log.i(tag, "Exit reached");

            if (interaction.progressBarFragment.canFadeOut()) {
                interaction.progressBarFragment.fadeOut(new Runnable() {
                    @Override
                    public void run() {
                        complete();
                    }
                });
            } else {
                complete();
            }

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

        if (pictureStory == null) return;

        Log.i(tag, "reset " + pictureStory.id);

        for (PictureStoryScenario storyScenario : pictureStory.scenarios) {
            storyScenario.reset();
        }

        pictureStory.reset();
    }

}