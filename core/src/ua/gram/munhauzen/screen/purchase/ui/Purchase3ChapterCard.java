package ua.gram.munhauzen.screen.purchase.ui;

import ua.gram.munhauzen.screen.PurchaseScreen;

public class Purchase3ChapterCard extends ConsumableCard {

    public Purchase3ChapterCard(final PurchaseScreen screen) {
        super(
                screen,
                screen.game.params.appStoreSku3Chapter,
                screen.game.t("purchase_screen.chap3_title"),
                screen.game.t("purchase_screen.chap3_description"),
                "purchase/chap3.png"
        );

    }

    @Override
    public void act(float delta) {
        super.act(delta);

        setEnabled(!screen.game.gameState.purchaseState.isPro);
    }


}
