package ua.gram.munhauzen.interaction.hare.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import ua.gram.munhauzen.MunhauzenGame;

public class Ground extends Image {

    public float width, height;

    public Ground(Texture texture) {

        super(texture);

        width = MunhauzenGame.WORLD_WIDTH * 1.75f;
        float scale = 1f * width / texture.getWidth();
        height = texture.getHeight() * scale;

        layout();
    }

    @Override
    public void layout() {
        super.layout();

        setSize(width, height);
        setPosition(
                (MunhauzenGame.WORLD_WIDTH - width) / 2f,
                -height * .6f
        );

        setOrigin(getWidth() * .5f, getHeight() * .5f);
    }

    public void start() {
        addAction(
                Actions.forever(Actions.rotateBy(-90, 2.5f))
        );
    }
}
