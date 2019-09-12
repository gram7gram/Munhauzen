package ua.gram.munhauzen.interaction.swamp.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Image;

import ua.gram.munhauzen.screen.game.ui.BackgroundImage;

public class Swamp extends Image {

    final BackgroundImage backgroundImage;

    public Swamp(BackgroundImage backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        setPosition(
                backgroundImage.background.getX(),
                backgroundImage.background.getY()
        );
    }
}
