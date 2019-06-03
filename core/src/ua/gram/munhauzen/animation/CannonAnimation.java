package ua.gram.munhauzen.animation;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class CannonAnimation extends Image {

    private final Animation<TextureRegionDrawable> animation;
    private float duration;
    private boolean isStarted;

    public CannonAnimation(Texture texture) {

        super(new SpriteDrawable(new Sprite(texture)));

        setScaling(Scaling.fit);

        duration = 0;
        animation = animate(texture);
    }

    private Animation<TextureRegionDrawable> animate(Texture walkSheet) {

        int FRAME_ROWS = 14;
        int FRAME_COLS = 1;

        TextureRegion[][] tmp = TextureRegion.split(walkSheet,
                walkSheet.getWidth() / FRAME_COLS,
                walkSheet.getHeight() / FRAME_ROWS);

        TextureRegionDrawable[] frames = new TextureRegionDrawable[FRAME_COLS * FRAME_ROWS];
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                frames[index++] = new TextureRegionDrawable(tmp[i][j]);
            }
        }

        return new Animation<>(0.04f, frames);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        setDrawable(animation.getKeyFrame(duration));

        if (isStarted) {
            duration += delta;
        }
    }

    public void start() {
        isStarted = true;
    }

}
