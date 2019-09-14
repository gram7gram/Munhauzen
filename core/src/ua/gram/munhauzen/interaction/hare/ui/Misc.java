package ua.gram.munhauzen.interaction.hare.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import ua.gram.munhauzen.utils.Random;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class Misc extends Group {

    Image image;
    Ground ground;

    public Misc(Texture texture, Ground origin, float width, float height) {

        this.ground = origin;
        image = new Image(texture);

        image.setSize(width, height);

        addActor(image);

        layout();
    }

    public void layout() {

        float offset = .2f;
        setSize(ground.getWidth() * (1 - offset), ground.getHeight() * (1 - offset));

        setPosition(
                ground.getX() + ground.getWidth() * offset / 2,
                ground.getY() + ground.getHeight() * offset / 2
        );

        setOrigin(getWidth() * .5f, getHeight() * .5f);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        layout();
    }

    public void start() {

        addAction(
                Actions.forever(Actions.rotateBy(-90, new Random().between(2, 6)))
        );
    }
}
