package ua.gram.munhauzen.screen.menu.animation;

import com.badlogic.gdx.graphics.Texture;

import ua.gram.munhauzen.animation.AnimatedImage;

public class RoseAnimation extends AnimatedImage {

    public RoseAnimation(Texture texture) {
        super(texture);

        animate(texture, 6, 1, 6, 0.08f);
    }
}

