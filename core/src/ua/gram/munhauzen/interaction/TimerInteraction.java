package ua.gram.munhauzen.interaction;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;

import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.interaction.timer.TimerScenario;
import ua.gram.munhauzen.interaction.timer.TimerStory;
import ua.gram.munhauzen.interaction.timer.TimerStoryManager;
import ua.gram.munhauzen.interaction.timer.fragment.TimerImageFragment;
import ua.gram.munhauzen.interaction.timer.fragment.TimerProgressBarFragment;
import ua.gram.munhauzen.interaction.timer.fragment.TimerScenarioFragment;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.service.DatabaseManager;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class TimerInteraction extends AbstractInteraction {

    public Array<TimerScenario> scenarioRegistry;
    public TimerStoryManager storyManager;
    public TimerProgressBarFragment progressBarFragment;
    public TimerImageFragment imageFragment;
    public TimerScenarioFragment scenarioFragment;
    boolean isLoaded;
    public final String burnAudio;
    public final float burnDuration;

    public TimerInteraction(GameScreen gameScreen, String burnAudio, float burnDuration) {
        super(gameScreen);

        this.burnAudio = burnAudio;
        this.burnDuration = burnDuration;

    }

    @Override
    public void start() {
        super.start();

        scenarioRegistry = new DatabaseManager().loadTimerScenario();
        storyManager = new TimerStoryManager(gameScreen, this);

        assetManager.load("hare/p18_d.jpg", Texture.class);
    }

    public void onResourcesLoaded() {
        isLoaded = true;

        progressBarFragment = new TimerProgressBarFragment(gameScreen, this);
        progressBarFragment.create();

        gameScreen.gameLayers.setProgressBarLayer(progressBarFragment);

        storyManager.resume();

        imageFragment = new TimerImageFragment(this);
        imageFragment.create();

        gameScreen.gameLayers.setInteractionLayer(imageFragment);
    }

    @Override
    public void update() {
        super.update();

        assetManager.update();

        if (!isLoaded) {
            if (assetManager.isFinished()) {
                onResourcesLoaded();
            }
            return;
        }

        TimerStory story = storyManager.hareStory;

        if (!story.isCompleted) {

            if (!GameState.isPaused) {
                storyManager.update(
                        story.progress + (Gdx.graphics.getDeltaTime() * 1000),
                        story.totalDuration
                );

                if (story.isCompleted) {

                    storyManager.onCompleted();

                } else {
                    storyManager.startLoadingResources();
                }
            }
        }

        if (progressBarFragment != null)
            progressBarFragment.update();

        if (imageFragment != null)
            imageFragment.update();

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

        isLoaded = false;
    }

    public void showProgressBar() {
        if (progressBarFragment != null) {
            progressBarFragment.fadeIn();
        }
    }
}
