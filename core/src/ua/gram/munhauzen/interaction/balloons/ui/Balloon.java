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

    public Balloon(Texture texture, int width, int height, float x, float y) {
        super(texture);
        setOrigin(Align.center);

        setSize(width, height);
        setPosition(0, 0);
        r = new Random();
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
        isLocked = false;
    }

    public void start(boolean isSuperFast) {

        clearActions();

        isLocked = true;

        setVisible(true);

        addAction(
                Actions.sequence(
                        trajectory(),

                        Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                isLocked = false;

                                if (onMiss != null) {
                                    addAction(Actions.run(onMiss));
                                }
                            }
                        })
                )

        );

    }

    private Action trajectory() {

        SequenceAction action = new SequenceAction();

        coordinates = coordinates();

        for (int i = 0; i < coordinates.size; i++) {
            Vector2 coordinate = coordinates.get(i);

            action.addAction(ArcToAction.action(coordinate.x, coordinate.y, i == 0 ? 0 : 3));
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
