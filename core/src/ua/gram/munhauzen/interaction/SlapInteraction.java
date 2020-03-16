package ua.gram.munhauzen.interaction;

import com.badlogic.gdx.graphics.Texture;

import ua.gram.munhauzen.interaction.slap.fragment.SlapImageFragment;
import ua.gram.munhauzen.screen.GameScreen;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class SlapInteraction extends AbstractInteraction {

    public SlapImageFragment imageFragment;
    boolean isLoaded;

    public SlapInteraction(GameScreen gameScreen) {
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

        assetManager.load("slap/inter_slap_1.jpg", Texture.class);
        assetManager.load("slap/inter_slap_2.jpg", Texture.class);
        assetManager.load("slap/inter_slap_3.jpg", Texture.class);
        assetManager.load("slap/inter_slap_doors.png", Texture.class);
    }

    public void onResourcesLoaded() {

        isLoaded = true;

        imageFragment = new SlapImageFragment(this);
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

        if (imageFragment != null)
            imageFragment.update();
    }

    @Override
    public void dispose() {
        super.dispose();

        isLoaded = false;
    }
}
