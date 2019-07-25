package ua.gram.munhauzen.interaction.servants;

import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.screen.game.ui.BackgroundImage;

public class ServantImage extends BackgroundImage {

    public ServantImage(GameScreen screen) {
        super(screen);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        leftArrow.setVisible(false);
        rightArrow.setVisible(false);
    }
}
