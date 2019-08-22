package ua.gram.munhauzen.screen.loading.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.interaction.balloons.action.MoveByTrajectoryAction;
import ua.gram.munhauzen.screen.loading.SimpleTrajectoryProvider;
import ua.gram.munhauzen.utils.MathUtils;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public abstract class RotatingObject extends Image {

    float width, height;
    CatmullRomSpline<Vector2> trajectory;
    Vector2[] dataSet;
    Vector2[] pointCache;

    public RotatingObject(Texture texture) {

        super(texture);

        setVisible(false);
    }

    public void start() {

        layout();

        dataSet = SimpleTrajectoryProvider.obtain();

        int direction = MathUtils.random(new Integer[]{
                -1, 1
        });

        for (int i = 0; i < dataSet.length; i++) {
            Vector2 vector2 = dataSet[i];

            if (direction > 0) {
                if (i == 0) {
                    vector2.x = -width;
                } else {
                    vector2.x *= MunhauzenGame.WORLD_WIDTH / 100f;
                }
            } else {
                if (i == 0) {
                    vector2.x = MunhauzenGame.WORLD_WIDTH;
                } else {
                    vector2.x = (100 - vector2.x) * MunhauzenGame.WORLD_WIDTH / 100f;
                }
            }

            vector2.y *= MunhauzenGame.WORLD_HEIGHT / 100f;
        }

        trajectory = new CatmullRomSpline<>(dataSet, false);

        int rotation = MathUtils.random(new Integer[]{
                -25, 25
        });

        MoveByTrajectoryAction trajectoryAction = new MoveByTrajectoryAction(trajectory);
        trajectoryAction.setDuration(3f);

        clearActions();
        addAction(
                Actions.sequence(
                        Actions.visible(false),
                        Actions.rotateTo(0),
                        Actions.moveTo(-width, 0),
                        Actions.visible(true),
                        Actions.parallel(
                                Actions.forever(
                                        Actions.rotateBy(rotation, .1f)
                                ),
                                Actions.sequence(
                                        trajectoryAction,
                                        Actions.visible(false)
                                )
                        )
                )
        );

        updateTrajectoryCache();
    }

    protected void updateTrajectoryCache() {
        int size = trajectory.controlPoints.length * 10;
        pointCache = new Vector2[size];
        for (int i = 0; i < size; i++) {
            pointCache[i] = new Vector2();
            trajectory.valueAt(pointCache[i], ((float) i) / ((float) size - 1));
        }
    }

    @Override
    public void layout() {
        super.layout();

        setOrigin(Align.center);

        setSize(width, height);
    }
}