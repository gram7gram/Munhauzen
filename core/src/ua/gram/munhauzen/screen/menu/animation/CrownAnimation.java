package ua.gram.munhauzen.screen.menu.animation;

import com.badlogic.gdx.graphics.Texture;

import ua.gram.munhauzen.animation.AnimatedImage;

public class CrownAnimation extends AnimatedImage {

    public CrownAnimation(Texture texture) {
        super(texture);

        animate(texture, 9, 1, 9, 0.08f);
    }
}

