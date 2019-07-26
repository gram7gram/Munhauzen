package ua.gram.munhauzen.interaction.timer2.animation;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.animation.AnimatedImage;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class SparkAnimation extends AnimatedImage {

    final float width, duration;
    final Bomb bomb;

    public SparkAnimation(Texture texture, Bomb bomb, float width, float duration) {
        super(texture);

        animate(texture, 8, 1, 8, 0.12f);

        this.bomb = bomb;
        this.duration = duration;
        this.width = width;
    }

    public void start(Runnable onComplete) {

        start();

        TextureRegionDrawable drawable = animation.getKeyFrame(0);

        float scale = 1f * width / drawable.getMinWidth();
        float height = 1f * drawable.getMinHeight() * scale;

        setSize(width, height);
        setPosition(0, MunhauzenGame.WORLD_HEIGHT * 3 / 4f - height / 2f);

        addAction(Actions.sequence(
                Actions.moveTo(0, bomb.getY() + bomb.getHeight() / 2f, duration),
                Actions.run(onComplete))
        );
    }

    public void reset() {
        clearActions();
    }
}

