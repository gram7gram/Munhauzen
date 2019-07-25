package ua.gram.munhauzen.screen.menu.animation;

import com.badlogic.gdx.graphics.Texture;

import ua.gram.munhauzen.animation.AnimatedImage;

public class HelmAnimation extends AnimatedImage {

    public HelmAnimation(Texture texture) {
        super(texture);

        animate(texture, 5, 1, 5, 0.08f);
    }
}

