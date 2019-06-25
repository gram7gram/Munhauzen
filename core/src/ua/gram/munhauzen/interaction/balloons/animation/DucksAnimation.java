package ua.gram.munhauzen.interaction.balloons.animation;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.animation.AnimatedImage;
import ua.gram.munhauzen.utils.Random;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class DucksAnimation extends AnimatedImage {

    public DucksAnimation(Texture texture) {
        super(texture);

        animate(texture, 6, 1, 6, 0.08f);

        float size = MunhauzenGame.WORLD_WIDTH * .3f;
        float scale = 1f * size / 200;

        setSize(size, 91 * scale);
        setPosition(-getWidth() - 20, MunhauzenGame.WORLD_HEIGHT * .75f);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

    }

    @Override
    public void start() {
        super.start();

        Random r = new Random();

        float width = getWidth();

        addAction(
                Actions.forever(
                        Actions.sequence(
                                Actions.delay(r.between(2, 10)),
                                Actions.moveBy(MunhauzenGame.WORLD_WIDTH * 2, 0, r.between(4, 6)),
                                Actions.moveTo(-1 * r.between((int) width, (int) width * 2), getY())
                        )
                )
        );
    }
}

