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

            Information fullInfo = game.params.iap.getInformation(game.params.appStoreSkuFull);
            Information part1Info = game.params.iap.getInformation(game.params.appStoreSkuPart1);
            Information part2Info = game.params.iap.getInformation(game.params.appStoreSkuPart2);

            Product full = new Product();
            full.id = game.params.appStoreSkuFull;
            full.localDescription = fullInfo.getLocalDescription();
            full.localPricing = fullInfo.getLocalPricing();
            full.localName = fullInfo.getLocalName();
            full.isAvailable = fullInfo != Information.UNAVAILABLE;

            Product part1 = new Product();
            part1.id = game.params.appStoreSkuPart1;
            part1.localDescription = part1Info.getLocalDescription();
            part1.localPricing = part1Info.getLocalPricing();
            part1.localName = part1Info.getLocalName();
            part1.isAvailable = part1Info != Information.UNAVAILABLE;

            Product part2 = new Product();
            part2.id = game.params.appStoreSkuPart2;
            part2.localDescription = part2Info.getLocalDescription();
            part2.localPricing = part2Info.getLocalPricing();
            part2.localName = part2Info.getLocalName();
            part2.isAvailable = part2Info != Information.UNAVAILABLE;

            game.gameState.purchaseState.products = new ArrayList<>();
            game.gameState.purchaseState.products.add(part1);
            game.gameState.purchaseState.products.add(part2);
            game.gameState.purchaseState.products.add(full);

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
        Log.i(tag, "handleRestore\n" + Arrays.toString(transactions));

        try {

            game.gameState.purchaseState.purchases = new ArrayList<>();

            for (Transaction transaction : transactions) {
                Purchase p = new Purchase();
                p.orderId = transaction.getOrderId();
                p.productId = transaction.getIdentifier();

                game.gameState.purchaseState.purchases.add(p);
            }

            game.gameState.purchaseState.setPro(game.params);

            game.syncState();

        } catch (Throwable e) {
            Log.e(tag, e);

            screen.onCriticalError(e);
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

            game.gameState.purchaseState.setPro(game.params);

            game.stopCurrentSfx();
            if (game.gameState.purchaseState.purchases.size() > 0) {
                game.currentSfx = game.sfxService.onPurchaseSuccess();
            }

            if (game.currentSfx != null) {
                Gdx.input.setInputProcessor(null);
                GameState.clearTimer(tag);
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
    }

    @Override
    public void handlePurchaseCanceled() {

    }
}
