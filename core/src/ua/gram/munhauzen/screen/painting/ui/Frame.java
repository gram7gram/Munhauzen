package ua.gram.munhauzen.screen.painting.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

import ua.gram.munhauzen.screen.PaintingScreen;

public abstract class Frame extends Image {

    public final PaintingScreen screen;
    public final Painting painting;
    public float framePadding;

    public Frame(PaintingScreen screen, Painting painting) {
        super();
        this.screen = screen;
        this.painting = painting;

        framePadding = getFramePaddingPercent();
    }

    public abstract Texture createTexture();

    public abstract float getFramePaddingPercent();

    public void setBackground(Texture texture, boolean isWide) {

        Sprite sprite = new Sprite(texture);
        if (isWide)
            sprite.rotate90(true);

        SpriteDrawable drawable = new SpriteDrawable(sprite);

        setDrawable(drawable);
    }
}
