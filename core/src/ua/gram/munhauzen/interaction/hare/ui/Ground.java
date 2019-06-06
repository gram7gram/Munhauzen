package ua.gram.munhauzen.interaction.hare.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.ui.FitImage;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class Ground extends Image {

    Texture texture;
    float scale;

    public Ground(Texture texture) {
        super(texture);

        this.texture = texture;

        addAction(
                Actions.forever(Actions.rotateBy(-90, 10))
        );

    }

    @Override
    public void act(float delta) {
        super.act(delta);

        float width = MunhauzenGame.WORLD_WIDTH * 2f;
        scale = 1f * width / texture.getWidth();

        setSize(width, texture.getHeight() * scale);
        setPosition(-getWidth() / 4f, -getHeight() * 3 / 5f);
        setOrigin(getWidth() / 2f, getHeight() / 2f);

    }
}
