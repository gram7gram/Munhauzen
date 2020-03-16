package ua.gram.munhauzen.interaction;

import ua.gram.munhauzen.screen.GameScreen;

public class DummyInteraction extends AbstractInteraction {

    public DummyInteraction(GameScreen gameScreen) {
        super(gameScreen);
    }

    @Override
    public boolean isValid() {
        return true;
    }
}
