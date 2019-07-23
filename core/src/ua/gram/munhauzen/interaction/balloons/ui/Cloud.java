package ua.gram.munhauzen.interaction.balloons.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.utils.Random;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class Cloud extends Image {

    final float width, height, x, y;

    public Cloud(Texture texture, int width, int height, float x, float y) {

        super(texture);

        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
    }

    public void start() {

        Random r = new Random();

        float width = getDrawable().getMinWidth();

        addAction(
                Actions.forever(
                        Actions.sequence(
                                Actions.visible(false),
                                Actions.delay(r.between(0, 2)),
                                Actions.visible(true),
                                Actions.moveTo(-width, y),
                                Actions.moveTo(MunhauzenGame.WORLD_WIDTH, y, r.between(5, 10)),
                                Actions.delay(r.between(1, 2))
                        )
                )
        );
    }

    @Override
    public void layout() {
        super.layout();

        setPosition(x - width - 20, y);

        setSize(width, height);
    }
}
