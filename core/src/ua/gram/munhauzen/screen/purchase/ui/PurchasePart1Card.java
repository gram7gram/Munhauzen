package ua.gram.munhauzen.screen.purchase.ui;

import ua.gram.munhauzen.screen.PurchaseScreen;

public class PurchasePart1Card extends EntitlementCard {

    public PurchasePart1Card(final PurchaseScreen screen) {
        super(
                screen,
                screen.game.params.appStoreSkuPart1,
                screen.game.t("purchase_screen.part1_title"),
                screen.game.t("purchase_screen.part1_description"),
                "purchase/part1.png"
        );
    }
}
