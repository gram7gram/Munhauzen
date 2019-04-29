package ua.gram.munhauzen.animation;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class CannonLetterAnimation extends Image {

    private final Animation<TextureRegionDrawable> animation;
    private float duration;
    private boolean isStarted;

    public CannonLetterAnimation(Texture texture) {

        super(new SpriteDrawable(new Sprite(texture)));

        duration = 0;
        animation = animate(texture, 23);
    }

    private Animation<TextureRegionDrawable> animate(Texture walkSheet, int frameLimit) {

        int FRAME_ROWS = 5;
        int FRAME_COLS = 5;

        TextureRegion[][] tmp = TextureRegion.split(walkSheet,
                walkSheet.getWidth() / FRAME_COLS,
                walkSheet.getHeight() / FRAME_ROWS);

        TextureRegionDrawable[] frames = new TextureRegionDrawable[frameLimit];
        int index = -1;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                index++;
                if (index < frameLimit) {
                    frames[index] = new TextureRegionDrawable(tmp[i][j]);
                }
            }
        }

        return new Animation<>(0.05f, frames);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        boolean isFinished = animation.isAnimationFinished(duration);

        setDrawable(animation.getKeyFrame(duration));

        if (isFinished) {
            isStarted = false;
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
