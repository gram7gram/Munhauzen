package ua.gram.munhauzen.interaction;

import com.badlogic.gdx.graphics.Texture;

import ua.gram.munhauzen.entity.StoryAudio;
import ua.gram.munhauzen.interaction.puzzle.PuzzleDecisionManager;
import ua.gram.munhauzen.interaction.puzzle.fragment.PuzzleImageFragment;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class PuzzleInteraction extends AbstractInteraction {

    boolean isLoaded;
    public PuzzleImageFragment imageFragment;
    public PuzzleDecisionManager decisionManager;
    StoryAudio introAudio;

    public final static String[] externalAudio = {
            "s15_1_a",
            "s15_1_a_crow",
            "s15_1_b",
            "s15_1_d",
            "s15_1_c",
            "s15_1_c_crow",
            "s15broken_1", "s15broken_2", "s15broken_3", "s15broken_4", "s15broken_5",
            "s15crow_1", "s15crow_2", "s15crow_3", "s15crow_4", "s15crow_5"
    };

    public PuzzleInteraction(GameScreen gameScreen) {
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
        assetManager.load("puzzle/inter_puzzle_fond_1.jpg", Texture.class);
        assetManager.load("puzzle/inter_puzzle_fond_2.png", Texture.class);
    }

    public void onResourcesLoaded() {

        isLoaded = true;

        imageFragment = new PuzzleImageFragment(this);
        imageFragment.create();

        gameScreen.gameLayers.setInteractionLayer(imageFragment);

        imageFragment.fadeInRoot();

        playIntro();
    }

    private void playIntro() {
        try {

            introAudio = new StoryAudio();
            introAudio.audio = "sfx_inter_puzzle_1";

            gameScreen.audioService.prepareAndPlay(introAudio);

        } catch (Throwable e) {
            Log.e(tag, e);

//            gameScreen.onCriticalError(e);
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

        gameScreen.hideProgressBar();

        if (imageFragment != null) {
            imageFragment.update();
        }

        if (decisionManager != null) {
            decisionManager.update();
        }

        if (introAudio != null) {
            gameScreen.audioService.updateVolume(introAudio);
        }
    }

    @Override
    public void dispose() {
        super.dispose();

        if (decisionManager != null) {
            decisionManager.dispose();
        }

        if (introAudio != null) {
            gameScreen.audioService.stop(introAudio);
            introAudio = null;
        }

        isLoaded = false;

    }
}
