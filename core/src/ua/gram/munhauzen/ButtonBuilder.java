package ua.gram.munhauzen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

import io.sentry.util.Nullable;
import ua.gram.munhauzen.ui.PrimaryButton;

public class ButtonBuilder {

    private final MunhauzenGame game;
    private final Texture secondaryEnabled, primaryEnabled;
    private final Texture dangerDisabled, dangerEnabled;
    final int pad = 50;

    public static int BTN_PRIMARY_WIDTH = 300;
    public static int BTN_PRIMARY_SM_WIDTH = 225;
    public static int BTN_PRIMARY_HEIGHT = 100;
    public static int BTN_PRIMARY_SM_HEIGHT = 75;

    public ButtonBuilder(MunhauzenGame game) {
        this.game = game;
        primaryEnabled = game.internalAssetManager.get("ui/b_primary_sm_enabled.png", Texture.class);
        secondaryEnabled = game.internalAssetManager.get("ui/b_primary_sm_disabled.png", Texture.class);
        dangerEnabled = game.internalAssetManager.get("ui/b_danger_sm_enabled.png", Texture.class);
        dangerDisabled = game.internalAssetManager.get("ui/b_danger_sm_disabled.png", Texture.class);

        BTN_PRIMARY_WIDTH *= game.params.scaleFactor;
        BTN_PRIMARY_SM_WIDTH *= game.params.scaleFactor;
        BTN_PRIMARY_HEIGHT *= game.params.scaleFactor;
        BTN_PRIMARY_SM_HEIGHT *= game.params.scaleFactor;
    }

    public PrimaryButton primary(String text, @Nullable ClickListener onClick) {

        NinePatchDrawable background1 = new NinePatchDrawable(new NinePatch(primaryEnabled,
                pad, pad, 0, 0));

        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = game.fontProvider.getFont(FontProvider.h4);
        style.up = background1;
        style.down = background1;
        style.disabled = background1;
        style.fontColor = Color.BLACK;

        final PrimaryButton button = new PrimaryButton(text, style);

        button.addCaptureListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                if (game.sfxService == null) return;

                if (button.isDisabled()) {
                    game.sfxService.onAnyDisabledBtnClicked();
                } else {
                    game.sfxService.onAnyBtnClicked();
                }
            }
        });

        if (onClick != null)
            button.addListener(onClick);

        return button;
    }

    public PrimaryButton primaryRose(String text, @Nullable ClickListener onClick) {

        NinePatchDrawable background1 = new NinePatchDrawable(new NinePatch(
                game.internalAssetManager.get("ui/btn_rose_enabled.png", Texture.class),
                30, 30, 0, 0));
        NinePatchDrawable background2 = new NinePatchDrawable(new NinePatch(
                game.internalAssetManager.get("ui/btn_rose_enabled.png", Texture.class),
                30, 30, 0, 0));

        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = game.fontProvider.getFont(FontProvider.h4);
        style.up = background1;
        style.down = background1;
        style.disabled = background2;
        style.fontColor = Color.BLACK;

        final PrimaryButton button = new PrimaryButton(text, style);

        button.addCaptureListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                if (game.sfxService == null) return;

                if (button.isDisabled()) {
                    game.sfxService.onAnyDisabledBtnClicked();
                } else {
                    game.sfxService.onAnyBtnClicked();
                }
            }
        });

        if (onClick != null)
            button.addListener(onClick);

        return button;
    }

    public PrimaryButton danger(String text, @Nullable ClickListener onClick) {

        NinePatchDrawable background1 = new NinePatchDrawable(new NinePatch(dangerEnabled,
                pad, pad, 0, 0));
        NinePatchDrawable background2 = new NinePatchDrawable(new NinePatch(dangerDisabled,
                pad, pad, 0, 0));

        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = game.fontProvider.getFont(FontProvider.h4);
        style.up = background1;
        style.down = background1;
        style.disabled = background2;
        style.fontColor = Color.BLACK;

        final PrimaryButton button = new PrimaryButton(text, style);

        button.addCaptureListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                if (game.sfxService == null) return;

                if (button.isDisabled()) {
                    game.sfxService.onAnyDisabledBtnClicked();
                } else {
                    game.sfxService.onAnyBtnClicked();
                }
            }
        });

        if (onClick != null)
            button.addListener(onClick);

        return button;
    }

    public PrimaryButton secondary(String text, @Nullable ClickListener onClick) {

        NinePatchDrawable background1 = new NinePatchDrawable(new NinePatch(secondaryEnabled,
                pad, pad, 0, 0));

        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = game.fontProvider.getFont(FontProvider.h4);
        style.up = background1;
        style.down = background1;
        style.disabled = background1;
        style.fontColor = Color.BLACK;

        final PrimaryButton button = new PrimaryButton(text, style);

        button.addCaptureListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                if (game.sfxService == null) return;

                if (button.isDisabled()) {
                    game.sfxService.onAnyDisabledBtnClicked();
                } else {
                    game.sfxService.onAnyBtnClicked();
                }
            }
        });

        if (onClick != null)
            button.addListener(onClick);

        return button;
    }
}
