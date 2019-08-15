package ua.gram.munhauzen.screen.painting.ui;

import com.badlogic.gdx.graphics.Texture;

import ua.gram.munhauzen.screen.PaintingScreen;

public class StatueFrame extends Frame {

    public StatueFrame(PaintingScreen screen, Painting painting) {
        super(screen, painting);
    }

    @Override
    public float getFramePaddingPercent() {
        return .21f;
    }

    @Override
    public Texture createTexture() {
        return screen.assetManager.get("gallery/gv2_frame_3.png", Texture.class);
    }
}
