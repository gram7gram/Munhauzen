package ua.gram.munhauzen.interaction;

import com.badlogic.gdx.graphics.Texture;

import ua.gram.munhauzen.interaction.puzzle.PuzzleDecisionManager;
import ua.gram.munhauzen.interaction.puzzle.fragment.PuzzleImageFragment;
import ua.gram.munhauzen.screen.GameScreen;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class PuzzleInteraction extends AbstractInteraction {

    boolean isLoaded;
    public PuzzleImageFragment imageFragment;
    public PuzzleDecisionManager decisionManager;

    public PuzzleInteraction(GameScreen gameScreen) {
        super(gameScreen);
    }

    @Override
    public void start() {
        super.start();

        gameScreen.hideProgressBar();

        decisionManager = new PuzzleDecisionManager(this);

        assetManager.load("puzzle/inter_puzzle_stick_1.png", Texture.class);
        assetManager.load("puzzle/inter_puzzle_spoon_1.png", Texture.class);
        assetManager.load("puzzle/inter_puzzle_shoes_1.png", Texture.class);
        assetManager.load("puzzle/inter_puzzle_peas_1.png", Texture.class);
        assetManager.load("puzzle/inter_puzzle_key_1.png", Texture.class);
        assetManager.load("puzzle/inter_puzzle_hair_1.png", Texture.class);
        assetManager.load("puzzle/inter_puzzle_clocks_1.png", Texture.class);
        assetManager.load("puzzle/inter_puzzle_arrows_1.png", Texture.class);
        assetManager.load("puzzle/inter_puzzle_powder_1.png", Texture.class);
        assetManager.load("puzzle/inter_puzzle_rope_1.png", Texture.class);
        assetManager.load("puzzle/inter_puzzle_fond_1.png", Texture.class);
        assetManager.load("puzzle/inter_puzzle_fond_2.png", Texture.class);
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

        gameScreen.hideProgressBar();

        assetManager.update();

        if (!isLoaded) {
            if (assetManager.isFinished()) {
                onResourcesLoaded();
            }
            return;
        }

        gameScreen.hideProgressBar();

        if (imageFragment != null) {
            imageFragment.update();
        }

        if (decisionManager != null) {
            decisionManager.update();
        }
    }

    @Override
    public void dispose() {
        super.dispose();

        if (decisionManager != null) {
            decisionManager.dispose();
        }

        isLoaded = false;

    }
}
