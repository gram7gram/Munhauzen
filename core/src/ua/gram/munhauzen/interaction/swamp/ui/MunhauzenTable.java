package ua.gram.munhauzen.interaction.swamp.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.screen.game.ui.BackgroundImage;

public class MunhauzenTable extends Table {

    final BackgroundImage backgroundImage;

    public MunhauzenTable(BackgroundImage backgroundImage) {
        this.backgroundImage = backgroundImage;

        setClip(true);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        setBounds(
                backgroundImage.background.getX(),
                backgroundImage.background.getY(),
                MunhauzenGame.WORLD_WIDTH,
                MunhauzenGame.WORLD_HEIGHT
        );
    }
}
