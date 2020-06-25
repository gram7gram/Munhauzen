package ua.gram.munhauzen.screen.purchase.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;

import ua.gram.munhauzen.FontProvider;
import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.screen.PurchaseScreen;
import ua.gram.munhauzen.ui.FixedImage;
import ua.gram.munhauzen.utils.Log;

public abstract class Card extends Stack {

    protected final String tag = getClass().getSimpleName();
    public final PurchaseScreen screen;
    public final FixedImage purchasedIcon;
    public final Image img;
    public final Label price;
    public final Table root, content;
    public boolean enabled, purchased;
    public final Container<Table> container;
    final Container<Table> purchasedIconContainer;
    public final float maxWidth;

    public Card(PurchaseScreen s) {
        this.screen = s;

        maxWidth = MunhauzenGame.WORLD_WIDTH - 10 * 6;

        root = new Table();

        content = new Table();

        Texture cardBg = screen.game.internalAssetManager.get("bg3.png", Texture.class);

        Texture bg = getImage();

        float imgWidth = maxWidth * .35f;
        float imgHeight = bg.getHeight() * (imgWidth / bg.getWidth());

        img = new FixedImage(bg, maxWidth * .35f);

        Texture iconTxt = screen.game.internalAssetManager.get("purchase/ok.png", Texture.class);

        purchasedIcon = new FixedImage(iconTxt, maxWidth * .12f);

        Label title = createTitle();
        title.setWrap(true);
        title.setAlignment(Align.left);

        content.add(title).left().expandX().row();

        for (Label sentence : createSentences()) {
            sentence.setWrap(true);
            sentence.setAlignment(Align.left);
            content.add(sentence).left().expandX().row();
        }

        price = createPrice();
        price.setWrap(true);
        price.setAlignment(Align.left);

        content.add(price).expandX().align(Align.bottomLeft);
        content.row();

        root.pad(10);
        root.add(img).width(imgWidth).height(imgHeight).center();
        root.add(content).width(maxWidth * .65f).center().padLeft(10);

        root.setBackground(new SpriteDrawable(new Sprite(cardBg)));


        ClickListener listener1 = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                try {
                    if (screen.game.sfxService == null) return;

                    if (!enabled) {
                        screen.game.sfxService.onAnyDisabledBtnClicked();
                    } else {
                        screen.game.sfxService.onAnyBtnClicked();
                    }
                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }
        };

        ClickListener listener2 = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                try {

                    setPriceText(screen.game.t("purchase_screen.processing"));

                    if (purchased) {
                        event.cancel();

                        screen.onPurchaseCompleted();
                    }
                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }
        };

        root.addCaptureListener(listener1);
        root.addCaptureListener(listener2);

        container = new Container<>(root);
        container.align(Align.top);
        container.padBottom(20);

        addActor(container);

        Table purchasedIconTable = new Table();
        purchasedIconTable.add(purchasedIcon)
                .width(purchasedIcon.width)
                .height(purchasedIcon.height);

        purchasedIconContainer = new Container<>(purchasedIconTable);
        purchasedIconContainer.setFillParent(true);
        purchasedIconContainer.pad(10);
        purchasedIconContainer.align(Align.bottomRight);
        purchasedIconContainer.setVisible(false);
        purchasedIconContainer.padBottom(30);
        purchasedIconContainer.setTouchable(Touchable.disabled);

        addActor(purchasedIconContainer);

        setEnabled(true);
    }

    public void onClick(ClickListener listener) {
        root.addListener(listener);
    }

    public void updateSideIcon() {

        purchasedIconContainer.setVisible(purchased);

    }

    public void setPurchased(boolean purchased) {
        this.purchased = purchased;

        updateSideIcon();
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        root.setTouchable(enabled ? Touchable.enabled : Touchable.disabled);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        getColor().a = enabled ? 1 : .5f;
    }

    public abstract Label createTitle();

    public abstract Label createPrice();

    public abstract Label[] createSentences();

    public abstract Texture getImage();

    public Label.LabelStyle getPriceNumberStyle() {
        return new Label.LabelStyle(
                screen.game.fontProvider.getFont(FontProvider.DroidSansMono, FontProvider.h4),
                Color.BLACK
        );
    }

    public Label.LabelStyle getPriceTextStyle() {
        return new Label.LabelStyle(
                screen.game.fontProvider.getFont(FontProvider.BuxtonSketch, FontProvider.h3),
                Color.BLACK
        );
    }

    public void setPriceNumber(String value) {
        price.setStyle(getPriceNumberStyle());
        price.setText(value);
    }

    public void setPriceText(String value) {
        price.setStyle(getPriceTextStyle());
        price.setText(value);
    }
}
