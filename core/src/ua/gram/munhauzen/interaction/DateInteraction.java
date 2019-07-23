package ua.gram.munhauzen.interaction;

import com.badlogic.gdx.graphics.Texture;

import ua.gram.munhauzen.entity.Story;
import ua.gram.munhauzen.interaction.date.fragment.DateImageFragment;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class DateInteraction extends AbstractInteraction {

    boolean isLoaded;
    public DateImageFragment imageFragment;

    public DateInteraction(GameScreen gameScreen) {
        super(gameScreen);
    }

    @Override
    public void start() {
        super.start();

        gameScreen.hideProgressBar();

        assetManager.load("date/inter_date_autumn.png", Texture.class);
        assetManager.load("date/inter_date_summer.png", Texture.class);
        assetManager.load("date/inter_date_winter.png", Texture.class);
        assetManager.load("date/inter_date_spring.png", Texture.class);
        assetManager.load("date/back.jpg", Texture.class);

        assetManager.load("ui/playbar_skip_backward.png", Texture.class);
        assetManager.load("ui/playbar_skip_backward_off.png", Texture.class);
        assetManager.load("ui/playbar_skip_forward.png", Texture.class);
        assetManager.load("ui/playbar_skip_forward_off.png", Texture.class);

        assetManager.load("GameScreen/an_cannons_main.png", Texture.class);
        assetManager.load("GameScreen/b_decision_add_line.png", Texture.class);
        assetManager.load("GameScreen/b_decision_first_line.png", Texture.class);
        assetManager.load("GameScreen/b_decision_last_line.png", Texture.class);

    }

    public void onResourcesLoaded() {
        isLoaded = true;

        imageFragment = new DateImageFragment(this);
        imageFragment.create();

        gameScreen.gameLayers.setInteractionLayer(imageFragment);

        imageFragment.fadeInRoot();
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

            gameScreen.onCriticalError(e);
        }
    }

    public void discard() {
        Log.i(tag, "discard");

        try {

            gameScreen.interactionService.complete();

            Story newStory = gameScreen.storyManager.create("amoon_proxy");

            gameScreen.setStory(newStory);

            gameScreen.restoreProgressBarIfDestroyed();

        } catch (Throwable e) {
            Log.e(tag, e);

            gameScreen.onCriticalError(e);
        }
    }

    @Override
    public void update() {
        super.update();

        gameScreen.hideProgressBar();

        assetManager.update();

        if (!isLoaded) {
            if (assetManager.isFinished()) {
                onResourcesLoaded();
            }
            return;
        }

        if (imageFragment != null) {
            imageFragment.update();
        }
    }

    @Override
    public void dispose() {
        super.dispose();

        isLoaded = false;

        if (imageFragment != null) {
            imageFragment.dispose();
            imageFragment = null;
        }

    }
}
