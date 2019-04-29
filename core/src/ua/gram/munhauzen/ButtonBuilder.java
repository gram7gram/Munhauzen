package ua.gram.munhauzen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class ButtonBuilder {

    private final MunhauzenGame game;
    private final Texture decoration;

    public ButtonBuilder(MunhauzenGame game) {
        this.game = game;
        decoration = game.assetManager.get("ui/b_primary_enabled.9.png", Texture.class);
    }

    public Button primary(String text, final ClickListener onClick) {

        NinePatchDrawable background = new NinePatchDrawable(new NinePatch(decoration, 90, 90, 0, 0));

        TextButton button = new TextButton(text, new TextButton.TextButtonStyle(
                background,
                background,
                background,
                game.fontProvider.getFont(FontProvider.BuxtonSketch, FontProvider.h3)
        ));
        button.getLabel().setColor(Color.BLACK);

        button.addListener(onClick);

        return button;
    }
}
