package ua.gram.munhauzen.interaction.timer.animation;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import ua.gram.munhauzen.animation.AnimatedImage;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class BoomAnimation extends AnimatedImage {

    final Bomb bomb;
    final float width;

    public BoomAnimation(Texture texture, Bomb bomb, float width) {
        super(texture, false);

        animate(texture, 7, 1, 7, 0.12f);

        this.bomb = bomb;
        this.width = width;
    }

    @Override
    public void start() {
        super.start();

        TextureRegionDrawable drawable = animation.getKeyFrame(0);

        float scale = 1f * width / drawable.getMinWidth();
        float height = 1f * drawable.getMinHeight() * scale;

        setSize(width, height);
        setPosition(bomb.getX(), bomb.getY());


    }
}

