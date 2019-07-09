package ua.gram.munhauzen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

import ua.gram.munhauzen.ui.PrimaryButton;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class ButtonBuilder {

    private final MunhauzenGame game;
    private final Texture primaryDisabled, primaryEnabled;
    private final Texture dangerDisabled, dangerEnabled;

    public ButtonBuilder(MunhauzenGame game) {
        this.game = game;
        primaryEnabled = game.assetManager.get("ui/b_primary_sm_enabled.png", Texture.class);
        primaryDisabled = game.assetManager.get("ui/b_primary_sm_disabled.png", Texture.class);
        dangerEnabled = game.assetManager.get("ui/b_danger_sm_enabled.png", Texture.class);
        dangerDisabled = game.assetManager.get("ui/b_danger_sm_disabled.png", Texture.class);
    }

    public PrimaryButton primary(String text, final ClickListener onClick) {

        NinePatchDrawable background1 = new NinePatchDrawable(new NinePatch(primaryEnabled, 30, 30, 0, 0));
        NinePatchDrawable background2 = new NinePatchDrawable(new NinePatch(primaryDisabled, 30, 30, 0, 0));

        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = game.fontProvider.getFont(FontProvider.h4);
        style.up = background1;
        style.down = background1;
        style.disabled = background2;
        style.fontColor = Color.BLACK;

        PrimaryButton button = new PrimaryButton(text, style);

        button.addListener(onClick);

        return button;
    }

    public PrimaryButton danger(String text, final ClickListener onClick) {

        NinePatchDrawable background1 = new NinePatchDrawable(new NinePatch(dangerEnabled, 30, 30, 0, 0));
        NinePatchDrawable background2 = new NinePatchDrawable(new NinePatch(dangerDisabled, 30, 30, 0, 0));

        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = game.fontProvider.getFont(FontProvider.h4);
        style.up = background1;
        style.down = background1;
        style.disabled = background2;
        style.fontColor = Color.BLACK;

        PrimaryButton button = new PrimaryButton(text, style);

        button.addListener(onClick);

        return button;
    }
}
