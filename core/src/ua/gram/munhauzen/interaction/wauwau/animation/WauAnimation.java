package ua.gram.munhauzen.interaction.wauwau.animation;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.animation.AnimatedImage;
import ua.gram.munhauzen.interaction.wauwau.WauStoryImage;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class WauAnimation extends AnimatedImage {

    public float width, height;

    public WauAnimation(Texture texture) {
        super(texture);

        animate(texture, 4, 1, 4, 0.15f);
        setTouchable(Touchable.disabled);
    }

    public void init(WauStoryImage image) {

        width = MunhauzenGame.WORLD_WIDTH * .5f;
        float scale = 1f * width / image.width;
        height = scale * image.height;

        setSize(width, height);
        setX(MunhauzenGame.WORLD_WIDTH);
        setY(MunhauzenGame.WORLD_HEIGHT * .225f);

        startMovement();
    }

    public void startMovement() {
        clearActions();

        addAction(Actions.forever(
                Actions.sequence(
                        Actions.visible(false),
                        Actions.delay(2),
                        Actions.visible(true),
                        Actions.moveTo(MunhauzenGame.WORLD_WIDTH, getY()),
                        Actions.moveTo(-width, getY(), 5)
                )
        ));
    }

    public void resumeMovement() {
        clearActions();

        float x = getX() + width;
        float distance = MunhauzenGame.WORLD_WIDTH + width * 2;
        float duration = 5;

        addAction(Actions.sequence(
                Actions.moveTo(-width, getY(), duration * x / distance),
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

    }
}

