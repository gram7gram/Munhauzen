package ua.gram.munhauzen.screen.purchase.ui;

import ua.gram.munhauzen.screen.PurchaseScreen;

public class PurchasePart2Card extends EntitlementCard {

    public PurchasePart2Card(final PurchaseScreen screen) {
        super(
                screen,
                screen.game.params.appStoreSkuPart2,
                screen.game.t("purchase_screen.part2_title"),
                screen.game.t("purchase_screen.part2_description"),
                "purchase/part2.png"
        );

    }

}
