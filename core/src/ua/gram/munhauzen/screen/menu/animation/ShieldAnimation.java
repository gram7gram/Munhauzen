package ua.gram.munhauzen.screen.menu.animation;

import com.badlogic.gdx.graphics.Texture;

import ua.gram.munhauzen.animation.AnimatedImage;

public class ShieldAnimation extends AnimatedImage {

    public ShieldAnimation(Texture texture) {
        super(texture);

        animate(texture, 8, 1, 8, 0.08f);
    }
}

