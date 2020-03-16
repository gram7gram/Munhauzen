package ua.gram.munhauzen.interaction;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;

import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.entity.ServantsState;
import ua.gram.munhauzen.entity.StoryAudio;
import ua.gram.munhauzen.interaction.servants.fragment.ServantsFireImageFragment;
import ua.gram.munhauzen.interaction.servants.fragment.ServantsHireImageFragment;
import ua.gram.munhauzen.interaction.servants.fragment.ServantsProgressBarFragment;
import ua.gram.munhauzen.interaction.servants.hire.HireImageService;
import ua.gram.munhauzen.interaction.servants.hire.HireScenario;
import ua.gram.munhauzen.interaction.servants.hire.HireStory;
import ua.gram.munhauzen.interaction.servants.hire.HireStoryManager;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.utils.Log;
import ua.gram.munhauzen.utils.MathUtils;

public class ServantsInteraction extends AbstractInteraction {

    public ServantsHireImageFragment hireFragment;
    public ServantsFireImageFragment fireFragment;
    public ServantsProgressBarFragment progressBarFragment;
    public ArrayList<HireScenario> hireScenarioRegistry;
    public HireStoryManager storyManager;
    public HireImageService imageService;
    public String currentFragment;
    StoryAudio fireAudio;

    public ServantsInteraction(GameScreen gameScreen) {
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

        gameScreen.getActiveSave().servantsInteractionState = new ServantsState();

        openHireFragment();
    }

    public void openHireFragment() {

        gameScreen.audioService.stop(tag);

        imageService = new HireImageService(gameScreen, this);
        storyManager = new HireStoryManager(gameScreen, this);

        hireScenarioRegistry = gameScreen.game.databaseManager.loadServantsHireScenario();

        assetManager.clear();

        assetManager.load("ui/playbar_skip_backward.png", Texture.class);
        assetManager.load("ui/playbar_skip_forward.png", Texture.class);

        assetManager.load("GameScreen/an_cannons_main.png", Texture.class);
        assetManager.load("GameScreen/b_decision_add_line.png", Texture.class);
        assetManager.load("GameScreen/b_decision_first_line.png", Texture.class);
        assetManager.load("GameScreen/b_decision_last_line.png", Texture.class);

        assetManager.load("servants/icon_eagle.png", Texture.class);
        assetManager.load("servants/icon_helmet.png", Texture.class);

        assetManager.load("ui/playbar_pause.png", Texture.class);
        assetManager.load("ui/playbar_play.png", Texture.class);

        assetManager.load("ui/playbar_rewind_backward.png", Texture.class);
        assetManager.load("ui/playbar_rewind_forward.png", Texture.class);
        assetManager.load("ui/elements_player_fond_1.png", Texture.class);
        assetManager.load("ui/elements_player_fond_2.png", Texture.class);
        assetManager.load("ui/elements_player_fond_3.png", Texture.class);
        assetManager.load("ui/player_progress_bar_progress.9.jpg", Texture.class);
        assetManager.load("ui/player_progress_bar_knob.png", Texture.class);

        assetManager.finishLoading();

        ServantsState state = gameScreen.getActiveSave().servantsInteractionState;

        progressBarFragment = new ServantsProgressBarFragment(gameScreen, this);
        progressBarFragment.create();

        gameScreen.gameLayers.setInteractionProgressBarLayer(progressBarFragment);

        hireFragment = new ServantsHireImageFragment(this, state);
        hireFragment.create();

        gameScreen.gameLayers.setInteractionLayer(hireFragment);

        hireFragment.fadeInRoot();

        currentFragment = ServantsHireImageFragment.class.getSimpleName();
    }

    public void openFireFragment() {

        gameScreen.audioService.stop(tag);

        disposeHire();

        if (progressBarFragment != null) {
            progressBarFragment.destroy();
            progressBarFragment = null;
            gameScreen.gameLayers.setInteractionProgressBarLayer(null);
        }

        assetManager.clear();

        assetManager.load("GameScreen/an_cannons_main.png", Texture.class);
        assetManager.load("GameScreen/b_decision_add_line.png", Texture.class);
        assetManager.load("GameScreen/b_decision_first_line.png", Texture.class);
        assetManager.load("GameScreen/b_decision_last_line.png", Texture.class);

        assetManager.load("servants/icon_eagle.png", Texture.class);
        assetManager.load("servants/icon_helmet.png", Texture.class);
        assetManager.load("servants/inter_servants_fond.jpg", Texture.class);

        assetManager.finishLoading();

        fireFragment = new ServantsFireImageFragment(this);
        fireFragment.create();

        gameScreen.gameLayers.setInteractionLayer(fireFragment);

        fireFragment.fadeInRoot();

        currentFragment = ServantsFireImageFragment.class.getSimpleName();

        playFireOpened();
    }

    public void playFireOpened() {
        try {

            fireAudio = new StoryAudio();
            fireAudio.audio = MathUtils.random(new String[]{
                    "sfx_inter_servants_inventory_1",
                    "sfx_inter_servants_inventory_2",
                    "sfx_inter_servants_inventory_3",
                    "sfx_inter_servants_inventory_4",
                    "sfx_inter_servants_inventory_5"
            });

            gameScreen.audioService.prepareAndPlay(fireAudio);

        } catch (Throwable e) {
            Log.e(tag, e);

            gameScreen.onCriticalError(e);
        }
    }

    public void complete() {
        Log.i(tag, "complete");

        try {
            gameScreen.interactionService.complete();

            gameScreen.interactionService.findStoryAfterInteraction();

            gameScreen.restoreProgressBarIfDestroyed();

        } catch (Throwable e) {
            Log.e(tag, e);

            gameScreen.onCriticalError(e);
        }
    }

    @Override
    public void update() {
        super.update();

        if (assetManager == null) return;

        gameScreen.hideProgressBar();

        if (imageService != null) {
            imageService.update();
        }

        if (storyManager != null) {

            HireStory story = storyManager.story;

            if (story != null && !story.isCompleted) {

                if (!GameState.isPaused) {
                    storyManager.update(
                            story.progress + (Gdx.graphics.getDeltaTime() * 1000),
                            story.totalDuration
                    );
                }

                if (story.isCompleted) {
                    storyManager.onCompleted();
                }
            }
        }

        if (hireFragment != null) {
            hireFragment.update();
        }

        if (fireFragment != null) {
            fireFragment.update();
        }

        if (progressBarFragment != null) {
            progressBarFragment.update();
        }

        if (fireAudio != null) {
            gameScreen.audioService.updateVolume(fireAudio);
        }
    }

    private void disposeHire() {

        HireStory story = storyManager.story;
        if (story != null) {
            gameScreen.audioService.dispose(story);
            imageService.dispose(story);
        }

        if (imageService != null) {
            imageService.dispose();
            imageService = null;
        }

        if (storyManager != null) {
            storyManager.reset();
            storyManager = null;
        }

        hireScenarioRegistry.clear();
    }

    @Override
    public void dispose() {
        super.dispose();

        currentFragment = null;

        if (fireFragment != null) {
            fireFragment.dispose();
            fireFragment = null;
        }

        if (hireFragment != null) {
            hireFragment.dispose();
            hireFragment = null;
        }

        if (progressBarFragment != null) {
            progressBarFragment.dispose();
            progressBarFragment = null;
        }

        if (fireAudio != null) {
            gameScreen.audioService.stop(fireAudio);
            fireAudio = null;
        }

        disposeHire();

    }

    public boolean isLimitReached() {
        return hireFragment.servantCount == hireFragment.servantLimit;
    }
}
