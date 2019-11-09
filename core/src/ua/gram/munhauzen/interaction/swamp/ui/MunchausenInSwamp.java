package ua.gram.munhauzen.interaction.swamp.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Timer;

import ua.gram.munhauzen.interaction.SwampInteraction;
import ua.gram.munhauzen.interaction.swamp.fragment.SwampImageFragment;
import ua.gram.munhauzen.utils.Log;
import ua.gram.munhauzen.utils.Random;

public class MunchausenInSwamp extends Image {

    final String tag = getClass().getSimpleName();
    final SwampImageFragment fragment;

    final Random r = new Random();

    Timer.Task task;

    public boolean isDragging, isCompleted;
    public float bottomBound, limit;

    public MunchausenInSwamp(SwampInteraction interaction) {

        this.fragment = interaction.imageFragment;

        addCaptureListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                if (!isTouchable()) {
                    event.cancel();
                    return true;
                }

                return false;
            }
        });

        addListener(new ActorGestureListener() {

            @Override
            public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
                super.touchDown(event, x, y, pointer, button);

                fragment.playPull();
            }

            @Override
            public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
                super.pan(event, x, y, deltaX, deltaY);

                if (isCompleted) {
                    clearListeners();
                    return;
                }

                if (deltaY <= 0) return;

                isDragging = true;

                try {
                    float newY = Math.min(limit, getY() + deltaY);

                    setY(newY);

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

        float width = swampBackground.width * numbers[0] / 100;
        float height = swampBackground.height * numbers[1] / 100;
        float x = swampBackground.getX()
                + swampBackground.width * numbers[2] / 100;
        float y = swampBackground.getY()
                + (swampBackground.height * numbers[3] / 100);

        bottomBound = y;
        limit = fragment.swamp.y + fragment.swamp.height;

        setSize(width, height);

        if (!isDragging && !isCompleted) {
            setPosition(x, y);
        }
    }

    private float[] getPercentBounds() {
        return new float[]{
                49.44f, 65.04f, 17.00f, -25.24f
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

        isDragging = false;
        isCompleted = false;

        cancelTask();

        task = Timer.instance().scheduleTask(new Timer.Task() {
            @Override
            public void run() {

                if (!isDragging) return;

                float newY = getY() - r.between(6, 10);

                setY(newY);

                if (newY < bottomBound) {
                    setY(bottomBound);
                    fragment.pausePull();
                    isDragging = false;
                    isCompleted = false;
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

        isCompleted = true;
        isDragging = false;

        clear();

        cancelTask();

        setY(limit);

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
