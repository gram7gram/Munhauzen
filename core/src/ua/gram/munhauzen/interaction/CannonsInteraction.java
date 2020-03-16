package ua.gram.munhauzen.interaction;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;

import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.interaction.cannons.CannonsImageService;
import ua.gram.munhauzen.interaction.cannons.CannonsScenario;
import ua.gram.munhauzen.interaction.cannons.CannonsStory;
import ua.gram.munhauzen.interaction.cannons.CannonsStoryManager;
import ua.gram.munhauzen.interaction.cannons.fragment.CannonsImageFragment;
import ua.gram.munhauzen.interaction.cannons.fragment.CannonsProgressBarFragment;
import ua.gram.munhauzen.interaction.cannons.fragment.CannonsScenarioFragment;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class CannonsInteraction extends AbstractInteraction {

    public ArrayList<CannonsScenario> scenarioRegistry;
    public CannonsStoryManager storyManager;
    public CannonsProgressBarFragment progressBarFragment;
    public CannonsScenarioFragment scenarioFragment;
    boolean isLoaded;
    public CannonsImageService imageService;
    public CannonsImageFragment imageFragment;
    public int wauCounter, maxWauCounter = 5;

    public CannonsInteraction(GameScreen gameScreen) {
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

        scenarioRegistry = gameScreen.game.databaseManager.loadCannonsScenario();

        storyManager = new CannonsStoryManager(gameScreen, this);
        imageService = new CannonsImageService(gameScreen, this);

        assetManager.load("cannons/inter_cannons_flood_worm.png", Texture.class);
        assetManager.load("cannons/inter_cannons_burn_worm.png", Texture.class);
        assetManager.load("cannons/inter_cannons_eat_worm.png", Texture.class);
    }

    public void onResourcesLoaded() {


        isLoaded = true;

        progressBarFragment = new CannonsProgressBarFragment(gameScreen, this);
        progressBarFragment.create();

        gameScreen.gameLayers.setInteractionProgressBarLayer(progressBarFragment);

        imageFragment = new CannonsImageFragment(this);
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

        CannonsStory story = storyManager.story;

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
