package ua.gram.munhauzen.animation;

import com.badlogic.gdx.graphics.Texture;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class CannonAnimation extends AnimatedImage {

    public CannonAnimation(Texture texture) {
        super(texture, false);

        animate(texture, 14, 1, 14, 0.04f);
    }
}
