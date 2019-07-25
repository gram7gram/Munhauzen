package ua.gram.munhauzen.screen.menu.ui;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.animation.AnimatedImage;

public abstract class DecorationAnimation extends AnimatedImage {

    public final String resource;

    public DecorationAnimation(String resource) {
        super(null);
        this.resource = resource;
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        float[] bounds = getPercentBounds();

        float width = bounds[0] / 100 * MunhauzenGame.WORLD_WIDTH;
        float height = bounds[1] / 100 * MunhauzenGame.WORLD_HEIGHT;
        float x = bounds[2] / 100 * MunhauzenGame.WORLD_WIDTH;
        float y = bounds[3] / 100 * MunhauzenGame.WORLD_HEIGHT;

        setBounds(x, y - height, width, height);
    }

    public abstract void init();

    public abstract boolean canBeDisplayed();

    public abstract float[] getPercentBounds();

}
