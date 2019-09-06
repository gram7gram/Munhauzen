package ua.gram.munhauzen.animation;

import com.badlogic.gdx.graphics.Texture;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class LetterCAnimation extends AnimatedImage {

    public LetterCAnimation(Texture texture) {
        super(texture, false);

        animate(texture, 3, 10, 23);
    }

}
