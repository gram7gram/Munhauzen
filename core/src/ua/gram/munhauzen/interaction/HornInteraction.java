package ua.gram.munhauzen.interaction;

import com.badlogic.gdx.graphics.Texture;

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
    StoryAudio introAudio;

    public HornInteraction(GameScreen gameScreen) {
        super(gameScreen);
    }

    @Override
    public void start() {
        super.start();

        gameScreen.hideProgressBar();

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

//        playIntro();
    }

    private void playIntro() {
        try {

            introAudio = new StoryAudio();
            introAudio.audio = "sfx_inter_horn";

            gameScreen.audioService.prepareAndPlay(introAudio);
            introAudio.player.setLooping(true);

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

        if (introAudio != null) {
            gameScreen.audioService.updateVolume(introAudio);
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

        if (introAudio != null) {
            gameScreen.audioService.stop(introAudio);
            introAudio = null;
        }

    }
}
