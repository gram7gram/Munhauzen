package ua.gram.munhauzen.screen.authors.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

public class Underline extends Image {

    public Underline() {
        Pixmap px = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        px.setColor(Color.BLACK);
        px.fill();

        setDrawable(new SpriteDrawable(new Sprite(new Texture(px))));
    }
}
