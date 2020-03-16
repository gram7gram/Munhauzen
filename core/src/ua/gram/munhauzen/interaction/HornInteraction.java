package ua.gram.munhauzen.interaction;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Timer;

import ua.gram.munhauzen.entity.StoryAudio;
import ua.gram.munhauzen.interaction.horn.fragment.HornImageFragment;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class HornInteraction extends AbstractInteraction {

    boolean isLoaded;
    public HornImageFragment imageFragment;
    StoryAudio storyAudio;

    public HornInteraction(GameScreen gameScreen) {
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

        assetManager.load("continue/btn_enabled.png", Texture.class);
        assetManager.load("horn/int_horn_horn.png", Texture.class);
        assetManager.load("horn/int_horn_note_1.png", Texture.class);
        assetManager.load("horn/int_horn_note_2.png", Texture.class);
    }

    public void onResourcesLoaded() {

        isLoaded = true;

        imageFragment = new HornImageFragment(this);
        imageFragment.create();

        gameScreen.gameLayers.setInteractionLayer(imageFragment);

        imageFragment.fadeInRoot();

        playIntro();
    }

    private void playIntro() {
        try {

            storyAudio = new StoryAudio();
            storyAudio.audio = "sfx_inter_horn";

            gameScreen.audioService.prepareAndPlay(storyAudio);
            storyAudio.player.setLooping(true);

        } catch (Throwable e) {
            Log.e(tag, e);

            gameScreen.onCriticalError(e);
        }
    }

    private void playEnding() {
        try {

            if (storyAudio != null) {
                gameScreen.audioService.stop(storyAudio);
                storyAudio = null;
            }

            storyAudio = new StoryAudio();
            storyAudio.audio = "s33_fin";

            gameScreen.audioService.prepareAndPlay(storyAudio);

        } catch (Throwable e) {
            Log.e(tag, e);

            gameScreen.onCriticalError(e);
        }
    }

    public void complete() {
        Log.i(tag, "complete");

        playEnding();

        if (storyAudio != null)
            Timer.instance().scheduleTask(new Timer.Task() {
                @Override
                public void run() {
                    try {

                        gameScreen.interactionService.complete();

                        gameScreen.interactionService.findStoryAfterInteraction();

                        gameScreen.restoreProgressBarIfDestroyed();

                    } catch (Throwable e) {
                        Log.e(tag, e);

                        gameScreen.onCriticalError(e);
                    }
                }
            }, storyAudio.duration / 1000f);
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

        if (storyAudio != null) {
            gameScreen.audioService.updateVolume(storyAudio);
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

        if (storyAudio != null) {
            gameScreen.audioService.stop(storyAudio);
            storyAudio = null;
        }

    }
}
