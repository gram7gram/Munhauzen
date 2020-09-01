package ua.gram.munhauzen.screen.purchase.ui;

import ua.gram.munhauzen.screen.PurchaseScreen;

public class Purchase10ChapterCard extends ConsumableCard {

    public Purchase10ChapterCard(final PurchaseScreen screen) {
        super(
                screen,
                screen.game.params.appStoreSku10Chapter,
                screen.game.t("purchase_screen.chap10_title"),
                screen.game.t("purchase_screen.chap10_description"),
                "purchase/chap10.png"
        );

    }

    @Override
    public void act(float delta) {
        super.act(delta);

        setEnabled(!screen.game.gameState.purchaseState.isPro);
    }


}
