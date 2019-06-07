package ua.gram.munhauzen.interaction.hare.animation;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.animation.AnimatedImage;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class HareAnimation extends AnimatedImage {

    Actor origin;

    public HareAnimation(Texture texture, Actor origin) {
        super(texture);

        animate(texture, 1, 4, 4, 0.08f);

        this.origin= origin;

    }

    @Override
    public void act(float delta) {
        super.act(delta);

        setSize(200, 200);
        setPosition(50, MunhauzenGame.WORLD_HEIGHT * .35f);
        setOrigin(origin.getOriginX(),origin.getOriginY());

    }
}

