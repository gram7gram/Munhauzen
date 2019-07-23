package ua.gram.munhauzen.interaction;

import com.badlogic.gdx.graphics.Texture;

import ua.gram.munhauzen.interaction.horn.fragment.HornImageFragment;
import ua.gram.munhauzen.screen.GameScreen;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class HornInteraction extends AbstractInteraction {

    boolean isLoaded;
    public HornImageFragment imageFragment;

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
