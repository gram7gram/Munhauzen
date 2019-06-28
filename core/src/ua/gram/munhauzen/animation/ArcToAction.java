package ua.gram.munhauzen.animation;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;

import ua.gram.munhauzen.utils.Random;


public class ArcToAction extends TemporalAction {

    private float startX, startY;
    private float endX, endY;
    int offset2, offset1;

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
        Random r = new Random();
        offset1 = r.between(80, 180);
        offset2 = r.between(5, 12);

        int seed = r.between(-1, 1);
        if (seed != 0) offset1 += seed;
    }

    @Override
    protected void update(float percent) {

        float newX = startX + (endX - startX) * percent;
        float newY = startY + (endY - startY) * percent;

        newX = (float) (newX + offset1 * Math.sin(percent * offset2));

        target.setPosition(newX, newY);
    }

    public void setPosition(float x, float y) {
        endX = x;
        endY = y;
    }

}