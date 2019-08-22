package ua.gram.munhauzen.screen.loading.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.interaction.balloons.action.MoveByTrajectoryAction;
import ua.gram.munhauzen.screen.loading.SimpleTrajectoryProvider;
import ua.gram.munhauzen.utils.MathUtils;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public abstract class NotRotatingObject extends RotatingObject {

    public NotRotatingObject(Texture texture) {
        super(texture);
    }

    @Override
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

        MoveByTrajectoryAction trajectoryAction = new MoveByTrajectoryAction(trajectory);
        trajectoryAction.setDuration(3f);

        int rotation = MathUtils.random(new Integer[]{
                -45, 45
        });

        clearActions();
        addAction(
                Actions.sequence(
                        Actions.visible(false),
                        Actions.moveTo(-width, 0),
                        Actions.visible(true),
                        Actions.parallel(
                                Actions.rotateBy(rotation, 3),
                                Actions.sequence(
                                        trajectoryAction,
                                        Actions.visible(false)
                                )
                        )
                )
        );

        updateTrajectoryCache();
    }
}
