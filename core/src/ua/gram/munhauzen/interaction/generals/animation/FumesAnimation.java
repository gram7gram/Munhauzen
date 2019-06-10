package ua.gram.munhauzen.interaction.generals.animation;

import com.badlogic.gdx.graphics.Texture;

import ua.gram.munhauzen.animation.AnimatedImage;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class FumesAnimation extends AnimatedImage {

    public FumesAnimation(Texture texture) {
        super(texture);

        animate(texture, 1, 4, 4, 0.08f);

        //float size = MunhauzenGame.WORLD_WIDTH / 5f;

        setSize(100, 100);
        setPosition(100, 100);
    }

}

