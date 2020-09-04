package ua.gram.munhauzen.service;

import com.badlogic.gdx.pay.Offer;
import com.badlogic.gdx.pay.OfferType;
import com.badlogic.gdx.pay.PurchaseManagerConfig;
import com.badlogic.gdx.pay.PurchaseObserver;
import com.badlogic.gdx.pay.Transaction;
import com.badlogic.gdx.utils.Timer;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.Device;
import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.entity.Purchase;
import ua.gram.munhauzen.screen.PurchaseScreen;
import ua.gram.munhauzen.utils.Log;

public class PurchaseManager {

    public final String tag = getClass().getSimpleName();
    public final MunhauzenGame game;

    public static final int demoEndsAtChapter = 5, part1EndsAtChapter = 36, maxChapter = 68;

    public PurchaseManager(MunhauzenGame game) {
        this.game = game;
    }

    public boolean hasPurchases() {
        return game.gameState.purchaseState.purchases != null
                && game.gameState.purchaseState.purchases.size() > 0;
    }

    public void purchase(final String productId) {

        Log.i(tag, "purchase " + productId);

        if (MunhauzenGame.developmentSimulatePurchase) {
            Transaction transaction = new Transaction();
            transaction.setOrderId("test");
            transaction.setIdentifier(productId);

            ((PurchaseScreen) game.getScreen()).observer.handlePurchase(
                    transaction
            );
            return;
        }

        if (game.getScreen() instanceof PurchaseScreen) {
            ((PurchaseScreen) game.getScreen()).openPurchasePendingBanner();

            Timer.instance().scheduleTask(new Timer.Task() {
                @Override
                public void run() {
                    game.params.iap.purchase(productId);
                }
            }, .4f);

        } else {
            game.params.iap.purchase(productId);
        }
    }

    public void updatePurchaseState() {

        GameState gameState = game.gameState;

        int purchasedChapters = 0;
        String expansionVersion;

//        gameState.purchaseState = new PurchaseState();
//        gameState.purchaseState.referrals.add(game.params.appStoreSku10Chapter);
//        gameState.purchaseState.isVersionSelected = true;

        for (String code : gameState.purchaseState.referrals) {
            if (game.params.appStoreSku3Chapter.equals(code)) {
                purchasedChapters += 3;
            }

            if (game.params.appStoreSku10Chapter.equals(code)) {
                purchasedChapters += 10;
            }
        }

        for (String code : gameState.purchaseState.promocodes) {
            if (game.params.promocode1.equals(code)) {
                purchasedChapters += 3;
            }
            if (game.params.promocode2.equals(code)) {
                purchasedChapters += 5;
            }
        }

        for (Purchase purchase : gameState.purchaseState.purchases) {

            if (game.params.appStoreSku1Chapter.equals(purchase.productId)) {
                purchasedChapters += 1;
            }

            if (game.params.appStoreSku3Chapter.equals(purchase.productId)) {
                purchasedChapters += 3;
            }

            if (game.params.appStoreSku5Chapter.equals(purchase.productId)) {
                purchasedChapters += 5;
            }

            if (game.params.appStoreSku10Chapter.equals(purchase.productId)) {
                purchasedChapters += 10;
            }
        }

        int availableChapter = demoEndsAtChapter;

        for (Purchase purchase : gameState.purchaseState.purchases) {

            if (game.params.appStoreSkuPart1.equals(purchase.productId)) {
                availableChapter = part1EndsAtChapter;
                break;
            }

        }

        availableChapter += purchasedChapters;

        if (part1EndsAtChapter < availableChapter) {
            expansionVersion = "Part_2";
        } else if (demoEndsAtChapter < availableChapter) {
            expansionVersion = "Part_1";
        } else {
            expansionVersion = "Part_demo";
        }

        gameState.purchaseState.setPro(game.params);

        if (gameState.purchaseState.isPro) {
            availableChapter = maxChapter;
            expansionVersion = "Part_2";
        }

        gameState.purchaseState.maxChapter = availableChapter;
        gameState.purchaseState.currentExpansionVersion = expansionVersion;

        Log.i(tag, "availableChapter=" + availableChapter + " " + expansionVersion);
    }

    public void start(PurchaseObserver observer) {
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

        pmc.addOffer(new Offer()
                .setType(OfferType.CONSUMABLE)
                .setIdentifier(game.params.appStoreSku5Chapter));

        pmc.addOffer(new Offer()
                .setType(OfferType.CONSUMABLE)
                .setIdentifier(game.params.appStoreSku10Chapter));

        pmc.addOffer(new Offer()
                .setType(OfferType.ENTITLEMENT)
                .setIdentifier(game.params.appStoreSkuThanks));

        pmc.addOffer(new Offer()
                .setType(OfferType.ENTITLEMENT)
                .setIdentifier(game.params.appStoreSkuFullThanks));

        if (game.params.device.type == Device.Type.android) {

            pmc.addOffer(new Offer()
                    .setType(OfferType.CONSUMABLE)
                    .setIdentifier(game.params.appStoreSku1Chapter));

            pmc.addOffer(new Offer()
                    .setType(OfferType.CONSUMABLE)
                    .setIdentifier(game.params.appStoreSku3Chapter));
        }

        try {
            game.params.iap.install(observer, pmc, true);
        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }
}
