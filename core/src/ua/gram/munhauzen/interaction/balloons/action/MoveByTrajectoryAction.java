package ua.gram.munhauzen.interaction.balloons.action;

import com.badlogic.gdx.math.Path;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;

public class MoveByTrajectoryAction extends MoveToAction {

    final Path<Vector2> path;
    final Vector2 pos;

    public MoveByTrajectoryAction(Path<Vector2> path) {
        this.path = path;
        pos = new Vector2();
    }

    @Override
    protected void update(float percent) {

        path.valueAt(pos, percent);

        target.setPosition(pos.x, pos.y);
    }
}
