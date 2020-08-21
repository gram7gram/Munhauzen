package ua.gram.munhauzen.screen.purchase.ui;

import ua.gram.munhauzen.screen.PurchaseScreen;

public class Purchase1ChapterCard extends ConsumableCard {

    public Purchase1ChapterCard(final PurchaseScreen screen) {
        super(
                screen,
                screen.game.params.appStoreSku1Chapter,
                screen.game.t("purchase_screen.chap1_title"),
                screen.game.t("purchase_screen.chap1_description"),
                "purchase/chap1.png"
        );

    }


}
