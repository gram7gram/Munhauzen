package ua.gram.munhauzen.screen.loading.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import ua.gram.munhauzen.interaction.balloons.action.MoveByTrajectoryAction;
import ua.gram.munhauzen.utils.MathUtils;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public abstract class SlowRotatingObject extends RotatingObject {

    public SlowRotatingObject(Texture texture) {
        super(texture);
    }

    @Override
    public void start() {

        layout();

        createDataset();

        MoveByTrajectoryAction trajectoryAction = new MoveByTrajectoryAction(trajectory);
        trajectoryAction.setDuration(MathUtils.random(new Float[]{
                5f, 4.8f, 4.5f
        }));

        int rotation = MathUtils.random(new Integer[]{
                -45, 45
        });

        clearActions();
        addAction(
                Actions.sequence(
                        Actions.visible(false),
                        Actions.rotateTo(0),
                        Actions.moveTo(-width, 0),
                        Actions.visible(true),
                        Actions.parallel(
                                Actions.rotateBy(rotation, 3),
                                Actions.sequence(
                                        trajectoryAction,
                                        Actions.visible(false),
                                        Actions.run(new Runnable() {
                                            @Override
                                            public void run() {
                                                trajectory = null;
                                                dataSet = null;
                                                pointCache = null;
                                            }
                                        })
                                )
                        )
                )
        );
    }
}
