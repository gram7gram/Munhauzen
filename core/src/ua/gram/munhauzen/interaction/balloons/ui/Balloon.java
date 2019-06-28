package ua.gram.munhauzen.interaction.balloons.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.animation.ArcToAction;
import ua.gram.munhauzen.ui.FitImage;
import ua.gram.munhauzen.utils.Random;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class Balloon extends FitImage {

    public boolean isLocked;
    public Runnable onMiss;
    Array<Vector2> coordinates;
    Random r;

    public Balloon(Texture texture, int width, int height) {
        super(texture);
        setOrigin(Align.center);

        setSize(width, height);

        r = new Random();

        reset();
    }

    public void onHit() {

        addAction(Actions.sequence(
                Actions.parallel(
                        Actions.scaleBy(1.1f, 1.1f, .3f),
                        Actions.alpha(0, .5f)
                ),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        reset();
                    }
                })
        ));
    }

    public void reset() {
        clearActions();
        setVisible(false);
        setScale(1);
        setPosition(-getWidth(), -getHeight());
        isLocked = false;
    }

    public void start(boolean isSuperFast) {

        clearActions();

        isLocked = true;

        setVisible(true);

        Runnable onComplete = new Runnable() {
            @Override
            public void run() {
                isLocked = false;

                if (onMiss != null) {
                    addAction(Actions.run(onMiss));
                }
            }
        };

        if (isSuperFast) {
            addAction(Actions.sequence(
                    Actions.alpha(1),

                    Actions.delay(new Random().between(3, 10)),

                    trajectory(1.5f),

                    Actions.run(onComplete)
            ));
        } else {
            addAction(Actions.sequence(
                    Actions.alpha(1),

                    trajectory(3),

                    Actions.run(onComplete)
            ));
        }
    }

    private Action trajectory(float duration) {

        SequenceAction action = new SequenceAction();

        coordinates = coordinates();

        for (int i = 0; i < coordinates.size; i++) {
            Vector2 coordinate = coordinates.get(i);

            action.addAction(ArcToAction.action(coordinate.x, coordinate.y, i == 0 ? 0 : duration));
        }

        return action;
    }

    private Array<Vector2> coordinates() {

        Array<Vector2> coords = new Array<>();

        coords.add(new Vector2(r.between(0, (int) (MunhauzenGame.WORLD_WIDTH - getWidth())), 0));

        coords.add(new Vector2(r.between(0, (int) (MunhauzenGame.WORLD_WIDTH - getWidth())), MunhauzenGame.WORLD_HEIGHT));

        return coords;
    }
}
