package ua.gram.munhauzen.screen.purchase.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;

import ua.gram.munhauzen.FontProvider;
import ua.gram.munhauzen.entity.Product;
import ua.gram.munhauzen.entity.PurchaseState;
import ua.gram.munhauzen.screen.PurchaseScreen;

public abstract class PurchaseCard extends Card {

    final String title, description, image;

    public PurchaseCard(final PurchaseScreen screen, String id, String title, String description, String image) {
        super(screen, id);

        this.title = title;
        this.description = description;
        this.image = image;

        build();
    }


    @Override
    public void act(float delta) {
        super.act(delta);

        if (productId == null) return;

        PurchaseState state = screen.game.gameState.purchaseState;
        if (state != null) {

            if (state.products != null) {

                Product product = null;
                for (Product p : state.products) {
                    if (productId.equals(p.id)) {
                        product = p;
                        break;
                    }
                }

                if (product != null) {
                    updateCardText(product);
                    return;
                }
            }
        }

        setEnabled(false);
        setPriceText(screen.game.t("purchase_screen.unavailable"));

    }

    @Override
    public Label[] createSentences() {
        String[] lines = description.split("\n");

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
        return new Label(title, new Label.LabelStyle(
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
        return screen.game.internalAssetManager.get(image, Texture.class);
    }
}
