package ua.gram.munhauzen.screen.purchase.ui;

import ua.gram.munhauzen.screen.PurchaseScreen;

public class PurchaseThanksCard extends EntitlementCard {

    public PurchaseThanksCard(final PurchaseScreen screen) {
        super(
                screen,
                screen.game.params.appStoreSkuThanks,
                screen.game.t("purchase_screen.thx_title"),
                screen.game.t("purchase_screen.thx_description"),
                "purchase/thx.png"
        );

    }


}
