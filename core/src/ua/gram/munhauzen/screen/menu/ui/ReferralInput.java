package ua.gram.munhauzen.screen.menu.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;

import ua.gram.munhauzen.FontProvider;
import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.utils.ColorUtils;

public class ReferralInput extends TextField {

    public ReferralInput(MunhauzenGame game) {
        super("", new TextFieldStyle(
                game.fontProvider.getFont(FontProvider.BuxtonSketch, FontProvider.h4),
                Color.BLACK,
                null,
                null,
                null
        ));

        Pixmap pm1 = new Pixmap(1, 1, Pixmap.Format.RGB565);
        pm1.setColor(ColorUtils.yellowLight);
        pm1.fill();

        SpriteDrawable inputBackground = new SpriteDrawable(new Sprite(new Texture(pm1)));

        getStyle().background = inputBackground;

        setMaxLength(200);
        setAlignment(Align.center);
        setDisabled(true);

    }
}
