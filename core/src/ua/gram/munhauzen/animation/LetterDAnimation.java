package ua.gram.munhauzen.animation;

import com.badlogic.gdx.graphics.Texture;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class LetterDAnimation extends AnimatedImage {

    public LetterDAnimation(Texture texture) {
        super(texture, false);

        animate(texture, 3, 10, 23);
    }

}
