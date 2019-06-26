package ua.gram.munhauzen.interaction.puzzle;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.interaction.PuzzleInteraction;
import ua.gram.munhauzen.ui.FitImage;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public abstract class PuzzleItem extends FitImage {

    final String tag = getClass().getSimpleName();
    PuzzleInteraction interaction;

    Rectangle bounds;

    public PuzzleItem(PuzzleInteraction interaction, Texture texture) {
        super(texture);

        this.interaction = interaction;

        bounds = new Rectangle();
    }

    public void init() {

        interaction.imageFragment.setSizeRelativeToBackground(this, getDrawable());

        clear();

        final PuzzleItem actor = this;

        addListener(new ActorGestureListener() {

            boolean isInBounds;

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);

                actor.init();

                if (isInBounds) {
                    addPuzzle();
                }

                isInBounds = false;
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

    @Override
    public void act(float delta) {
        super.act(delta);

        bounds.set(getX(), getY(), getWidth(), getHeight());
    }

    private void addPuzzle() {

        String name = getClass().getSimpleName().toLowerCase();

        try {
            if (interaction.decisionManager.items.contains(name)) {
                interaction.decisionManager.items.remove(name);
            } else {
                interaction.decisionManager.items.add(name);
            }

            interaction.decisionManager.decide();
        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }
}
