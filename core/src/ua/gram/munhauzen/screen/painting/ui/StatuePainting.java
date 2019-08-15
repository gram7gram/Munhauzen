package ua.gram.munhauzen.screen.painting.ui;

import ua.gram.munhauzen.screen.PaintingScreen;

public class StatuePainting extends Painting {

    public StatuePainting(PaintingScreen screen) {
        super(screen);
    }

    @Override
    public Frame createFrame() {
        return new StatueFrame(screen, this);
    }
}
