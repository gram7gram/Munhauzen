package ua.gram.munhauzen.interaction.balloons.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.Align;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.interaction.BalloonsInteraction;
import ua.gram.munhauzen.interaction.balloons.ComplexTrajectoryProvider;
import ua.gram.munhauzen.interaction.balloons.SimpleTrajectoryProvider;
import ua.gram.munhauzen.interaction.balloons.action.MoveByTrajectoryAction;
import ua.gram.munhauzen.ui.FitImage;
import ua.gram.munhauzen.utils.Random;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class Balloon extends FitImage {

    final BalloonsInteraction interaction;
    public boolean isLocked;
    public Runnable onMiss;
    final Random r;
    CatmullRomSpline<Vector2> trajectory;
    Vector2[] dataSet;
    Vector2[] pointCache;

    public Balloon(BalloonsInteraction interaction, Texture texture, int width, int height) {
        super(texture);

        this.interaction = interaction;

        setOrigin(Align.center);

        setSize(width, height);

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
        setScale(1);
        setPosition(-getWidth(), -getHeight());
        isLocked = false;

        dataSet = null;
        trajectory = null;
        pointCache = null;
    }

    public void start() {

        reset();

        isLocked = true;

        SequenceAction sequenceAction = new SequenceAction();

        dataSet = SimpleTrajectoryProvider.obtain();

        for (Vector2 vector2 : dataSet) {
            vector2.x *= MunhauzenGame.WORLD_WIDTH / 100f;
            vector2.y *= MunhauzenGame.WORLD_HEIGHT / 100f;
        }

        trajectory = new CatmullRomSpline<>(dataSet, false);

        MoveByTrajectoryAction trajectoryAction = new MoveByTrajectoryAction(trajectory);
        trajectoryAction.setDuration(4f);

        sequenceAction.addAction(Actions.alpha(1));
        sequenceAction.addAction(Actions.run(new Runnable() {
            @Override
            public void run() {
                setVisible(true);
            }
        }));
        sequenceAction.addAction(trajectoryAction);
        sequenceAction.addAction(Actions.run(new Runnable() {
            @Override
            public void run() {
                isLocked = false;

                reset();

                if (onMiss != null) {
                    addAction(Actions.run(onMiss));
                }
            }
        }));

        addAction(sequenceAction);

        updateTrajectoryCache();
    }

    public void startFast(Runnable task) {

        reset();

        isLocked = true;

        SequenceAction sequenceAction = new SequenceAction();

        sequenceAction.addAction(Actions.delay(r.between(3, 10)));
        sequenceAction.addAction(Actions.run(task));

        dataSet = ComplexTrajectoryProvider.obtain();

        for (Vector2 vector2 : dataSet) {
            vector2.x *= MunhauzenGame.WORLD_WIDTH / 100f;
            vector2.y *= MunhauzenGame.WORLD_HEIGHT / 100f;
        }

        trajectory = new CatmullRomSpline<>(dataSet, false);

        MoveByTrajectoryAction trajectoryAction = new MoveByTrajectoryAction(trajectory);
        trajectoryAction.setDuration(10f);

        sequenceAction.addAction(Actions.alpha(1));
        sequenceAction.addAction(Actions.run(new Runnable() {
            @Override
            public void run() {
                setVisible(true);
            }
        }));
        sequenceAction.addAction(trajectoryAction);
        sequenceAction.addAction(Actions.run(new Runnable() {
            @Override
            public void run() {
                isLocked = false;

                reset();

                if (onMiss != null) {
                    addAction(Actions.run(onMiss));
                }
            }
        }));

        addAction(sequenceAction);

        updateTrajectoryCache();
    }

    private void updateTrajectoryCache() {
        int size = trajectory.controlPoints.length * 10;
        pointCache = new Vector2[size];
        for (int i = 0; i < size; i++) {
            pointCache[i] = new Vector2();
            trajectory.valueAt(pointCache[i], ((float) i) / ((float) size - 1));
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        if (isVisible()) {

            batch.end();

            if (interaction.shapeRenderer != null && pointCache != null && dataSet != null) {
                interaction.shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

                interaction.shapeRenderer.setColor(Color.RED);
                for (int i = 0; i < pointCache.length - 1; i++) {
                    interaction.shapeRenderer.line(pointCache[i], pointCache[i + 1]);
                }

                interaction.shapeRenderer.setColor(Color.BLUE);
                for (int i = 1; i < dataSet.length; i++) {
                    interaction.shapeRenderer.line(dataSet[i - 1], dataSet[i]);
                }

                interaction.shapeRenderer.end();
            }

            batch.begin();
        }
    }
}
