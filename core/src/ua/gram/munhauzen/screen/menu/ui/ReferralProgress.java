package ua.gram.munhauzen.screen.menu.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;

import ua.gram.munhauzen.entity.PurchaseState;
import ua.gram.munhauzen.screen.MenuScreen;
import ua.gram.munhauzen.service.ReferralService;
import ua.gram.munhauzen.ui.FixedImage;

public class ReferralProgress extends Stack {

    final MenuScreen screen;
    Container<?> color, black;
    public final float width, height;

    public ReferralProgress(final MenuScreen screen, float width) {

        this.screen = screen;

        Texture txt1 = screen.game.internalAssetManager.get("menu/InviteBar_0.png", Texture.class);
        Texture txt2 = screen.game.internalAssetManager.get("menu/InviteBar_100.png", Texture.class);

        this.width = width;

        FixedImage progressColor = new FixedImage(txt2, width);
        FixedImage progressBlack = new FixedImage(txt1, width);

        height = progressColor.height;

        color = new Container<>(progressColor);
        color.setFillParent(false);
        color.setClip(true);

        black = new Container<>(progressBlack);
        black.setFillParent(false);
        black.setClip(true);

        setFillParent(true);
        add(black);
        add(color);

        layout();
    }

    @Override
    public void layout() {

        setWidth(width);
        setHeight(height);

        float progress = 0;

        PurchaseState state = screen.game.gameState.purchaseState;
        if (state != null) {
            progress = 1f * state.referralCount / ReferralService.MAX_REFERRAL_COUNT;
        }

        progress = Math.min(1f, progress);

        super.layout();

        black.setWidth(width);
        color.setWidth(width * progress);
    }
}
