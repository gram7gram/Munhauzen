package ua.gram.munhauzen.interaction.puzzle;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.interaction.PuzzleInteraction;
import ua.gram.munhauzen.screen.game.ui.BackgroundImage;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public abstract class PuzzleItem extends Image {

    final String tag = getClass().getSimpleName();
    final PuzzleInteraction interaction;
    final BackgroundImage backgroundImage;

    boolean isDragging;
    Rectangle bounds;

    public PuzzleItem(PuzzleInteraction inter, Texture texture) {
        super(texture);

        this.interaction = inter;
        this.backgroundImage = inter.imageFragment.backgroundImage;

        bounds = new Rectangle();

        final PuzzleItem actor = this;

        addListener(new ActorGestureListener() {

            boolean isInBounds;

            @Override
            public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
                super.touchDown(event, x, y, pointer, button);

                isDragging = true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);

                if (isInBounds) {
                    interaction.imageFragment.dropzone.addPuzzle(tag);
                }

                isInBounds = false;
                isDragging = false;
            }

            @Override
            public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
                super.pan(event, x, y, deltaX, deltaY);

                try {
                    float newX = actor.getX() + deltaX;
                    float newY = actor.getY() + deltaY;

                    float rightBound = MunhauzenGame.WORLD_WIDTH - actor.getWidth();
                    float topBound = MunhauzenGame.WORLD_HEIGHT - actor.getHeight();

                    if (0 < newX && newX < rightBound) {
                        actor.setX(newX);
                    }
                    if (0 < newY && newY < topBound) {
                        actor.setY(newY);
                    }

                    if (actor.getX() < 0) actor.setX(0);
                    if (actor.getX() > rightBound) actor.setX(rightBound);
                    if (actor.getY() < 0) actor.setY(0);
                    if (actor.getY() > topBound) actor.setY(topBound);

                    isInBounds = actor.bounds.overlaps(interaction.imageFragment.dropzone.bounds);

                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }

        });
    }

    public abstract float[] getPercentBounds();

    @Override
    public void act(float delta) {
        super.act(delta);

        float[] numbers = getPercentBounds();

        float width = backgroundImage.backgroundWidth * numbers[0] / 100;
        float height = backgroundImage.backgroundHeight * numbers[1] / 100;
        float x = backgroundImage.background.getX() + backgroundImage.backgroundWidth * numbers[2] / 100;
        float y = backgroundImage.background.getY() + (backgroundImage.backgroundHeight * (100 - numbers[3]) / 100) - height;

        setSize(width, height);

        if (!isDragging) {
            setPosition(x, y);
        }

        bounds.set(getX(), getY(), getWidth(), getHeight());
    }
}
