package ua.gram.munhauzen.screen.purchase.ui;

import ua.gram.munhauzen.entity.Purchase;
import ua.gram.munhauzen.entity.PurchaseState;
import ua.gram.munhauzen.screen.PurchaseScreen;

public class PurchaseFullThanksCard extends EntitlementCard {

    public PurchaseFullThanksCard(final PurchaseScreen screen) {
        super(
                screen,
                screen.game.params.appStoreSkuFullThanks,
                screen.game.t("purchase_screen.full_thx_title"),
                screen.game.t("purchase_screen.full_thx_description"),
                "purchase/full_thx.png"
        );

    }

    @Override
    public void act(float delta) {
        super.act(delta);

        disableIfPart1AndPart2ArePurchased();
    }

    private void disableIfPart1AndPart2ArePurchased() {
        PurchaseState state = screen.game.gameState.purchaseState;

        Purchase part1 = null, part2 = null;
        for (Purchase purchase : state.purchases) {
            if (screen.game.params.appStoreSkuPart1.equals(purchase.productId)) {
                part1 = purchase;
            } else if (screen.game.params.appStoreSkuPart2.equals(purchase.productId)) {
                part2 = purchase;
            }
        }

        if (part1 != null && part2 != null) {
            setEnabled(false);
            setPriceText("");
        }
    }

}
