package ua.gram.munhauzen.interaction;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Timer;

import ua.gram.munhauzen.entity.Story;
import ua.gram.munhauzen.entity.StoryAudio;
import ua.gram.munhauzen.interaction.date.fragment.DateImageFragment;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class DateInteraction extends AbstractInteraction {

    boolean isLoaded;
    public DateImageFragment imageFragment;
    StoryAudio winAudio;

    public DateInteraction(GameScreen gameScreen) {
        super(gameScreen);
    }

    @Override
    public boolean isValid() {
        return true;
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
        assetManager.load("ui/playbar_skip_forward.png", Texture.class);

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

            playWin();

            Timer.instance().scheduleTask(new Timer.Task() {
                @Override
                public void run() {
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
            }, winAudio.duration / 1000f);

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

        if (assetManager == null) return;

        gameScreen.hideProgressBar();

        if (!isLoaded) {
            if (assetManager.isFinished()) {
                onResourcesLoaded();
            }
            return;
        }

        if (imageFragment != null) {
            imageFragment.update();
        }

        if (winAudio != null) {
            gameScreen.audioService.updateVolume(winAudio);
        }
    }

    private void playWin() {
        try {
            winAudio = new StoryAudio();
            winAudio.audio = "sfx_inter_date";

            gameScreen.audioService.prepareAndPlay(winAudio);

        } catch (Throwable e) {
            Log.e(tag, e);

            gameScreen.onCriticalError(e);
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

        if (winAudio != null) {
            gameScreen.audioService.stop(winAudio);
            winAudio = null;
        }

    }
}
