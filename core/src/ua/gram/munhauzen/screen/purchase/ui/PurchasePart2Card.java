package ua.gram.munhauzen.screen.purchase.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import ua.gram.munhauzen.FontProvider;
import ua.gram.munhauzen.entity.Purchase;
import ua.gram.munhauzen.entity.PurchaseState;
import ua.gram.munhauzen.screen.PurchaseScreen;
import ua.gram.munhauzen.utils.Log;

public class PurchasePart2Card extends Card {

    public PurchasePart2Card(final PurchaseScreen screen) {
        super(screen);

        onClick(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                try {
                    final String id = screen.game.params.appStoreSkuPart2;
                    Log.i(tag, "clicked on " + id);

                    PurchaseState state = screen.game.gameState.purchaseState;
                    if (state.purchases != null) {

                        for (Purchase purchase : state.purchases) {
                            if (purchase.productId.equals(id)) {
                                screen.onPurchaseCompleted();
                                return;

                            }
                        }

                    }

                    screen.openAdultGateBanner(new Runnable() {
                        @Override
                        public void run() {
                            screen.game.params.iap.purchase(id);
                        }
                    });                } catch (Throwable e) {
                    Log.e(tag, e);

                    screen.onCriticalError(e);
                }
            }
        });
    }

    @Override
    public Label[] createSentences() {
        String[] lines = screen.game.t("purchase_screen.part2_description").split("\n");

        Label[] sentences = new Label[lines.length];
        for (int i = 0; i < lines.length; i++) {
            Label label = new Label(lines[i], new Label.LabelStyle(
                    screen.game.fontProvider.getFont(FontProvider.h5),
                    Color.BLACK
            ));
            label.setWrap(true);
            label.setAlignment(Align.left);

            sentences[i] = label;
        }

        return sentences;
    }

    @Override
    public Label createTitle() {
        return new Label(screen.game.t("purchase_screen.part2_title"), new Label.LabelStyle(
                screen.game.fontProvider.getFont(FontProvider.h3),
                Color.BLACK
        ));
    }

    @Override
    public Label createPrice() {
        return new Label("", getPriceTextStyle());
    }

    @Override
    public Texture getImage() {
        return screen.game.internalAssetManager.get("purchase/part2.png", Texture.class);
    }
}
