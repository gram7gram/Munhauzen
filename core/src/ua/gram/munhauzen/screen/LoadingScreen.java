package ua.gram.munhauzen.screen;

import com.badlogic.gdx.graphics.Texture;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.screen.loading.fragment.ControlsFragment;
import ua.gram.munhauzen.screen.loading.fragment.ImageFragment;
import ua.gram.munhauzen.screen.loading.ui.LoadingLayers;
import ua.gram.munhauzen.service.ExpansionDownloadManager;
import ua.gram.munhauzen.utils.InternalAssetManager;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class LoadingScreen extends AbstractScreen {

    public LoadingLayers layers;
    public ImageFragment imageFragment;
    public ControlsFragment controlsFragment;
    public ExpansionDownloadManager expansionDownloader;

    public LoadingScreen(MunhauzenGame game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();

        assetManager = new InternalAssetManager();

        assetManager.load("loading/t_putty.png", Texture.class);
        assetManager.load("loading/p_loading.jpg", Texture.class);
        assetManager.load("loading/lv_decor_1.png", Texture.class);
        assetManager.load("loading/ducks_sheet_1x6.png", Texture.class);
        assetManager.load("loading/lv_cloud_1.png", Texture.class);
        assetManager.load("loading/lv_cloud_2.png", Texture.class);
        assetManager.load("loading/lv_cloud_3.png", Texture.class);
        assetManager.load("loading/lv_balloon.png", Texture.class);
        assetManager.load("loading/lv_hat.png", Texture.class);
        assetManager.load("loading/lv_hat2.png", Texture.class);
        assetManager.load("loading/lv_hat3.png", Texture.class);
        assetManager.load("loading/an_painting.png", Texture.class);
        assetManager.load("loading/lv_hair.png", Texture.class);
        assetManager.load("loading/lv_axe.png", Texture.class);
        assetManager.load("loading/lv_balloon.png", Texture.class);
        assetManager.load("loading/lv_baron.png", Texture.class);
        assetManager.load("loading/lv_bomb.png", Texture.class);
        assetManager.load("loading/lv_clocks.png", Texture.class);
        assetManager.load("loading/lv_cup.png", Texture.class);
        assetManager.load("loading/lv_dog.png", Texture.class);
        assetManager.load("loading/lv_shoes.png", Texture.class);
        assetManager.load("loading/lv_statue.png", Texture.class);
        assetManager.load("loading/lv_moon.png", Texture.class);
        assetManager.load("loading/lv_sheep.png", Texture.class);
        assetManager.load("loading/lv_shovel.png", Texture.class);

        layers = new LoadingLayers();

        ui.addActor(layers);
    }

    @Override
    public void onResourcesLoaded() {
        super.onResourcesLoaded();

        try {

            if (game.gameState.expansionInfo != null) {
                game.gameState.expansionInfo.isDownloadStarted = false;
            }

            controlsFragment = new ControlsFragment(this);
            controlsFragment.create();

            layers.setControlsLayer(controlsFragment);

            imageFragment = new ImageFragment(this);
            imageFragment.create();

            layers.setContentLayer(imageFragment);

            expansionDownloader = new ExpansionDownloadManager(game, controlsFragment);

            expansionDownloader.fetchExpansionToDownload();

        } catch (Throwable e) {
            Log.e(tag, e);

            onCriticalError(e);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        game.navigator.navigateTo(new PurchaseScreen(game));
    }

    @Override
    public void renderAfterLoaded(float delta) {

        if (imageFragment != null) {
            imageFragment.update();
        }

        if (controlsFragment != null) {
            controlsFragment.update();
        }

        if (expansionDownloader != null) {
            expansionDownloader.updateProgress();
        }
    }

    @Override
    public void dispose() {
        super.dispose();

        if (expansionDownloader != null) {
            expansionDownloader.dispose();
            expansionDownloader = null;
        }

        if (layers != null) {
            layers.dispose();
            layers = null;
        }

        if (imageFragment != null) {
            imageFragment.destroy();
            imageFragment = null;
        }

        if (controlsFragment != null) {
            controlsFragment.destroy();
            controlsFragment = null;
        }
    }
}
