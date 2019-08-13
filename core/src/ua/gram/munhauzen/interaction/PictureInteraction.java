package ua.gram.munhauzen.interaction;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;

import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.interaction.picture.PictureImageService;
import ua.gram.munhauzen.interaction.picture.PictureScenario;
import ua.gram.munhauzen.interaction.picture.PictureStory;
import ua.gram.munhauzen.interaction.picture.PictureStoryManager;
import ua.gram.munhauzen.interaction.picture.fragment.PictureImageFragment;
import ua.gram.munhauzen.interaction.picture.fragment.PictureProgressBarFragment;
import ua.gram.munhauzen.interaction.picture.fragment.PictureScenarioFragment;
import ua.gram.munhauzen.screen.GameScreen;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class PictureInteraction extends AbstractInteraction {

    boolean isLoaded;
    public PictureImageFragment imageFragment;
    public PictureScenarioFragment scenarioFragment;
    public PictureProgressBarFragment progressBarFragment;
    public PictureStoryManager storyManager;
    public Array<PictureScenario> scenarioRegistry;
    public PictureImageService imageService;

    public PictureInteraction(GameScreen gameScreen) {
        super(gameScreen);
    }

    @Override
    public void start() {
        super.start();

        gameScreen.hideProgressBar();

        scenarioRegistry = gameScreen.game.databaseManager.loadPictureScenario();

        storyManager = new PictureStoryManager(gameScreen, this);

        imageService = new PictureImageService(gameScreen, this);

        assetManager.load("picture/p35_what.jpg", Texture.class);
    }

    public void onResourcesLoaded() {

        isLoaded = true;

        imageFragment = new PictureImageFragment(this);
        imageFragment.create();

        gameScreen.gameLayers.setInteractionLayer(imageFragment);

        imageFragment.fadeInRoot();

        progressBarFragment = new PictureProgressBarFragment(gameScreen, this);
        progressBarFragment.create();

        gameScreen.gameLayers.setInteractionProgressBarLayer(progressBarFragment);

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

        PictureStory story = storyManager.story;

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

        if (imageFragment != null) {
            imageFragment.update();
        }

        if (progressBarFragment != null)
            progressBarFragment.update();

        if (scenarioFragment != null)
            scenarioFragment.update();
    }

    @Override
    public void dispose() {
        super.dispose();

        isLoaded = false;

        if (imageFragment != null) {
            imageFragment.destroy();
            imageFragment = null;
        }

        if (progressBarFragment != null) {
            progressBarFragment.destroy();
            progressBarFragment = null;
        }

        if (scenarioFragment != null) {
            scenarioFragment.destroy();
            scenarioFragment = null;
        }

        if (storyManager != null) {
            gameScreen.audioService.dispose(storyManager.story);
            storyManager = null;
        }

    }

    @Override
    public void drawOnTop() {
        super.drawOnTop();

        if (scenarioFragment != null) {
            scenarioFragment.draw();
        }
    }
}
