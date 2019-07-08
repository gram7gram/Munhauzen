package ua.gram.munhauzen.interaction;

import com.badlogic.gdx.graphics.Texture;

import ua.gram.munhauzen.entity.Story;
import ua.gram.munhauzen.interaction.servants.fragment.ServantsFireImageFragment;
import ua.gram.munhauzen.interaction.servants.fragment.ServantsHireImageFragment;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class ServantsInteraction extends AbstractInteraction {

    boolean isLoaded;
    public ServantsHireImageFragment hireFragment;
    public ServantsFireImageFragment fireFragment;

    public ServantsInteraction(GameScreen gameScreen) {
        super(gameScreen);
    }

    @Override
    public void start() {
        super.start();

        gameScreen.hideAndDestroyProgressBar();

        assetManager.load("ui/playbar_skip_backward.png", Texture.class);
        assetManager.load("ui/playbar_skip_backward_off.png", Texture.class);
        assetManager.load("ui/playbar_skip_forward.png", Texture.class);
        assetManager.load("ui/playbar_skip_forward_off.png", Texture.class);

        assetManager.load("GameScreen/an_cannons_main.png", Texture.class);
        assetManager.load("GameScreen/b_decision_add_line.png", Texture.class);
        assetManager.load("GameScreen/b_decision_first_line.png", Texture.class);
        assetManager.load("GameScreen/b_decision_last_line.png", Texture.class);

        assetManager.load("servants/icon_eagle.png", Texture.class);
        assetManager.load("servants/icon_helmet.png", Texture.class);
    }

    public void onResourcesLoaded() {
        isLoaded = true;

        hireFragment = new ServantsHireImageFragment(this);
        hireFragment.create();

        gameScreen.gameLayers.setInteractionLayer(hireFragment);
    }

    public void openFireFragment() {

        fireFragment = new ServantsFireImageFragment(this);
        fireFragment.create();

        gameScreen.gameLayers.setInteractionLayer(fireFragment);
    }

    public void complete() {
        Log.i(tag, "complete");

        try {
            gameScreen.interactionService.complete();

            Story newStory = gameScreen.storyManager.create("amoon_correct");

            gameScreen.setStory(newStory);

            gameScreen.restoreProgressBarIfDestroyed();

        } catch (Throwable e) {
            Log.e(tag, e);
        }
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

        if (hireFragment != null) {
            hireFragment.update();
        }

        if (fireFragment != null) {
            fireFragment.update();
        }
    }

    @Override
    public void dispose() {
        super.dispose();

        isLoaded = false;

        if (fireFragment != null) {
            fireFragment.dispose();
            fireFragment = null;
        }

        if (hireFragment != null) {
            hireFragment.dispose();
            hireFragment = null;
        }

    }
}
