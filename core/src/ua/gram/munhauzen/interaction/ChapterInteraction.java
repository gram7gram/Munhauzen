package ua.gram.munhauzen.interaction;

import com.badlogic.gdx.graphics.Texture;

import ua.gram.munhauzen.interaction.chapter.fragment.ChapterImageFragment;
import ua.gram.munhauzen.screen.GameScreen;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class ChapterInteraction extends AbstractInteraction {

    boolean isLoaded;
    public ChapterImageFragment imageFragment;

    public ChapterInteraction(GameScreen gameScreen) {
        super(gameScreen);
    }

    @Override
    public void start() {
        super.start();

        gameScreen.hideProgressBar();

        assetManager.load("chapter/frame_2.png", Texture.class);

        if (gameScreen.game.params.isPro) {
            assetManager.load("chapter/b_full_version_1.png", Texture.class);
        } else {
            assetManager.load("chapter/b_demo_version.png", Texture.class);
        }
    }

    public void onResourcesLoaded() {

        isLoaded = true;

        imageFragment = new ChapterImageFragment(this);
        imageFragment.create();

        gameScreen.gameLayers.setInteractionLayer(imageFragment);
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
