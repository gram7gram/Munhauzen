package ua.gram.munhauzen.interaction;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;

import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.interaction.generals.GeneralsImageService;
import ua.gram.munhauzen.interaction.generals.GeneralsScenario;
import ua.gram.munhauzen.interaction.generals.GeneralsStory;
import ua.gram.munhauzen.interaction.generals.GeneralsStoryManager;
import ua.gram.munhauzen.interaction.generals.fragment.GeneralsImageFragment;
import ua.gram.munhauzen.interaction.generals.fragment.GeneralsProgressBarFragment;
import ua.gram.munhauzen.interaction.generals.fragment.GeneralsScenarioFragment;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class GeneralsInteraction extends AbstractInteraction {

    public ArrayList<GeneralsScenario> scenarioRegistry;
    public GeneralsStoryManager storyManager;
    public GeneralsProgressBarFragment progressBarFragment;
    public GeneralsScenarioFragment scenarioFragment;
    boolean isLoaded;
    public GeneralsImageService imageService;
    public GeneralsImageFragment imageFragment;

    public GeneralsInteraction(GameScreen gameScreen) {
        super(gameScreen);
    }

    @Override
    public boolean isValid() {
        return storyManager != null && storyManager.story != null;
    }

    @Override
    public void start() {
        super.start();

        gameScreen.hideProgressBar();

        scenarioRegistry = gameScreen.game.databaseManager.loadGeneralsScenario();

        storyManager = new GeneralsStoryManager(gameScreen, this);
        imageService = new GeneralsImageService(gameScreen, this);

        assetManager.load("generals/an_general_1_sheet_3x1.png", Texture.class);
        assetManager.load("generals/an_general_2_sheet_3x1.png", Texture.class);
        assetManager.load("generals/an_general_3_sheet_3x1.png", Texture.class);
        assetManager.load("images/p33_d.jpg", Texture.class);
    }

    public void onResourcesLoaded() {

        isLoaded = true;

        progressBarFragment = new GeneralsProgressBarFragment(gameScreen, this);
        progressBarFragment.create();

        gameScreen.gameLayers.setInteractionProgressBarLayer(progressBarFragment);

        imageFragment = new GeneralsImageFragment(this);
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

        GeneralsStory story = storyManager.story;

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
