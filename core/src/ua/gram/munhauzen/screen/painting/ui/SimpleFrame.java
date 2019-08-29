package ua.gram.munhauzen.screen.painting.ui;

import com.badlogic.gdx.graphics.Texture;

import ua.gram.munhauzen.screen.PaintingScreen;

public class SimpleFrame extends Frame {

    public SimpleFrame(PaintingScreen screen, Painting painting) {
        super(screen, painting);
    }

    @Override
    public float getFramePaddingPercent() {
        return .05f;
    }

    @Override
    public Texture createTexture() {
        return screen.paintingFragment.getSimpleFrameTexture();
    }
}
