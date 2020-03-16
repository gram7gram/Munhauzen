package ua.gram.munhauzen.interaction;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;

import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.interaction.hare.HareScenario;
import ua.gram.munhauzen.interaction.hare.HareStory;
import ua.gram.munhauzen.interaction.hare.HareStoryManager;
import ua.gram.munhauzen.interaction.hare.fragment.HareImageFragment;
import ua.gram.munhauzen.interaction.hare.fragment.HareProgressBarFragment;
import ua.gram.munhauzen.interaction.hare.fragment.HareScenarioFragment;
import ua.gram.munhauzen.screen.GameScreen;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class HareInteraction extends AbstractInteraction {

    public ArrayList<HareScenario> scenarioRegistry;
    public HareStoryManager storyManager;
    public HareProgressBarFragment progressBarFragment;
    public HareImageFragment imageFragment;
    public HareScenarioFragment scenarioFragment;
    boolean isLoaded;

    public HareInteraction(GameScreen gameScreen) {
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

        gameScreen.hideImageFragment();

        scenarioRegistry = gameScreen.game.databaseManager.loadHareScenario();
        storyManager = new HareStoryManager(gameScreen, this);

        assetManager.load("hare/lv_cloud_1.png", Texture.class);
        assetManager.load("hare/lv_cloud_2.png", Texture.class);
        assetManager.load("hare/lv_cloud_3.png", Texture.class);
        assetManager.load("hare/ducks_sheet_1x6.png", Texture.class);
        assetManager.load("hare/hare_sheet.png", Texture.class);
        assetManager.load("hare/horse_sheet.png", Texture.class);
        assetManager.load("hare/inter_hare_ground.png", Texture.class);
        assetManager.load("hare/inter_hare_butterflies.png", Texture.class);
        assetManager.load("images/p18_d.jpg", Texture.class);
    }

    public void onResourcesLoaded() {

        isLoaded = true;

        progressBarFragment = new HareProgressBarFragment(gameScreen, this);
        progressBarFragment.create();

        gameScreen.gameLayers.setInteractionProgressBarLayer(progressBarFragment);

        storyManager.resume();

        imageFragment = new HareImageFragment(this);
        imageFragment.create();

        gameScreen.gameLayers.setInteractionLayer(imageFragment);

        imageFragment.fadeInRoot();
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

        HareStory story = storyManager.story;

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
}
