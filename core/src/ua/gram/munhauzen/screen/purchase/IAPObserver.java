package ua.gram.munhauzen.screen.purchase;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.pay.Information;
import com.badlogic.gdx.pay.PurchaseObserver;
import com.badlogic.gdx.pay.Transaction;
import com.badlogic.gdx.utils.Timer;

import java.util.ArrayList;
import java.util.Arrays;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.entity.Product;
import ua.gram.munhauzen.entity.Purchase;
import ua.gram.munhauzen.screen.PurchaseScreen;
import ua.gram.munhauzen.utils.Log;

public class IAPObserver implements PurchaseObserver {

    public final String tag = getClass().getSimpleName();
    public final PurchaseScreen screen;
    public final MunhauzenGame game;

    public IAPObserver(PurchaseScreen screen) {
        this.screen = screen;
        this.game = screen.game;
    }

    @Override
    public void handleInstall() {
        Log.i(tag, "handleInstall");

        try {

            String[] ids = {
                    game.params.appStoreSkuFull,
                    game.params.appStoreSkuPart1,
                    game.params.appStoreSkuPart2,
                    game.params.appStoreSku1Chapter,
                    game.params.appStoreSku3Chapter,
                    game.params.appStoreSku5Chapter,
                    game.params.appStoreSku10Chapter,
                    game.params.appStoreSkuThanks,
                    game.params.appStoreSkuFullThanks,
            };

            game.gameState.purchaseState.products = new ArrayList<>();

            for (String id : ids) {
                Information info = game.params.iap.getInformation(id);

                Product product = new Product();
                product.id = id;
                product.localDescription = info.getLocalDescription();
                product.localPricing = info.getLocalPricing();
                product.localName = info.getLocalName();
                product.isAvailable = info != Information.UNAVAILABLE;

                game.gameState.purchaseState.products.add(product);
            }

            game.syncState();

        } catch (Throwable e) {
            Log.e(tag, e);
        }

        try {
            game.params.iap.purchaseRestore();
        } catch (Throwable e) {
            Log.e(tag, e);

            screen.onCriticalError(e);
        }
    }

    @Override
    public void handleInstallError(Throwable e) {
        Log.e(tag, e);
    }

    @Override
    public void handleRestore(Transaction[] transactions) {
        Log.e(tag, "handleRestore\n" + Arrays.toString(transactions));

        try {

            game.gameState.purchaseState.purchases = new ArrayList<>();

            if (!MunhauzenGame.developmentIgnorePurchaseRestore) {
                for (Transaction transaction : transactions) {
                    Purchase p = new Purchase();
                    p.orderId = transaction.getOrderId();
                    p.productId = transaction.getIdentifier();

                    game.gameState.purchaseState.purchases.add(p);
                }
            }

            game.purchaseManager.updatePurchaseState();

            game.syncState();

        } catch (Throwable e) {
            Log.e(tag, e);

            screen.onCriticalError(e);
        }

    }

    @Override
    public void handleRestoreError(Throwable e) {
        Log.e(tag, e);

        screen.destroyBanners();
    }

    @Override
    public void handlePurchase(Transaction transaction) {
        Log.i(tag, "handlePurchase\n" + transaction);

        try {

            if (game.gameState.purchaseState.purchases == null) {
                game.gameState.purchaseState.purchases = new ArrayList<>();
            }

            if (transaction != null) {
                Purchase p = new Purchase();
                p.orderId = transaction.getOrderId();
                p.productId = transaction.getIdentifier();

                game.gameState.purchaseState.purchases.add(p);
            }

            game.purchaseManager.updatePurchaseState();

            game.stopCurrentSfx();
            game.currentSfx = game.sfxService.onPurchaseSuccess();

            Gdx.input.setInputProcessor(null);
            GameState.clearTimer(tag);

            if (game.currentSfx != null) {
                Timer.instance().scheduleTask(new Timer.Task() {
                    @Override
                    public void run() {
                        try {
                            screen.onPurchaseCompleted();
                        } catch (Throwable e) {
                            Log.e(tag, e);
                        }
                    }
                }, game.currentSfx.duration / 1000f);
            } else {
                screen.onPurchaseCompleted();
            }


        } catch (Throwable e) {
            Log.e(tag, e);

            screen.onCriticalError(e);
        }
    }

    @Override
    public void handlePurchaseError(Throwable e) {
        Log.e(tag, e);

        screen.destroyBanners();
    }

    @Override
    public void handlePurchaseCanceled() {

        screen.destroyBanners();

    }
}
