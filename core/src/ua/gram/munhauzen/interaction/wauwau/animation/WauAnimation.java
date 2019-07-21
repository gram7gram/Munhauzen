package ua.gram.munhauzen.interaction.wauwau.animation;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.animation.AnimatedImage;
import ua.gram.munhauzen.interaction.wauwau.fragment.WauImageFragment;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class WauAnimation extends AnimatedImage {

    final WauImageFragment fragment;
    public float width, height;
    public boolean isMoving;

    public WauAnimation(Texture texture, WauImageFragment fragment) {
        super(texture);

        this.fragment = fragment;

        animate(texture, 4, 1, 4, 0.15f);
        setTouchable(Touchable.disabled);
    }

    public void init() {

        width = MunhauzenGame.WORLD_WIDTH * .5f;
        float scale = 1f * width / getCurrentDrawable().getMinWidth();
        height = scale * getCurrentDrawable().getMinHeight();

        setSize(width, height);

        setX(fragment.background.getX() + fragment.backgroundWidth);

        start();
    }

    public void stop() {
        clear();
        remove();
        isMoving = false;
    }

    public void startMovement() {
        clearActions();

        isMoving = true;

        addAction(Actions.forever(
                Actions.sequence(
                        Actions.visible(false),
                        Actions.delay(2),
                        Actions.visible(true),
                        Actions.moveTo(fragment.background.getX() + fragment.backgroundWidth, getY()),
                        Actions.moveTo(fragment.background.getX() - width, getY(), 5)
                )
        ));
    }

    public void stopMovement() {
        isMoving = false;
        clearActions();
    }

    public void resumeMovement() {
        clearActions();

        isMoving = true;

        float x = getX() + width;
        float distance = fragment.backgroundWidth + width;
        float duration = 5;

        addAction(Actions.sequence(
                Actions.moveTo(fragment.background.getX() - width, getY(), duration * x / distance),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        startMovement();
                    }
                })
        ));
    }

    @Override
    public void act(float delta) {
        super.act(delta);


        setY(MunhauzenGame.WORLD_HEIGHT * .225f);
    }
}

