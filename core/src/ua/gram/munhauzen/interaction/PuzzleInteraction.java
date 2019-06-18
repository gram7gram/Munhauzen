package ua.gram.munhauzen.interaction;

import com.badlogic.gdx.graphics.Texture;

import ua.gram.munhauzen.interaction.generals.fragment.GeneralsImageFragment;
import ua.gram.munhauzen.interaction.puzzle.fragment.PuzzleImageFragment;
import ua.gram.munhauzen.screen.GameScreen;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class PuzzleInteraction extends AbstractInteraction {

    boolean isLoaded;
    PuzzleImageFragment imageFragment;

    public PuzzleInteraction(GameScreen gameScreen) {
        super(gameScreen);
    }

    @Override
    public void start() {
        super.start();

//        assetManager.load("generals/an_general_1_sheet_3x1.png", Texture.class);
    }

    public void onResourcesLoaded() {
        isLoaded = true;

        imageFragment = new PuzzleImageFragment(this);
        imageFragment.create();

        gameScreen.gameLayers.setInteractionLayer(imageFragment);
    }

    @Override
    public void update() {
        super.update();

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

    }
}
