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

        float originalW = getDrawable().getMinWidth();
        float originalH = getDrawable().getMinHeight();

        if (originalW < originalH) {
            width = preferredWidth();
            float scale = 1f * width / originalW;
            height = 1f * originalH * scale;
        } else {
            height = preferredHeight();
            float scale = 1f * height / originalH;
            width = 1f * originalW * scale;
        }
    }

    protected float preferredWidth() {
        return MunhauzenGame.WORLD_WIDTH * .25f;
    }

    protected float preferredHeight() {
        return MunhauzenGame.WORLD_HEIGHT * .1f;
    }

    public void start() {

        layout();

        createDataset();

        int rotation = MathUtils.random(new Integer[]{
                -25, 25
        });

        MoveByTrajectoryAction trajectoryAction = new MoveByTrajectoryAction(trajectory);
        trajectoryAction.setDuration(MathUtils.random(new Float[]{
                5f, 4.8f, 4.5f
        }));

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

    protected void createDataset() {
        Vector2[] points = SimpleTrajectoryProvider.obtain();

        int direction = MathUtils.random(new Integer[]{
                -1, 1
        });

        dataSet = new Vector2[points.length + 2];

        //Show item outside the screen
        if (direction > 0) {
            dataSet[0] = new Vector2(-width, -height);
        } else {
            dataSet[0] = new Vector2(MunhauzenGame.WORLD_WIDTH + width, -height);
        }
        int i;
        for (i = 0; i < points.length; i++) {
            Vector2 vector2 = points[i];

            if (direction > 0) {
                vector2.x *= MunhauzenGame.WORLD_WIDTH / 100f;
            } else {
                vector2.x = (100 - vector2.x) * MunhauzenGame.WORLD_WIDTH / 100f;
            }

            vector2.y *= MunhauzenGame.WORLD_HEIGHT / 100f;

            dataSet[i + 1] = vector2;
        }

        //End item outside the screen
        if (direction > 0) {
            dataSet[i + 1] = new Vector2(MunhauzenGame.WORLD_WIDTH + width, -height);
        } else {
            dataSet[i + 1] = new Vector2(-width, -height);
        }

        trajectory = new CatmullRomSpline<>(dataSet, true);

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
