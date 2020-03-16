package ua.gram.munhauzen.interaction;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Timer;

import ua.gram.munhauzen.entity.StoryAudio;
import ua.gram.munhauzen.interaction.balloons.fragment.BalloonsImageFragment;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.utils.Log;
import ua.gram.munhauzen.utils.MathUtils;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class BalloonsInteraction extends AbstractInteraction {

    public BalloonsImageFragment imageFragment;
    boolean isLoaded;
    StoryAudio introAudio;

    public BalloonsInteraction(GameScreen gameScreen) {
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

        assetManager.load("balloons/lv_cloud_1.png", Texture.class);
        assetManager.load("balloons/lv_cloud_2.png", Texture.class);
        assetManager.load("balloons/lv_cloud_3.png", Texture.class);
        assetManager.load("balloons/ducks_sheet_1x6.png", Texture.class);
        assetManager.load("balloons/inter_balloons_1.png", Texture.class);
        assetManager.load("balloons/inter_balloons_2.png", Texture.class);
        assetManager.load("balloons/inter_balloons_3.png", Texture.class);
        assetManager.load("balloons/inter_balloons_4.png", Texture.class);
        assetManager.load("balloons/inter_balloons_fond.jpg", Texture.class);
    }

    public void onResourcesLoaded() {


        isLoaded = true;

        imageFragment = new BalloonsImageFragment(this);
        imageFragment.create();

        gameScreen.gameLayers.setInteractionLayer(imageFragment);

        imageFragment.fadeInRoot();

        playIntro();

        Timer.instance().scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                imageFragment.start();
            }
        }, introAudio.duration / 1000f);
    }

    private void playIntro() {
        try {
            introAudio = new StoryAudio();
            introAudio.audio = MathUtils.random(new String[]{
                    "sfx_inter_start_1",
                    "sfx_inter_start_2",
                    "sfx_inter_start_3",
                    "sfx_inter_start_4"
            });

            gameScreen.audioService.prepareAndPlay(introAudio);

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

        if (imageFragment != null)
            imageFragment.update();

        if (introAudio != null) {
            gameScreen.audioService.updateVolume(introAudio);
        }
    }

    @Override
    public void dispose() {
        super.dispose();

        if (introAudio != null) {
            gameScreen.audioService.stop(introAudio);
            introAudio = null;
        }

        isLoaded = false;
    }

    public void complete() {
        try {

            gameScreen.interactionService.complete();

            gameScreen.interactionService.findStoryAfterInteraction();

            gameScreen.restoreProgressBarIfDestroyed();

        } catch (Throwable e) {
            Log.e(tag, e);

            gameScreen.onCriticalError(e);
        }
    }
}
