package ua.gram.munhauzen.screen.purchase.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.screen.PurchaseScreen;

public abstract class Card extends Container<Table> {

    protected final String tag = getClass().getSimpleName();
    public final PurchaseScreen screen;
    public final Image okImg;
    public final Label price;
    public final Table root, content;
    public boolean enabled, purchased;

    public Card(PurchaseScreen s) {
        this.screen = s;

        float maxWidth = MunhauzenGame.WORLD_WIDTH - 10 * 6;

        root = new Table();

        content = new Table();

        Texture cardBg = screen.game.internalAssetManager.get("bg3.jpg", Texture.class);
        Texture okBg = screen.game.internalAssetManager.get("purchase/ok.png", Texture.class);
        Texture bg = getImage();

        float imgWidth = maxWidth * .35f;
        float imgHeight = bg.getHeight() * (imgWidth / bg.getWidth());

        float okWidth = maxWidth * .1f;
        float okHeight = okBg.getHeight() * (okWidth / okBg.getWidth());

        Image img = new Image(bg);
        okImg = new Image(okBg);
        okImg.setVisible(false);

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
        content.add(okImg).width(okWidth).height(okHeight).align(Align.bottomRight);
        content.row();

        root.pad(10);
        root.add(img).width(imgWidth).height(imgHeight).center();
        root.add(content).width(maxWidth * .65f).center().padLeft(10);

        root.setBackground(new SpriteDrawable(new Sprite(cardBg)));

        align(Align.center);
        setActor(root);
        padBottom(20);

        enabled = true;

        addCaptureListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                if (screen.game.sfxService == null) return;

                if (!enabled) {
                    screen.game.sfxService.onAnyDisabledBtnClicked();
                } else {
                    screen.game.sfxService.onAnyBtnClicked();
                }
            }
        });

        addCaptureListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                if (purchased) {
                    event.cancel();

                    screen.onPurchaseCompleted();
                }
            }
        });

    }

    public void setPurchased(boolean purchased) {
        this.purchased = purchased;
        okImg.setVisible(purchased);
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        setTouchable(enabled ? Touchable.enabled : Touchable.disabled);
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
}
