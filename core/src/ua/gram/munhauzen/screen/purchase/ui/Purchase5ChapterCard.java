package ua.gram.munhauzen.screen.purchase.ui;

import ua.gram.munhauzen.screen.PurchaseScreen;

public class Purchase5ChapterCard extends ConsumableCard {

    public Purchase5ChapterCard(final PurchaseScreen screen) {
        super(
                screen,
                screen.game.params.appStoreSku5Chapter,
                screen.game.t("purchase_screen.chap5_title"),
                screen.game.t("purchase_screen.chap5_description"),
                "purchase/chap5.png"
        );

    }

    @Override
    public void act(float delta) {
        super.act(delta);

        setEnabled(!screen.game.gameState.purchaseState.isPro);
    }


}
