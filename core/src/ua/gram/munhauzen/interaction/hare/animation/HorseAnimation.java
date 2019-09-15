package ua.gram.munhauzen.interaction.hare.animation;

import com.badlogic.gdx.graphics.Texture;

import ua.gram.munhauzen.animation.AnimatedImage;
import ua.gram.munhauzen.interaction.hare.ui.Ground;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class HorseAnimation extends AnimatedImage {

    final Ground ground;

    public HorseAnimation(Texture texture, Ground ground) {
        super(texture);

        this.ground = ground;

        animate(texture, 1, 5, 5, 0.1f);
    }

    @Override
    public void layout() {
        super.layout();

        setRotation(-13.3f);

        float x = ground.getX() + (ground.getWidth() * 51.8f / 100);
        float y = ground.getY() + (ground.getHeight() * 85.3f / 100);
        float width = ground.getWidth() * 21.23f / 100;
        float height = ground.getHeight() * 18.24f / 100;

        setBounds(x, y, width, height);
    }
}

