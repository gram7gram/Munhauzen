package ua.gram.munhauzen.interaction.hare.animation;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.animation.AnimatedImage;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class HorseAnimation extends AnimatedImage {

    Actor origin;

    public HorseAnimation(Texture texture, Actor origin) {
        super(texture);

        animate(texture, 1, 5, 5, 0.1f);

        this.origin = origin;
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        setSize(250, 250);
        setPosition(MunhauzenGame.WORLD_WIDTH - 50 -getWidth(), MunhauzenGame.WORLD_HEIGHT * .32f);
        setOrigin(origin.getOriginX(),origin.getOriginY());

    }
}

