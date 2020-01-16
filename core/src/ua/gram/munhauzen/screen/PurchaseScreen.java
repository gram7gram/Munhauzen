package ua.gram.munhauzen.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.pay.Information;
import com.badlogic.gdx.pay.Offer;
import com.badlogic.gdx.pay.OfferType;
import com.badlogic.gdx.pay.PurchaseManagerConfig;
import com.badlogic.gdx.pay.PurchaseObserver;
import com.badlogic.gdx.pay.Transaction;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Arrays;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.Product;
import ua.gram.munhauzen.entity.Purchase;
import ua.gram.munhauzen.expansion.response.ExpansionResponse;
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

    public PurchaseScreen(MunhauzenGame game) {
        this.game = game;
    }

    @Override
    public void show() {

        ui = new MunhauzenStage(game);

        background = game.internalAssetManager.get("p0.jpg", Texture.class);

        game.internalAssetManager.load("bg3.jpg", Texture.class);
        game.internalAssetManager.load("purchase/full.png", Texture.class);
        game.internalAssetManager.load("purchase/part1.png", Texture.class);
        game.internalAssetManager.load("purchase/part2.png", Texture.class);
        game.internalAssetManager.load("purchase/free.png", Texture.class);
        game.internalAssetManager.load("purchase/ok.png", Texture.class);
        game.internalAssetManager.load("purchase/off.png", Texture.class);
        game.internalAssetManager.load("purchase/b_menu.png", Texture.class);

        game.internalAssetManager.finishLoading();

        Gdx.input.setInputProcessor(ui);

        fragment = new ListFragment(this);
        fragment.create();

        ui.addActor(fragment.getRoot());

        controlsFragment = new ControlsFragment(PurchaseScreen.this);
        controlsFragment.create();

        ui.addActor(controlsFragment.getRoot());

        fragment.fadeIn();

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
            game.params.iap.install(new PurchaseObserver() {
                @Override
                public void handleInstall() {
                    Log.i(tag, "handleInstall");
                    try {

                        Information fullInfo = game.params.iap.getInformation(game.params.appStoreSkuFull);
                        Information part1Info = game.params.iap.getInformation(game.params.appStoreSkuPart1);
                        Information part2Info = game.params.iap.getInformation(game.params.appStoreSkuPart2);

                        Product full = new Product();
                        full.localDescription = fullInfo.getLocalDescription();
                        full.localPricing = fullInfo.getLocalPricing();
                        full.localName = fullInfo.getLocalName();

                        Product part1 = new Product();
                        part1.localDescription = part1Info.getLocalDescription();
                        part1.localPricing = part1Info.getLocalPricing();
                        part1.localName = part1Info.getLocalName();

                        Product part2 = new Product();
                        part2.localDescription = part2Info.getLocalDescription();
                        part2.localPricing = part2Info.getLocalPricing();
                        part2.localName = part2Info.getLocalName();

                        game.gameState.purchaseState.products = new ArrayList<>();

                        ObjectMapper om = new ObjectMapper();

                        if (part1Info != Information.UNAVAILABLE) {
                            game.gameState.purchaseState.products.add(part1);
                            fragment.card2.price.setText(part1.localPricing);
                        } else {
                            fragment.card2.price.setText(game.t("purchase_screen.unavailable"));
                            fragment.card2.setEnabled(false);
                        }

                        if (part2Info != Information.UNAVAILABLE) {
                            game.gameState.purchaseState.products.add(part2);
                            fragment.card3.price.setText(part2.localPricing);
                        } else {
                            fragment.card3.price.setText(game.t("purchase_screen.unavailable"));
                            fragment.card3.setEnabled(false);
                        }

                        if (fullInfo != Information.UNAVAILABLE) {
                            game.gameState.purchaseState.products.add(full);
                            fragment.card4.price.setText(full.localPricing);
                        } else {
                            fragment.card4.price.setText(game.t("purchase_screen.unavailable"));
                            fragment.card4.setEnabled(false);
                        }

                        Log.e(tag, "products\n" + om.writeValueAsString(game.gameState.purchaseState.products));

                        game.databaseManager.persistSync(game.gameState);

                    } catch (Throwable e) {
                        Log.e(tag, e);
                    }

                    try {
                        game.params.iap.purchaseRestore();
                    } catch (Throwable e) {
                        Log.e(tag, e);

                        onCriticalError(e);
                    }
                }

                @Override
                public void handleInstallError(Throwable e) {
                    Log.e(tag, e);
                }

                @Override
                public void handleRestore(Transaction[] transactions) {
                    Log.i(tag, "handleRestore\n" + Arrays.toString(transactions));

                    try {

                        if (game.gameState.purchaseState.purchases == null) {
                            game.gameState.purchaseState.purchases = new ArrayList<>();
                        }

                        for (Transaction transaction : transactions) {
                            Purchase p = new Purchase();
                            p.orderId = transaction.getOrderId();
                            p.productId = transaction.getIdentifier();

                            boolean contains = false;
                            for (Purchase purchase : game.gameState.purchaseState.purchases) {
                                if (purchase.orderId.equals(p.orderId) && p.productId.equals(purchase.productId)) {
                                    contains = true;
                                    break;
                                }
                            }

                            if (!contains)
                                game.gameState.purchaseState.purchases.add(p);
                        }

                        setPro(game.gameState.purchaseState.purchases);

                        game.databaseManager.persistSync(game.gameState);

                        for (Purchase transaction : game.gameState.purchaseState.purchases) {
                            if (transaction.productId.equals(game.params.appStoreSkuFull)) {
                                fragment.card4.price.setText(game.t("purchase_screen.already_purchased"));
                                fragment.card4.setPurchased(true);
                            }

                            if (transaction.productId.equals(game.params.appStoreSkuPart1)) {
                                fragment.card2.price.setText(game.t("purchase_screen.already_purchased"));
                                fragment.card2.setPurchased(true);
                            }

                            if (transaction.productId.equals(game.params.appStoreSkuPart2)) {
                                fragment.card3.price.setText(game.t("purchase_screen.already_purchased"));
                                fragment.card3.setPurchased(true);
                            }
                        }

                    } catch (Throwable e) {
                        Log.e(tag, e);

                        onCriticalError(e);
                    }

                }

                @Override
                public void handleRestoreError(Throwable e) {
                    Log.e(tag, e);
                }

                @Override
                public void handlePurchase(Transaction transaction) {
                    Log.i(tag, "handlePurchase\n" + transaction);

                    try {

                        if (game.gameState.purchaseState.purchases == null) {
                            game.gameState.purchaseState.purchases = new ArrayList<>();
                        }

                        Purchase p = new Purchase();
                        p.orderId = transaction.getOrderId();
                        p.productId = transaction.getIdentifier();

                        game.gameState.purchaseState.purchases.add(p);

                        setPro(game.gameState.purchaseState.purchases);

                        onPurchaseCompleted();
                    } catch (Throwable e) {
                        Log.e(tag, e);

                        onCriticalError(e);
                    }
                }

                @Override
                public void handlePurchaseError(Throwable e) {
                    Log.e(tag, e);
                }

                @Override
                public void handlePurchaseCanceled() {

                }
            }, pmc, true);
        } catch (Throwable e) {
            Log.e(tag, e);
        }

    }

    public void onPurchaseCompleted() {
        try {
            if (game.gameState.expansionInfo == null) {
                game.gameState.expansionInfo = new ExpansionResponse();
            }

            game.gameState.expansionInfo.isCompleted = false;

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
            controlsFragment.root.setVisible(game.gameState.expansionInfo != null
                    && game.gameState.expansionInfo.isCompleted);
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

        game.internalAssetManager.unload("bg3.jpg");
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

    public void setPro(ArrayList<Purchase> purchases) {

        game.gameState.purchaseState.isPro = false;

        if (purchases != null) {

            boolean hasFull = false, hasPart1 = false, hasPart2 = false;

            for (Purchase purchase : purchases) {
                if (purchase.productId.equals(game.params.appStoreSkuFull)) {
                    hasFull = true;
                }

                if (purchase.productId.equals(game.params.appStoreSkuPart1)) {
                    hasPart1 = true;
                }

                if (purchase.productId.equals(game.params.appStoreSkuPart2)) {
                    hasPart2 = true;
                }
            }

            game.gameState.purchaseState.isPro = hasFull || (hasPart1 && hasPart2);

        }
    }
}
