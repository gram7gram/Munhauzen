package ua.gram.munhauzen.interaction.hare.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import ua.gram.munhauzen.MunhauzenGame;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class Ground extends Image {

    Texture texture;

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
        float scale = 1f * width / texture.getWidth();
        float height = texture.getHeight() * scale;

        setSize(width, height);
        setPosition(-width / 4f, -height * 3 / 5f);
        setOrigin(getX() + width / 2f, getY() + height / 2f);

    }
}
