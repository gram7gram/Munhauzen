package ua.gram.munhauzen.screen.painting.ui;

import com.badlogic.gdx.graphics.Texture;

import ua.gram.munhauzen.screen.PaintingScreen;

public class ColorFrame extends Frame {

    public ColorFrame(PaintingScreen screen, Painting painting) {
        super(screen, painting);
    }

    @Override
    public float getFramePaddingPercent() {
        return .15f;
    }

    @Override
    public Texture createTexture() {
        return screen.paintingFragment.getColorFrameTexture();
    }
}
