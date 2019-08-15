package ua.gram.munhauzen.screen.painting.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

import ua.gram.munhauzen.screen.PaintingScreen;

public abstract class Frame extends Table {

    public float frameWidth, frameHeight;
    public final PaintingScreen screen;
    public final Painting painting;
    public final Image frame;
    PaintingDescription description;
    public float framePadding;

    public Frame(PaintingScreen screen, Painting painting) {
        this.screen = screen;
        this.painting = painting;

        framePadding = getFramePaddingPercent();

        frame = new Image();
        description = new PaintingDescription(screen, this);

        add(frame).top().row();
        add(description).top().row();
    }

    public abstract Texture createTexture();

    public abstract float getFramePaddingPercent();

    public void setBackground(Texture texture, boolean isWide) {

        Sprite sprite = new Sprite(texture);
        if (isWide)
            sprite.rotate90(true);

        SpriteDrawable drawable = new SpriteDrawable(sprite);

        frame.setDrawable(drawable);

        layout();
    }

    @Override
    public void layout() {
        super.layout();

        frameWidth = painting.backgroundWidth * (1 + framePadding);
        frameHeight = painting.backgroundHeight * (1 + framePadding);

        getCell(frame)
                .width(frameWidth)
                .height(frameHeight);
    }
}
