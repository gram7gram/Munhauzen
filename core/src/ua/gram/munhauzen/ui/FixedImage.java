package ua.gram.munhauzen.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class FixedImage extends Image {

    public final float width, height;

    public FixedImage(Texture texture, float width) {
        this(texture, width, texture.getHeight() * (width / texture.getWidth()));
    }

    public FixedImage(Texture texture, float width, float height) {
        super(texture);
        this.width = width;
        this.height = height;

        setSize(width, height);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        setSize(width, height);
    }
}
