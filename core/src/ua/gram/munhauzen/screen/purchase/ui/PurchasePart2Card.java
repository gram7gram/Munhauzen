package ua.gram.munhauzen.screen.purchase.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import ua.gram.munhauzen.FontProvider;
import ua.gram.munhauzen.screen.PurchaseScreen;
import ua.gram.munhauzen.utils.ColorUtils;
import ua.gram.munhauzen.utils.Log;

public class PurchasePart2Card extends Card {

    public PurchasePart2Card(final PurchaseScreen screen) {
        super(screen);

        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                Log.i(tag, "clicked on " + screen.game.params.appStoreSkuPart2);

                screen.game.params.iap.purchase(screen.game.params.appStoreSkuPart2);
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
                    ColorUtils.dark
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
                ColorUtils.yellowLight
        ));
    }

    @Override
    public Label createPrice() {
        return new Label("", new Label.LabelStyle(
                screen.game.fontProvider.getFont(FontProvider.BuxtonSketch, FontProvider.h3),
                ColorUtils.yellowLight
        ));
    }

    @Override
    public Texture getImage() {
        return screen.game.internalAssetManager.get("purchase/part2.jpg", Texture.class);
    }
}
