package ua.gram.munhauzen.interaction.balloons.animation;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;

import ua.gram.munhauzen.utils.Random;

public class ArcToAction extends TemporalAction {

    private float startX, startY;
    private float endX, endY;
    int depth;

    static public ArcToAction action(float x, float y, float duration) {
        ArcToAction action = Actions.action(ArcToAction.class);
        action.setPosition(x, y);
        action.setDuration(duration);

        return action;
    }

    @Override
    protected void begin() {
        startX = target.getX();
        startY = target.getY();
        depth = new Random().between(80, 180);
    }

    @Override
    protected void update(float percent) {

        float newX = startX + (endX - startX) * percent;
        float newY = startY + (endY - startY) * percent;

        newX = (float) (newX + depth * Math.sin(percent * 10));

        target.setPosition(newX, newY);
    }

    public void setPosition(float x, float y) {
        endX = x;
        endY = y;
    }

}