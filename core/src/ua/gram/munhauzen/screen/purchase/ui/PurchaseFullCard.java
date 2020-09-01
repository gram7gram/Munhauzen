package ua.gram.munhauzen.screen.purchase.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

import ua.gram.munhauzen.FontProvider;
import ua.gram.munhauzen.entity.Purchase;
import ua.gram.munhauzen.entity.PurchaseState;
import ua.gram.munhauzen.screen.PurchaseScreen;
import ua.gram.munhauzen.ui.FixedImage;

public class PurchaseFullCard extends EntitlementCard {

    final Label discount;
    final FixedImage discountImg;
    final Container<Table> discountImgContainer;

    public PurchaseFullCard(final PurchaseScreen screen) {
        super(
                screen,
                screen.game.params.appStoreSkuFull,
                screen.game.t("purchase_screen.full_title"),
                screen.game.t("purchase_screen.full_description"),
                "purchase/full.png"
        );

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
    public boolean isPurchased() {
        PurchaseState state = screen.game.gameState.purchaseState;

        return state.isPro;
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        disableIfPart1AndPart2ArePurchased();
    }

    private void disableIfPart1AndPart2ArePurchased() {
        PurchaseState state = screen.game.gameState.purchaseState;

        Purchase part1 = null, part2 = null;
        for (Purchase purchase : state.purchases) {
            if (screen.game.params.appStoreSkuPart1.equals(purchase.productId)) {
                part1 = purchase;
            } else if (screen.game.params.appStoreSkuPart2.equals(purchase.productId)) {
                part2 = purchase;
            }
        }

        if (part1 != null && part2 != null) {
            setEnabled(false);
            setPriceText("");
        }
    }

    @Override
    public void updateSideIcon() {

        purchasedIconContainer.setVisible(purchased);
        discountImgContainer.setVisible(!purchased);

    }

}
