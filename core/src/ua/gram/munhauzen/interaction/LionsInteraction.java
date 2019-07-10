package ua.gram.munhauzen.interaction;

import com.badlogic.gdx.graphics.Texture;

import ua.gram.munhauzen.interaction.lions.fragment.LionsImageFragment;
import ua.gram.munhauzen.screen.GameScreen;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class LionsInteraction extends AbstractInteraction {

    boolean isLoaded;
    public LionsImageFragment imageFragment;

    public LionsInteraction(GameScreen gameScreen) {
        super(gameScreen);
    }

    @Override
    public void start() {
        super.start();

        gameScreen.hideProgressBar();

        assetManager.load("lions/int_lions_fond.jpg", Texture.class);

    }

    public void onResourcesLoaded() {
        isLoaded = true;

        imageFragment = new LionsImageFragment(this);
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
