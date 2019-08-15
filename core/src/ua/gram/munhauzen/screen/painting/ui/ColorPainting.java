package ua.gram.munhauzen.screen.painting.ui;

import ua.gram.munhauzen.screen.PaintingScreen;

public class ColorPainting extends Painting {

    public ColorPainting(PaintingScreen screen) {
        super(screen);
    }

    @Override
    public Frame createFrame() {
        return new ColorFrame(screen, this);
    }
}
