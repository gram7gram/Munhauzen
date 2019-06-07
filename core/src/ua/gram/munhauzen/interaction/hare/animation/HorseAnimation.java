package ua.gram.munhauzen.interaction.hare.animation;

import com.badlogic.gdx.graphics.Texture;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.animation.AnimatedImage;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class HorseAnimation extends AnimatedImage {

    public HorseAnimation(Texture texture) {
        super(texture);

        animate(texture, 1, 5, 5, 0.1f);

        setSize(250, 250);
        setPosition(MunhauzenGame.WORLD_WIDTH - 50 - getWidth(), MunhauzenGame.WORLD_HEIGHT * .32f);

    }
}

