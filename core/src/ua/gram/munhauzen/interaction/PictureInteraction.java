package ua.gram.munhauzen.interaction;

import com.badlogic.gdx.graphics.Texture;

import ua.gram.munhauzen.interaction.picture.fragment.PictureImageFragment;
import ua.gram.munhauzen.screen.GameScreen;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class PictureInteraction extends AbstractInteraction {

    boolean isLoaded;
    public PictureImageFragment imageFragment;

    public PictureInteraction(GameScreen gameScreen) {
        super(gameScreen);
    }

    @Override
    public void start() {
        super.start();

        gameScreen.hideProgressBar();

        assetManager.load("picture/p35_what.jpg", Texture.class);
    }

    public void onResourcesLoaded() {
        isLoaded = true;

        imageFragment = new PictureImageFragment(this);
        imageFragment.create();

        gameScreen.gameLayers.setInteractionLayer(imageFragment);
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
