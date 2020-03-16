package ua.gram.munhauzen.interaction;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;

import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.interaction.timer.TimerImageService;
import ua.gram.munhauzen.interaction.timer.TimerScenario;
import ua.gram.munhauzen.interaction.timer.TimerStory;
import ua.gram.munhauzen.interaction.timer.TimerStoryManager;
import ua.gram.munhauzen.interaction.timer.fragment.TimerImageFragment;
import ua.gram.munhauzen.interaction.timer.fragment.TimerProgressBarFragment;
import ua.gram.munhauzen.interaction.timer.fragment.TimerScenarioFragment;
import ua.gram.munhauzen.screen.GameScreen;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class TimerInteraction extends AbstractInteraction {

    public ArrayList<TimerScenario> scenarioRegistry;
    public TimerStoryManager storyManager;
    public TimerProgressBarFragment progressBarFragment;
    public TimerImageFragment imageFragment;
    public TimerScenarioFragment scenarioFragment;
    public TimerImageService imageService;
    boolean isLoaded;
    public final String burnScenario;
    public final float burnDurationInSeconds;
    public boolean isBombCanceled;

    public TimerInteraction(GameScreen gameScreen, String burnScenario, float burnDuration) {
        super(gameScreen);

        this.burnScenario = burnScenario;
        this.burnDurationInSeconds = burnDuration;

    }

    @Override
    public boolean isValid() {
        return storyManager != null && storyManager.story != null;
    }

    @Override
    public void start() {
        super.start();

        isBombCanceled = false;

        gameScreen.hideProgressBar();

        imageService = new TimerImageService(gameScreen, this);

        scenarioRegistry = gameScreen.game.databaseManager.loadTimerScenario();
        storyManager = new TimerStoryManager(gameScreen, this);

        assetManager.load("timer/an_bam_sheet_1x7.png", Texture.class);
        assetManager.load("timer/an_timer_sheet_1x8.png", Texture.class);
        assetManager.load("timer/inter_bomb.png", Texture.class);

        assetManager.load("ui/playbar_pause.png", Texture.class);
        assetManager.load("ui/playbar_play.png", Texture.class);

        assetManager.load("ui/playbar_rewind_backward.png", Texture.class);
        assetManager.load("ui/playbar_rewind_forward.png", Texture.class);

        assetManager.load("ui/elements_player_fond_1.png", Texture.class);
        assetManager.load("ui/elements_player_fond_2.png", Texture.class);
        assetManager.load("ui/elements_player_fond_3.png", Texture.class);
        assetManager.load("ui/player_progress_bar_progress.9.jpg", Texture.class);
        assetManager.load("ui/player_progress_bar_knob.png", Texture.class);
    }

    public void onResourcesLoaded() {

        isLoaded = true;

        progressBarFragment = new TimerProgressBarFragment(gameScreen, this);
        progressBarFragment.create();

        gameScreen.gameLayers.setInteractionProgressBarLayer(progressBarFragment);

        imageFragment = new TimerImageFragment(this);
        imageFragment.create();

        gameScreen.gameLayers.setInteractionLayer(imageFragment);

        imageFragment.fadeInRoot();

        storyManager.resume();
    }

    @Override
    public void update() {
        super.update();

        if (assetManager == null) return;

        gameScreen.hideProgressBar();

        if (!isLoaded) {
            if (assetManager.isFinished()) {
                onResourcesLoaded();
            }
            return;
        }

        if (imageService != null) {
            imageService.update();
        }

        TimerStory story = storyManager.story;

        if (!story.isCompleted) {

            if (!GameState.isPaused) {
                storyManager.update(
                        story.progress + (Gdx.graphics.getDeltaTime() * 1000),
                        story.totalDuration
                );
            }

            storyManager.startLoadingImages();

            if (!GameState.isPaused) {

                if (story.isCompleted) {

                    storyManager.onCompleted();

                } else {
                    storyManager.startLoadingAudio();
                }
            }
        }

        if (progressBarFragment != null)
            progressBarFragment.update();

        if (imageFragment != null)
            imageFragment.update();

        if (scenarioFragment != null)
            scenarioFragment.update();

    }

    @Override
    public void dispose() {
        super.dispose();

        if (progressBarFragment != null) {
            progressBarFragment.dispose();
            progressBarFragment = null;
        }

        if (scenarioFragment != null) {
            scenarioFragment.dispose();
            scenarioFragment = null;
        }

        if (imageFragment != null) {
            imageFragment.dispose();
            imageFragment = null;
        }

        if (storyManager != null) {
            gameScreen.audioService.dispose(storyManager.story);
            storyManager = null;
        }

        isLoaded = false;
    }

    public void showProgressBar() {
        if (progressBarFragment != null) {
            progressBarFragment.fadeIn();
        }
    }

    public void hideProgressBar() {
        if (progressBarFragment != null) {
            progressBarFragment.fadeOut();
        }
    }
}
