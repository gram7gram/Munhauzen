package ua.gram.munhauzen.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.pay.Offer;
import com.badlogic.gdx.pay.OfferType;
import com.badlogic.gdx.pay.PurchaseManagerConfig;
import com.badlogic.gdx.utils.Timer;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.expansion.response.ExpansionResponse;
import ua.gram.munhauzen.screen.purchase.IAPObserver;
import ua.gram.munhauzen.screen.purchase.fragment.ControlsFragment;
import ua.gram.munhauzen.screen.purchase.fragment.ListFragment;
import ua.gram.munhauzen.ui.MunhauzenStage;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class PurchaseScreen implements Screen {

    public final String tag = getClass().getSimpleName();
    public final MunhauzenGame game;
    public MunhauzenStage ui;
    public Texture background;
    public ListFragment fragment;
    public boolean isBackPressed;
    ControlsFragment controlsFragment;
    IAPObserver observer;

    public PurchaseScreen(MunhauzenGame game) {
        this.game = game;
    }

    @Override
    public void show() {

        ui = new MunhauzenStage(game);
        observer = new IAPObserver(this);

        game.internalAssetManager.load("bg4.jpg", Texture.class);
        game.internalAssetManager.load("bg3.png", Texture.class);
        game.internalAssetManager.load("purchase/full.png", Texture.class);
        game.internalAssetManager.load("purchase/part1.png", Texture.class);
        game.internalAssetManager.load("purchase/part2.png", Texture.class);
        game.internalAssetManager.load("purchase/free.png", Texture.class);
        game.internalAssetManager.load("purchase/ok.png", Texture.class);
        game.internalAssetManager.load("purchase/off.png", Texture.class);
        game.internalAssetManager.load("purchase/b_menu.png", Texture.class);
        game.internalAssetManager.load("purchase/restore.png", Texture.class);

        game.internalAssetManager.finishLoading();

        background = game.internalAssetManager.get("bg4.jpg", Texture.class);

        Gdx.input.setInputProcessor(ui);

        fragment = new ListFragment(this);
        fragment.create();

        ui.addActor(fragment.getRoot());

        controlsFragment = new ControlsFragment(PurchaseScreen.this);
        controlsFragment.create();

        ui.addActor(controlsFragment.getRoot());

        controlsFragment.fadeIn();
        fragment.fadeIn();

        restorePurchases();
    }

    public void restorePurchases() {

        PurchaseManagerConfig pmc = new PurchaseManagerConfig();

        pmc.addOffer(new Offer()
                .setType(OfferType.ENTITLEMENT)
                .setIdentifier(game.params.appStoreSkuFull));

        pmc.addOffer(new Offer()
                .setType(OfferType.ENTITLEMENT)
                .setIdentifier(game.params.appStoreSkuPart1));

        pmc.addOffer(new Offer()
                .setType(OfferType.ENTITLEMENT)
                .setIdentifier(game.params.appStoreSkuPart2));

        try {
            game.params.iap.install(observer, pmc, true);
        } catch (Throwable e) {
            Log.e(tag, e);
        }

        Timer.instance().scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                controlsFragment.fadeInRestore();
            }
        }, 1);
    }

    public void onPurchaseCompleted() {
        try {
            if (game.gameState.expansionInfo == null) {
                game.gameState.expansionInfo = new ExpansionResponse();
            }

            game.gameState.expansionInfo.isCompleted = false;
            game.gameState.expansionInfo.isDownloadStarted = false;

            game.databaseManager.persistSync(game.gameState);

            navigateTo(new LoadingScreen(game));
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
    }

    public void onCriticalError(Throwable e) {
        game.navigator.onCriticalError(e);
    }

    public void navigateTo(Screen screen) {

        game.navigator.navigateTo(screen);
    }
}
