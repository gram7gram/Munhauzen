package ua.gram.munhauzen.interaction.hare.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import ua.gram.munhauzen.ui.FitImage;
import ua.gram.munhauzen.utils.Random;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class Misc extends Group {

    FitImage image;
    Actor origin;
    Actor originPoint;

    public Misc(Texture texture, Actor origin, int width, int height, float x, float y) {

        this.origin = origin;

        image = new FitImage(texture);

        originPoint = new Actor();
        originPoint.setSize(3, 3);
        originPoint.setVisible(true);

        addActor(image);
        addActor(originPoint);

        image.setSize(width, height);
        image.setPosition(x - width - 20, y);

        setOrigin(origin.getOriginX(), origin.getOriginY());

        originPoint.setPosition(
                getOriginX() - 1,
                getOriginY() - 1
        );

    }

    @Override
    public void act(float delta) {
        super.act(delta);

        setOrigin(origin.getOriginX(), origin.getOriginY());

        originPoint.setPosition(
                getOriginX() - 1,
                getOriginY() - 1
        );

    }

    public void start() {

        addAction(
                Actions.forever(Actions.rotateBy(-90, new Random().between(8, 20)))
        );
    }
}
