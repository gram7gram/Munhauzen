package ua.gram.munhauzen.animation;

import com.badlogic.gdx.graphics.Texture;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class CannonLetterAnimation extends AnimatedImage {

    public CannonLetterAnimation(Texture texture) {
        super(texture);

        animate(texture, 5, 5, 25, 0.07f);
    }

}
