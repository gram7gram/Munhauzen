package ua.gram.munhauzen.interaction;

import com.badlogic.gdx.graphics.Texture;

import ua.gram.munhauzen.interaction.swamp.fragment.SwampImageFragment;
import ua.gram.munhauzen.screen.GameScreen;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class SwampInteraction extends AbstractInteraction {

    public SwampImageFragment imageFragment;
    boolean isLoaded;

    public SwampInteraction(GameScreen gameScreen) {
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

        assetManager.load("swamp/int_swamp_1.jpg", Texture.class);
        assetManager.load("swamp/int_swamp_2.png", Texture.class);
        assetManager.load("swamp/int_swamp_3.png", Texture.class);
    }

    public void onResourcesLoaded() {

        isLoaded = true;

        imageFragment = new SwampImageFragment(this);
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
