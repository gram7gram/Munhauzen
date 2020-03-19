package ua.gram.munhauzen.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

public class FixedImage extends Image {

    public float width, height;

    public FixedImage() {
        super();
    }

    public FixedImage(Texture texture, float width) {
        this(texture, width, texture.getHeight() * (width / texture.getWidth()));
    }

    public FixedImage(Texture texture, float width, float height) {
        super(texture);
        this.width = width;
        this.height = height;

        setSize(width, height);
    }

    public void setBackground(SpriteDrawable drawable, float width) {
        setDrawable(drawable);

        this.width = width;
        this.height = drawable.getMinHeight() * (width / drawable.getMinWidth());

        setSize(width, height);
    }
}
