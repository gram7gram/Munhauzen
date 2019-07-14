package ua.gram.munhauzen.interaction;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import ua.gram.munhauzen.interaction.balloons.fragment.BalloonsImageFragment;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class BalloonsInteraction extends AbstractInteraction {

    public BalloonsImageFragment imageFragment;
    boolean isLoaded;
    public ShapeRenderer shapeRenderer;

    public BalloonsInteraction(GameScreen gameScreen) {
        super(gameScreen);
    }

    @Override
    public void start() {
        super.start();

        gameScreen.hideProgressBar();

        shapeRenderer = new ShapeRenderer();

        assetManager.load("LoadingScreen/lv_cloud_1.png", Texture.class);
        assetManager.load("LoadingScreen/lv_cloud_2.png", Texture.class);
        assetManager.load("LoadingScreen/lv_cloud_3.png", Texture.class);
        assetManager.load("balloons/ducks_sheet_1x5.png", Texture.class);
        assetManager.load("balloons/inter_balloons_1.png", Texture.class);
        assetManager.load("balloons/inter_balloons_2.png", Texture.class);
        assetManager.load("balloons/inter_balloons_3.png", Texture.class);
        assetManager.load("balloons/inter_balloons_4.png", Texture.class);
        assetManager.load("balloons/inter_balloons_fond.jpg", Texture.class);
    }

    public void onResourcesLoaded() {
        isLoaded = true;

        imageFragment = new BalloonsImageFragment(this);
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

        if (imageFragment != null)
            imageFragment.update();
    }

    @Override
    public void dispose() {
        super.dispose();

        isLoaded = false;
    }

    public void complete() {
        try {

            gameScreen.interactionService.complete();

            gameScreen.interactionService.findStoryAfterInteraction();

            gameScreen.restoreProgressBarIfDestroyed();

        } catch (Throwable e) {
            Log.e(tag, e);

            gameScreen.onCriticalError(e);
        }
    }
}
