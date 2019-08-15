package ua.gram.munhauzen.screen.painting.ui;

import com.badlogic.gdx.graphics.Texture;

import ua.gram.munhauzen.screen.PaintingScreen;

public class ColorFrame extends Frame {

    public ColorFrame(PaintingScreen screen, Painting painting) {
        super(screen, painting);
    }

    @Override
    public float getFramePaddingPercent() {
        return .19f;
    }

    @Override
    public Texture createTexture() {
        return screen.assetManager.get("gallery/gv2_frame_2.png", Texture.class);
    }
}
