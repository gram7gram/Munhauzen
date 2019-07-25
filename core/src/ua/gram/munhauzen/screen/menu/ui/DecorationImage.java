package ua.gram.munhauzen.screen.menu.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Image;

import ua.gram.munhauzen.MunhauzenGame;

public abstract class DecorationImage extends Image {

    public final String resource;

    public DecorationImage(String resource) {
        super();
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
