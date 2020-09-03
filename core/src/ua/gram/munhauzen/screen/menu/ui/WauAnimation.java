package ua.gram.munhauzen.screen.menu.ui;

import com.badlogic.gdx.graphics.Texture;

import ua.gram.munhauzen.animation.AnimatedImage;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class WauAnimation extends AnimatedImage {

    public float width, height;

    public WauAnimation(Texture texture, float width) {
        super();

        animate(texture, 4, 1, 4, 0.15f);

        this.width = width;
        loop = true;
    }

    @Override
    public void start() {

//        width = MunhauzenGame.WORLD_WIDTH * .5f;
        float scale = 1f * width / getCurrentDrawable().getMinWidth();
        height = scale * getCurrentDrawable().getMinHeight();

        setSize(width, height);

        super.start();
    }
}

