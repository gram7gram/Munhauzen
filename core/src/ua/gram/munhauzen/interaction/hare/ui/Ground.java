package ua.gram.munhauzen.interaction.hare.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.ui.FitImage;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class Ground extends Group {

    public FitImage image;
    public Actor originPoint;
    Texture texture;

    public Ground(Texture texture) {

        this.texture = texture;
        image = new FitImage(texture);

        originPoint = new Actor();
        originPoint.setSize(3, 3);
        originPoint.setVisible(true);

        addActor(image);
        addActor(originPoint);

        float width = MunhauzenGame.WORLD_WIDTH * 2f;
        float scale = 1f * width / texture.getWidth();
        float height = texture.getHeight() * scale;

        image.setSize(width, height);
        image.setPosition(-width / 4f, -height * 3 / 5f);

        setOrigin(image.getX() + image.getWidth() / 2f, image.getY() + image.getHeight() / 2f);

        originPoint.setPosition(
                getOriginX() - 1,
                getOriginY() - 1
        );
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        setOrigin(image.getX() + image.getWidth() / 2f, image.getY() + image.getHeight() / 2f);

        originPoint.setPosition(
                getOriginX() - 1,
                getOriginY() - 1
        );

    }

    public void start() {
        addAction(
                Actions.forever(Actions.rotateBy(-90, 10))
        );
    }
}
