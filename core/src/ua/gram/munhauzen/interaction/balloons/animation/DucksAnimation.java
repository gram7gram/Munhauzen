package ua.gram.munhauzen.interaction.balloons.animation;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.animation.AnimatedImage;
import ua.gram.munhauzen.utils.Random;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class DucksAnimation extends AnimatedImage {

    float width, height;

    public DucksAnimation(Texture texture) {
        super(texture);

        animate(texture, 6, 1, 6, 0.08f);
    }

    @Override
    public void layout() {
        super.layout();

        Drawable drawable = getCurrentDrawable();

        width = MunhauzenGame.WORLD_WIDTH * .4f;
        float scale = 1f * width / drawable.getMinWidth();
        height = drawable.getMinHeight() * scale;

        setSize(width, height);

        setPosition(-width - 20, MunhauzenGame.WORLD_HEIGHT * .75f);
    }

    @Override
    public void start() {
        super.start();

        Random r = new Random();

        float y = MunhauzenGame.WORLD_HEIGHT * .75f;

        clearActions();

        addAction(
                Actions.forever(
                        Actions.sequence(
                                Actions.visible(false),
                                Actions.delay(r.between(2, 10)),
                                Actions.visible(true),
                                Actions.moveTo(-getCurrentDrawable().getMinWidth(), y),
                                Actions.moveTo(MunhauzenGame.WORLD_WIDTH, y, r.between(4, 6))
                        )
                )
        );
    }
}

