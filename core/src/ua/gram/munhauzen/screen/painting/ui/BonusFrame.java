package ua.gram.munhauzen.screen.painting.ui;

import com.badlogic.gdx.graphics.Texture;

import ua.gram.munhauzen.screen.PaintingScreen;

public class BonusFrame extends Frame {

    public BonusFrame(PaintingScreen screen, Painting painting) {
        super(screen, painting);
    }

    @Override
    public float getFramePaddingPercent() {
        return .055f;
    }

    @Override
    public Texture createTexture() {
        return screen.paintingFragment.getBonusFrameTexture();
    }
}
