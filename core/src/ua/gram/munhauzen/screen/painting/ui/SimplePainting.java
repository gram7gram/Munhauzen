package ua.gram.munhauzen.screen.painting.ui;

import ua.gram.munhauzen.screen.PaintingScreen;

public class SimplePainting extends Painting {

    public SimplePainting(PaintingScreen screen) {
        super(screen);
    }

    @Override
    public Frame createFrame() {
        return new SimpleFrame(screen, this);
    }
}
