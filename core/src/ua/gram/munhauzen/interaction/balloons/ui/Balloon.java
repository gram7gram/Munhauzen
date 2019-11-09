package ua.gram.munhauzen.interaction.balloons.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.Align;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.interaction.BalloonsInteraction;
import ua.gram.munhauzen.interaction.balloons.ComplexTrajectoryProvider;
import ua.gram.munhauzen.interaction.balloons.SimpleTrajectoryProvider;
import ua.gram.munhauzen.interaction.balloons.action.MoveByTrajectoryAction;
import ua.gram.munhauzen.interaction.balloons.animation.ArcToAction;
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
    final float width, height;

    public Balloon(BalloonsInteraction interaction, Texture texture, int width, int height) {
        super(texture);

        this.interaction = interaction;
        this.width = width;
        this.height = height;

        setOrigin(Align.center);

        r = new Random();
    }

    public void onHit() {
        clearActions();
        isLocked = false;

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

        setTouchable(Touchable.enabled);
        isLocked = true;

        SequenceAction sequenceAction = new SequenceAction();
        sequenceAction.addAction(Actions.alpha(1));
        sequenceAction.addAction(Actions.visible(true));

        dataSet = SimpleTrajectoryProvider.obtain();

        for (Vector2 vector2 : dataSet) {
            vector2.x *= MunhauzenGame.WORLD_WIDTH / 100f;
            vector2.y *= MunhauzenGame.WORLD_HEIGHT / 100f;
        }

        Vector2 start = dataSet[0];
        Vector2 end = dataSet[dataSet.length - 1];

        sequenceAction.addAction(ArcToAction.action(start.x, start.y, 0));
        sequenceAction.addAction(ArcToAction.action(end.x, end.y, 2.5f));

        addAction(sequenceAction);
    }

    public void startFast() {

        reset();

        setTouchable(Touchable.enabled);
        isLocked = true;

        SequenceAction sequenceAction = new SequenceAction();

        sequenceAction.addAction(Actions.delay(r.between(3, 10)));
        sequenceAction.addAction(Actions.alpha(1));
        sequenceAction.addAction(Actions.visible(true));

        dataSet = ComplexTrajectoryProvider.obtain();

        for (Vector2 vector2 : dataSet) {
            vector2.x *= MunhauzenGame.WORLD_WIDTH / 100f;
            vector2.y *= MunhauzenGame.WORLD_HEIGHT / 100f;
        }

        trajectory = new CatmullRomSpline<>(dataSet, false);

        MoveByTrajectoryAction trajectoryAction = new MoveByTrajectoryAction(trajectory);
        trajectoryAction.setDuration(8f);

        sequenceAction.addAction(trajectoryAction);

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
    public void layout() {
        super.layout();

        setSize(width, height);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if (getY() >= MunhauzenGame.WORLD_HEIGHT) {

            if (onMiss != null) {
                onMiss.run();
            }

            reset();
        }
    }
}
