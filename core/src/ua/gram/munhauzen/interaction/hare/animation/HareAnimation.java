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

        setWidth(100);

        animate(texture, 1, 4, 4, 0.08f);

    }

    @Override
    public void act(float delta) {
        super.act(delta);

        setSize(200, 200);
        setPosition(50, MunhauzenGame.WORLD_HEIGHT * .35f);
        setOrigin(getWidth() / 2f, getHeight() / 2f);

    }
}

