package ua.gram.munhauzen.screen.purchase.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import ua.gram.munhauzen.FontProvider;
import ua.gram.munhauzen.entity.PurchaseState;
import ua.gram.munhauzen.screen.PurchaseScreen;
import ua.gram.munhauzen.ui.FixedImage;
import ua.gram.munhauzen.utils.Log;

public class PurchaseFullCard extends Card {

    final Label discount;
    final FixedImage discountImg;
    final Container<Table> discountImgContainer;

    public PurchaseFullCard(final PurchaseScreen screen) {
        super(screen);

        onClick(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                try {

                    final String id = screen.game.params.appStoreSkuFull;
                    Log.i(tag, "clicked on " + id);

                    PurchaseState state = screen.game.gameState.purchaseState;
                    if (state.isPro) {
                        screen.onPurchaseCompleted();
                        return;
                    }

                    screen.openAdultGateBanner(new Runnable() {
                        @Override
                        public void run() {
                            screen.game.params.iap.purchase(id);
                        }
                    });
                } catch (Throwable e) {
                    Log.e(tag, e);

                    screen.onCriticalError(e);
                }
            }
        });

        discount = new Label(screen.game.t("purchase_screen.full_discount"), new Label.LabelStyle(
                screen.game.fontProvider.getFont(FontProvider.BuxtonSketch, FontProvider.h2),
                Color.RED
        ));
        discount.getColor().a = .5f;

        Group discountTable = new Group();
        discountTable.addActor(discount);
        discountTable.setOrigin(Align.center);
        discountTable.setRotation(15);

        Container<Group> discountContainer = new Container<>(discountTable);
        discountContainer.setFillParent(true);
        discountContainer.pad(maxWidth * .3f, 100, 30, 10);
        discountContainer.align(Align.center);
        discountContainer.setTouchable(Touchable.disabled);

        addActor(discountContainer);

        Texture txt = screen.game.internalAssetManager.get("purchase/off.png", Texture.class);
        discountImg = new FixedImage(txt, maxWidth * .2f);

        Table discountImgTable = new Table();
        discountImgTable.add(discountImg)
                .width(discountImg.width)
                .height(discountImg.height);

        discountImgContainer = new Container<>(discountImgTable);
        discountImgContainer.setFillParent(true);
        discountImgContainer.pad(10);
        discountImgContainer.align(Align.bottomRight);
        discountImgContainer.padBottom(30);
        discountImgContainer.setTouchable(Touchable.disabled);

        addActor(discountImgContainer);
    }

    @Override
    public void updateSideIcon() {

        purchasedIconContainer.setVisible(purchased);
        discountImgContainer.setVisible(!purchased);

    }

    @Override
    public Label[] createSentences() {
        String[] lines = screen.game.t("purchase_screen.full_description").split("\n");

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
        return new Label(screen.game.t("purchase_screen.full_title"), new Label.LabelStyle(
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
        return screen.game.internalAssetManager.get("purchase/full.png", Texture.class);
    }
}
