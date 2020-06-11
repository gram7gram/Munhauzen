package ua.gram.munhauzen.animation;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public abstract class AnimatedImage extends Image {

    protected final String tag = getClass().getSimpleName();
    public Animation<TextureRegionDrawable> animation;
    public float duration;
    public boolean isStarted;
    public boolean loop;

    public AnimatedImage() {
        super();

        loop = true;
    }

    public AnimatedImage(Texture texture) {
        this(texture, true);
    }

    public AnimatedImage(Texture texture, boolean loop) {

        super(texture);

        setScaling(Scaling.fit);

        this.loop = loop;
    }

    public void animate(Texture walkSheet, int rows, int cols, int frameLimit) {
        animate(walkSheet, rows, cols, frameLimit, .08f);
    }

    public void animate(Texture walkSheet, int rows, int cols, int frameLimit, float speed) {

        TextureRegionDrawable[] frames = new TextureRegionDrawable[frameLimit];

        if (walkSheet != null) {
            TextureRegion[][] tmp = TextureRegion.split(walkSheet,
                    walkSheet.getWidth() / cols,
                    walkSheet.getHeight() / rows);

            int index = -1;
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    index++;
                    if (index < frameLimit) {
                        frames[index] = new TextureRegionDrawable(tmp[i][j]);
                    }
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

        if (animation == null) return;

        setDrawable(getCurrentDrawable());

        boolean isFinished = animation.isAnimationFinished(duration);

        if (isFinished) {

            if (loop) {
                duration = 0;
            } else {
                isStarted = false;
            }
        }

        if (isStarted) {
            duration += delta;
        }
    }

    public TextureRegionDrawable getCurrentDrawable() {
        return animation.getKeyFrame(duration);
    }

    public void start() {
        duration = 0;
        isStarted = true;
    }

    public void pause() {
        isStarted = false;
    }

}
