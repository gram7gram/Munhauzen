package ua.gram.munhauzen.screen.painting.ui;

import ua.gram.munhauzen.screen.PaintingScreen;

public class BonusPainting extends Painting {

    public BonusPainting(PaintingScreen screen) {
        super(screen);
    }

    @Override
    public Frame createFrame() {
        return new BonusFrame(screen, this);
    }
}