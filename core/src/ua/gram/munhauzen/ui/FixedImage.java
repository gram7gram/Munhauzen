package ua.gram.munhauzen.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

public class FixedImage extends Image {

    public float width, height;

    public FixedImage() {
        super();
    }

    public FixedImage(Texture texture, float width) {
        this();

        setBackground(texture, width);
    }

    public void setBackground(Texture texture, float width) {
        setBackground(new SpriteDrawable(new Sprite(texture)), width);
    }

    public void setBackground(SpriteDrawable drawable, float width) {
        setDrawable(drawable);

        this.width = width;
        this.height = drawable.getMinHeight() * (width / drawable.getMinWidth());

        setSize(width, height);

        layout();
    }

    @Override
    public void layout() {
        super.layout();

        setSize(width, height);
    }
}
