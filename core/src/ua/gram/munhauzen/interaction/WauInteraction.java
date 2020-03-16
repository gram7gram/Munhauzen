package ua.gram.munhauzen.interaction;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;

import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.interaction.wauwau.WauImageService;
import ua.gram.munhauzen.interaction.wauwau.WauScenario;
import ua.gram.munhauzen.interaction.wauwau.WauStory;
import ua.gram.munhauzen.interaction.wauwau.WauStoryManager;
import ua.gram.munhauzen.interaction.wauwau.fragment.WauImageFragment;
import ua.gram.munhauzen.interaction.wauwau.fragment.WauProgressBarFragment;
import ua.gram.munhauzen.interaction.wauwau.fragment.WauScenarioFragment;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class WauInteraction extends AbstractInteraction {

    public ArrayList<WauScenario> scenarioRegistry;
    public WauStoryManager storyManager;
    public WauProgressBarFragment progressBarFragment;
    public WauScenarioFragment scenarioFragment;
    boolean isLoaded;
    public WauImageService imageService;
    public WauImageFragment imageFragment;
    public int wauCounter, maxWauCounter = 5;

    public WauInteraction(GameScreen gameScreen) {
        super(gameScreen);
    }

    @Override
    public boolean isValid() {
        return storyManager != null && storyManager.story != null;
    }

    @Override
    public void start() {
        super.start();

        wauCounter = 0;

        gameScreen.hideProgressBar();

        scenarioRegistry = gameScreen.game.databaseManager.loadWauwauScenario();

        storyManager = new WauStoryManager(gameScreen, this);
        imageService = new WauImageService(gameScreen, this);

        assetManager.load("wau/wau_sheet_1x4.png", Texture.class);
    }

    public void onResourcesLoaded() {

        isLoaded = true;

        progressBarFragment = new WauProgressBarFragment(gameScreen, this);
        progressBarFragment.create();

        gameScreen.gameLayers.setInteractionProgressBarLayer(progressBarFragment);

        imageFragment = new WauImageFragment(this);
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

        WauStory story = storyManager.story;

        if (story != null && !story.isCompleted) {

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


        if (imageFragment != null) {
            imageFragment.update();
        }

        if (progressBarFragment != null)
            progressBarFragment.update();

    }

    @Override
    public void dispose() {
        super.dispose();

        try {

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

            if (imageService != null) {
                imageService.dispose();
                imageService = null;
            }

            if (storyManager != null) {
                gameScreen.audioService.dispose(storyManager.story);
                storyManager = null;
            }

            isLoaded = false;

        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }

    public void showProgressBar() {
        if (progressBarFragment != null) {
            progressBarFragment.fadeIn();
        }
    }
}
