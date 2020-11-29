package ua.gram.munhauzen.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Timer;

import ua.gram.munhauzen.GameLayerInterface;
import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.expansion.response.ExpansionResponse;
import ua.gram.munhauzen.screen.purchase.IAPObserver;
import ua.gram.munhauzen.screen.purchase.fragment.ControlsFragment;
import ua.gram.munhauzen.screen.purchase.fragment.ListFragment;
import ua.gram.munhauzen.screen.purchase.fragment.PromoFragment;
import ua.gram.munhauzen.screen.purchase.fragment.PurchasePendingFragment;
import ua.gram.munhauzen.screen.purchase.fragment.ThankYouFragment;
import ua.gram.munhauzen.screen.purchase.ui.Layers;
import ua.gram.munhauzen.ui.AdultGateFragment;
import ua.gram.munhauzen.ui.MunhauzenStage;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class PurchaseScreen extends MunhauzenScreen {

    public MunhauzenStage ui;
    public Texture background;
    public ListFragment fragment;
    public AdultGateFragment adultGateFragment;
    public PromoFragment promoFragment;
    public ThankYouFragment promoSuccessFragment;
    public Layers layers;
    public boolean isBackPressed;
    ControlsFragment controlsFragment;
    public IAPObserver observer;
    PurchasePendingFragment purchaseFragment;

    public PurchaseScreen(MunhauzenGame game) {
        super(game);
    }

    @Override
    public GameLayerInterface getLayers() {
        return layers;
    }

    @Override
    public void show() {

        if (game.params.isStandaloneProVersion) {
            game.navigator.closeApp();
            return;
        }

        ui = new MunhauzenStage(game);
        observer = new IAPObserver(this);

        layers = new Layers();

        game.internalAssetManager.load("bg4.jpg", Texture.class);
        game.internalAssetManager.load("bg3.png", Texture.class);
        game.internalAssetManager.load("purchase/full.png", Texture.class);
        game.internalAssetManager.load("purchase/part1.png", Texture.class);
        game.internalAssetManager.load("purchase/part2.png", Texture.class);
        game.internalAssetManager.load("purchase/free.png", Texture.class);
        game.internalAssetManager.load("purchase/chap1.png", Texture.class);
        game.internalAssetManager.load("purchase/chap3.png", Texture.class);
        game.internalAssetManager.load("purchase/chap5.png", Texture.class);
        game.internalAssetManager.load("purchase/chap10.png", Texture.class);
        game.internalAssetManager.load("purchase/thx.png", Texture.class);
        game.internalAssetManager.load("purchase/full_thx.png", Texture.class);
        game.internalAssetManager.load("purchase/ok.png", Texture.class);
        game.internalAssetManager.load("purchase/off.png", Texture.class);
        game.internalAssetManager.load("purchase/b_menu.png", Texture.class);
        game.internalAssetManager.load("purchase/restore.png", Texture.class);

        game.internalAssetManager.finishLoading();

        background = game.internalAssetManager.get("bg4.jpg", Texture.class);

        Gdx.input.setInputProcessor(ui);

        fragment = new ListFragment(this);
        fragment.create();

        controlsFragment = new ControlsFragment(PurchaseScreen.this);
        controlsFragment.create();

        controlsFragment.fadeIn();
        fragment.fadeIn();

        layers.setControlsLayer(controlsFragment);
        layers.setContentLayer(fragment);

        ui.addActor(layers);

        restorePurchases();
    }

    public void restorePurchases() {

        game.purchaseManager.start(observer);

        Timer.instance().scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                controlsFragment.fadeInRestore();

                if (!game.gameState.purchaseState.isGift6Viewed) {
                    game.gameState.purchaseState.isGift6Viewed = true;

                    openGift6Banner(new Runnable() {
                        @Override
                        public void run() {
                            destroyBanners();
                        }
                    });

                    game.syncState();
                }
            }
        }, 1);
    }

    public void onPurchaseCompleted() {
        Log.e(tag, "onPurchaseCompleted");

        try {
            if (game.gameState.expansionInfo == null) {
                game.gameState.expansionInfo = new ExpansionResponse();
            }

            game.gameState.purchaseState.isVersionSelected = true;
            game.gameState.expansionInfo.isCompleted = false;
            game.gameState.expansionInfo.isDownloadStarted = false;

            game.databaseManager.persistSync(game.gameState);

            if (!game.isOnlineMode()) {
                navigateTo(new LoadingScreen(game));
            } else {
                navigateTo(new MenuScreen(game));
            }

        } catch (Throwable e) {
            Log.e(tag, e);

            onCriticalError(e);
        }
    }

    public void checkBackPressed() {
        if (Gdx.input.isKeyPressed(Input.Keys.BACK)) {
            if (!isBackPressed) {
                isBackPressed = true;
                onBackPressed();
            }
        }
    }

    public void onBackPressed() {
        Log.i(tag, "onBackPressed");

        game.navigator.closeApp();
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void render(float delta) {

        drawBackground();

        if (ui == null) return;

        checkBackPressed();

        if (controlsFragment != null) {
            controlsFragment.update();
        }

        if (fragment != null) {
            fragment.update();
        }

        ui.act(delta);
        ui.draw();
    }

    private void drawBackground() {
        Gdx.gl.glClearColor(235 / 255f, 232 / 255f, 112 / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        if (background == null) return;

        game.batch.begin();
        game.batch.disableBlending();
        game.batch.draw(background,
                0, 0,
                MunhauzenGame.WORLD_WIDTH, MunhauzenGame.WORLD_HEIGHT);
        game.batch.enableBlending();
        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {
        MunhauzenGame.pauseGame();
    }

    @Override
    public void resume() {
        MunhauzenGame.resumeGame();
    }

    @Override
    public void dispose() {

        destroyBanners();

        if (ui != null) {
            ui.dispose();
        }

        game.internalAssetManager.unload("bg4.jpg");
        game.internalAssetManager.unload("bg3.png");
        game.internalAssetManager.unload("purchase/full.png");
        game.internalAssetManager.unload("purchase/part1.png");
        game.internalAssetManager.unload("purchase/part2.png");
        game.internalAssetManager.unload("purchase/free.png");
        game.internalAssetManager.unload("purchase/b_menu.png");
        game.internalAssetManager.unload("purchase/ok.png");
        game.internalAssetManager.unload("purchase/off.png");
        game.internalAssetManager.unload("purchase/restore.png");
    }

    public void onCriticalError(Throwable e) {
        game.navigator.onCriticalError(e);
    }

    public void navigateTo(Screen screen) {
        game.navigator.navigateTo(screen);
    }

    public void openAdultGateBanner(Runnable task) {
        super.openAdultGateBanner(task);

        try {

            destroyBanners();

            if (!game.params.isAdultGateEnabled) {
                if (task != null) {
                    task.run();
                }
                return;
            }

            adultGateFragment = new AdultGateFragment(this);
            adultGateFragment.create(task);

            layers.setBannerLayer(adultGateFragment);

            adultGateFragment.fadeIn();

        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }

    public void openPromoBanner() {

        Log.i(tag, "openPromoBanner");

        try {

            destroyBanners();

            promoFragment = new PromoFragment(this);
            promoFragment.create();

            layers.setBannerLayer(promoFragment);

            promoFragment.fadeIn();

        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }

    public void openPromoSuccessBanner() {

        try {

            destroyBanners();

            promoSuccessFragment = new ThankYouFragment(this);
            promoSuccessFragment.create();

            layers.setBannerLayer(promoSuccessFragment);

            promoSuccessFragment.fadeIn();

        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }

    public void openPurchasePendingBanner() {

        Log.i(tag, "openPurchasePendingBanner");

        try {

            destroyBanners();

            purchaseFragment = new PurchasePendingFragment(this);
            purchaseFragment.create();

            layers.setBannerLayer(purchaseFragment);

            purchaseFragment.fadeIn();

        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }

    @Override
    public void destroyBanners() {
        super.destroyBanners();

        if (adultGateFragment != null) {
            adultGateFragment.destroy();
            adultGateFragment = null;
        }
        if (promoFragment != null) {
            promoFragment.destroy();
            promoFragment = null;
        }
        if (promoSuccessFragment != null) {
            promoSuccessFragment.destroy();
            promoSuccessFragment = null;
        }
        if (purchaseFragment != null) {
            purchaseFragment.destroy();
            purchaseFragment = null;
        }
    }

}
