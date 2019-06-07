package ua.gram.munhauzen.interaction.hare.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.ui.FitImage;
import ua.gram.munhauzen.utils.Random;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class Cloud extends Group {

    FitImage image;

    public Cloud(Texture texture, int width, int height, float x, float y) {

        image = new FitImage(texture);

        addActor(image);

        image.setSize(width, height);
        image.setPosition(x - width - 20, y);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

    }

    public void start() {

        Random r = new Random();

        float width = image.getWidth();

        addAction(
                Actions.forever(
                        Actions.sequence(
                                Actions.delay(r.between(0, 2)),
                                Actions.moveBy(MunhauzenGame.WORLD_WIDTH*2, 0, r.between(3, 6)),
                                Actions.moveTo(-1 * r.between((int) width, (int) width * 2), getY()),
                                Actions.delay(r.between(1, 2))
                        )
                )
        );
    }
}
