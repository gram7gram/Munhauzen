package ua.gram.munhauzen.screen.purchase;

import com.badlogic.gdx.pay.Information;
import com.badlogic.gdx.pay.PurchaseObserver;
import com.badlogic.gdx.pay.Transaction;

import java.util.ArrayList;
import java.util.Arrays;

import ua.gram.munhauzen.MunhauzenGame;
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

            try {
                game.params.iap.purchaseRestore();
            } catch (Throwable e) {
                Log.e(tag, e);
            }

        } catch (Throwable ignore) {
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

            for (Transaction transaction : transactions) {
                Purchase p = new Purchase();
                p.orderId = transaction.getOrderId();
                p.productId = transaction.getIdentifier();

                game.gameState.purchaseState.purchases.add(p);
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
            Purchase purchase = null;
            if (transaction != null) {
                purchase = new Purchase();
                purchase.orderId = transaction.getOrderId();
                purchase.productId = transaction.getIdentifier();
            }

            game.purchaseManager.purchaseSuccess(purchase, new Runnable() {
                @Override
                public void run() {
                    screen.onPurchaseCompleted();
                }
            });

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
