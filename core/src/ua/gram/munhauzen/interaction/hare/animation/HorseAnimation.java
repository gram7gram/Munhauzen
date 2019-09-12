package ua.gram.munhauzen.interaction.hare.animation;

import com.badlogic.gdx.graphics.Texture;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.animation.AnimatedImage;
import ua.gram.munhauzen.interaction.hare.ui.Ground;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class HorseAnimation extends AnimatedImage {

    final Ground ground;

    public HorseAnimation(Texture texture, Ground ground) {
        super(texture);

        animate(texture, 1, 5, 5, 0.15f);

        this.ground = ground;
    }

    @Override
    public void layout() {
        super.layout();

        float size = MunhauzenGame.WORLD_WIDTH * .4f;

        setSize(size, size);
        setPosition(
                MunhauzenGame.WORLD_WIDTH - 50 - getWidth(),
                ground.originPoint.getY() + ground.image.getHeight() / 3f
        );

    }
}

