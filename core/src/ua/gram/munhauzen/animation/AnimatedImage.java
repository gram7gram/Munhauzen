package ua.gram.munhauzen.animation;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;

import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public abstract class AnimatedImage extends Image {

    public Animation<TextureRegionDrawable> animation;
    public float duration;
    public boolean isStarted;

    public AnimatedImage(Texture texture) {

        super(texture);

        setScaling(Scaling.fit);
    }

    public void animate(Texture walkSheet, int rows, int cols, int frameLimit, float speed) {

        TextureRegion[][] tmp = TextureRegion.split(walkSheet,
                walkSheet.getWidth() / cols,
                walkSheet.getHeight() / rows);

        TextureRegionDrawable[] frames = new TextureRegionDrawable[frameLimit];
        int index = -1;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                index++;
                if (index < frameLimit) {
                    frames[index] = new TextureRegionDrawable(tmp[i][j]);
                }
            }
        }


        animation = new Animation<>(speed, frames);
        duration = 0;
        isStarted = false;
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        setDrawable(animation.getKeyFrame(duration));

        boolean isFinished = animation.isAnimationFinished(duration);

        if (isFinished) {
            duration = 0;
        }

        if (isStarted) {
            duration += delta;
        }
    }

    public void start() {
        duration = 0;
        isStarted = true;
    }

}
