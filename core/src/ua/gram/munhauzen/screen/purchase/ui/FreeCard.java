package ua.gram.munhauzen.screen.purchase.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import ua.gram.munhauzen.FontProvider;
import ua.gram.munhauzen.screen.LoadingScreen;
import ua.gram.munhauzen.screen.PurchaseScreen;
import ua.gram.munhauzen.utils.Log;

public class FreeCard extends Card {

    public FreeCard(final PurchaseScreen screen) {
        super(screen);

        onClick(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                Log.i(tag, "clicked on free");

                try {
                    screen.game.gameState.purchaseState.isVersionSelected = true;

                    screen.navigateTo(new LoadingScreen(screen.game));
                } catch (Throwable e) {
                    Log.e(tag, e);

                    screen.onCriticalError(e);
                }
            }
        });
    }

    @Override
    public Label[] createSentences() {
        String[] lines = screen.game.t("purchase_screen.free_description").split("\n");

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
        return new Label(screen.game.t("purchase_screen.free_title"), new Label.LabelStyle(
                screen.game.fontProvider.getFont(FontProvider.h3),
                Color.BLACK
        ));
    }

    @Override
    public Label createPrice() {
        return new Label(screen.game.t("purchase_screen.free_price"), getPriceTextStyle());
    }

    @Override
    public Texture getImage() {
        return screen.game.internalAssetManager.get("purchase/free.png", Texture.class);
    }


}
