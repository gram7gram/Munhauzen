package ua.gram.munhauzen.interaction.hare.animation;

import com.badlogic.gdx.graphics.Texture;

import ua.gram.munhauzen.animation.AnimatedImage;
import ua.gram.munhauzen.interaction.hare.ui.Ground;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class HareAnimation extends AnimatedImage {

    final Ground ground;

    public HareAnimation(Texture texture, Ground ground) {
        super(texture);

        this.ground = ground;

        animate(texture, 1, 4, 4, 0.12f);
    }

    @Override
    public void layout() {
        super.layout();

        setRotation(13.3f);

        float x = ground.getX() + (ground.getWidth() * 31.38f / 100);
        float y = ground.getY() + (ground.getHeight() * 81.62f / 100);
        float width = ground.getWidth() * 11.17f / 100;
        float height = ground.getHeight() * 9.10f / 100;

        setBounds(x, y, width, height);
    }
}

