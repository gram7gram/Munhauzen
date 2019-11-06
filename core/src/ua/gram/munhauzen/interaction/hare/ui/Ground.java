package ua.gram.munhauzen.interaction.hare.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import ua.gram.munhauzen.MunhauzenGame;

public class Ground extends Image {

    public Ground(Texture texture) {
        super(texture);
    }

    @Override
    public void layout() {
        super.layout();

        float size = MunhauzenGame.WORLD_WIDTH * 177.8f / 100;
        float x = (MunhauzenGame.WORLD_WIDTH - size) / 2;
        float y = size * -63.52f / 100;

        setBounds(x, y, size, size);

        setOrigin(getWidth() * .5f, getHeight() * .5f);
    }

    public void start() {
        addAction(
                Actions.forever(Actions.rotateBy(-90, 2.5f))
        );
    }

    public void pause() {
        clearActions();
    }
}
