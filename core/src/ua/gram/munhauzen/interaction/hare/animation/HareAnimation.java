package ua.gram.munhauzen.interaction.hare.animation;

import com.badlogic.gdx.graphics.Texture;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.animation.AnimatedImage;
import ua.gram.munhauzen.interaction.hare.ui.Ground;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class HareAnimation extends AnimatedImage {

    final Ground ground;

    public HareAnimation(Texture texture, Ground ground) {
        super(texture);

        animate(texture, 1, 4, 4, 0.12f);

        this.ground = ground;
    }

    @Override
    public void layout() {
        super.layout();

        float size = MunhauzenGame.WORLD_WIDTH / 5f;

        setSize(size, size);
        setPosition(
                50,
                ground.originPoint.getY() + ground.image.getHeight() / 3f
        );
    }
}

