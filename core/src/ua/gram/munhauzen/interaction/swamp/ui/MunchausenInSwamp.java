package ua.gram.munhauzen.interaction.swamp.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Timer;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.interaction.SwampInteraction;
import ua.gram.munhauzen.interaction.swamp.fragment.SwampImageFragment;
import ua.gram.munhauzen.utils.Log;
import ua.gram.munhauzen.utils.Random;

public class MunchausenInSwamp extends Image {

    final String tag = getClass().getSimpleName();
    final SwampImageFragment fragment;

    final Random r = new Random();

    Timer.Task task;

    boolean isDragging;
    public float bottomBound, topBound, winLimit;

    public MunchausenInSwamp(SwampInteraction interaction) {

        this.fragment = interaction.imageFragment;

        addListener(new ActorGestureListener() {

            @Override
            public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
                super.touchDown(event, x, y, pointer, button);

                fragment.playPull();
            }

            @Override
            public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
                super.pan(event, x, y, deltaX, deltaY);

                if (deltaY <= 0) return;

                isDragging = true;

                try {
                    float newY = getY() + deltaY;

                    if (newY < topBound) {
                        setY(newY);
                    }

                    fragment.checkIfWinner();

                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }
        });

        setBackground(
                interaction.assetManager.get("swamp/int_swamp_2.png", Texture.class)
        );
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        SwampBackground swampBackground = fragment.swampBackground;

        float[] numbers = getPercentBounds();

        float width = swampBackground.backgroundWidth * numbers[0] / 100;
        float height = swampBackground.backgroundHeight * numbers[1] / 100;
        float x = swampBackground.background.getX()
                + swampBackground.backgroundWidth * numbers[2] / 100;
        float y = swampBackground.background.getY()
                + (swampBackground.backgroundHeight * numbers[3] / 100) - height;

        bottomBound = y;
        topBound = MunhauzenGame.WORLD_HEIGHT - height;
        winLimit = topBound * .9f;

        setSize(width, height);

        if (!isDragging) {
            setPosition(x, y);
        }
    }

    private float[] getPercentBounds() {
        return new float[]{
                49.4371f, 65.0386f, 15.9374f, 32f
        };
    }

    public void setBackground(Texture texture) {

        setDrawable(new SpriteDrawable(new Sprite(texture)));

        addAction(Actions.sequence(
                Actions.alpha(0),
                Actions.delay(.5f),
                Actions.alpha(1, .2f)
        ));
    }

    public void enableGravity() {

        task = Timer.instance().scheduleTask(new Timer.Task() {
            @Override
            public void run() {

                if (!isDragging) return;

                float newY = getY() - r.between(6, 10);

                setY(newY);

                if (getY() < bottomBound) {
                    setY(bottomBound);
                    fragment.pausePull();
                    isDragging = false;
                }

            }
        }, 0, .01f);
    }

    public void cancelTask() {
        if (task == null) return;

        task.cancel();
        task = null;
    }

    public void complete() {

        clear();

        cancelTask();

        addAction(Actions.repeat(10,
                Actions.sequence(
                        Actions.moveBy(5, 0, .1f),
                        Actions.moveBy(0, -5, .1f),
                        Actions.moveBy(-5, 0, .1f),
                        Actions.moveBy(0, 5, .1f)
                )
        ));
    }
}
