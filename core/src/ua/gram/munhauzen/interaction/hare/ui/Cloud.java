package ua.gram.munhauzen.interaction.hare.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.utils.Random;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class Cloud extends Image {


    public Cloud(Texture texture, int width, int height, float x, float y) {

        super(texture);


        setSize(width, height);
        setPosition(x - width - 20, y);
    }

    public void start() {

        Random r = new Random();

        float width = getDrawable().getMinWidth();

        addAction(
                Actions.forever(
                        Actions.sequence(
                                Actions.delay(r.between(0, 2)),
                                Actions.moveTo(-width, getY()),
                                Actions.moveTo(MunhauzenGame.WORLD_WIDTH, getY(), r.between(5, 10)),
                                Actions.delay(r.between(1, 2))
                        )
                )
        );
    }
}
