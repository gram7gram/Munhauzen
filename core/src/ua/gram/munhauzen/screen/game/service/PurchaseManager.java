package ua.gram.munhauzen.screen.game.service;

import ua.gram.munhauzen.entity.Purchase;
import ua.gram.munhauzen.entity.PurchaseState;
import ua.gram.munhauzen.screen.GameScreen;

public class PurchaseManager {

    public final GameScreen screen;

    public PurchaseManager(GameScreen screen) {
        this.screen = screen;
    }

    public boolean isExpansionPurchased(String expansion) {
        if (expansion == null || expansion.equals("Part_demo")) return true;

        PurchaseState state = screen.game.gameState.purchaseState;

        if (state == null || state.purchases == null) return false;

        if (state.isPro) return true;

        Purchase fullPurchase = null, part2Purchase = null, part1Purchase = null;

        for (Purchase purchase : state.purchases) {
            if (purchase.productId.equals(screen.game.params.appStoreSkuFull)) {
                fullPurchase = purchase;
            }

            if (purchase.productId.equals(screen.game.params.appStoreSkuPart2)) {
                part2Purchase = purchase;
            }

            if (purchase.productId.equals(screen.game.params.appStoreSkuPart1)) {
                part1Purchase = purchase;
            }
        }

        if (expansion.equals("Part_1")) {

            return fullPurchase != null || part1Purchase != null;

        }

        if (expansion.equals("Part_2")) {

            return fullPurchase != null || part2Purchase != null;

        }


        return false;
    }
}
