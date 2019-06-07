package ua.gram.munhauzen.interaction.hare.animation;

import com.badlogic.gdx.graphics.Texture;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.animation.AnimatedImage;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class HareAnimation extends AnimatedImage {

    public HareAnimation(Texture texture) {
        super(texture);

        animate(texture, 1, 4, 4, 0.08f);

        setSize(200, 200);
        setPosition(50, MunhauzenGame.WORLD_HEIGHT * .35f);
    }
}

