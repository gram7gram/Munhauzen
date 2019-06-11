package ua.gram.munhauzen.interaction.generals.animation;

import com.badlogic.gdx.graphics.Texture;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.animation.AnimatedImage;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class FireRightAnimation extends AnimatedImage {

    public FireRightAnimation(Texture texture) {
        super(texture);

        animate(texture, 1, 4, 4, 0.08f);

//        float size = MunhauzenGame.WORLD_WIDTH / 5f;

        setSize(100, 100);
        setPosition(MunhauzenGame.WORLD_WIDTH - getWidth() - 10, MunhauzenGame.WORLD_HEIGHT * .75f);
    }

}
