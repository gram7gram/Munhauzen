package ua.gram.munhauzen.screen.menu.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Image;

public abstract class DecorationImage extends Image {

    public final BackgroundImage backgroundImage;
    public final String resource;

    public DecorationImage(BackgroundImage backgroundImage, String resource) {
        super();
        this.backgroundImage = backgroundImage;
        this.resource = resource;
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        float[] bounds = getPercentBounds();

        float width = bounds[0] / 100 * backgroundImage.backgroundWidth;
        float height = bounds[1] / 100 * backgroundImage.backgroundHeight;
        float x = bounds[2] / 100 * backgroundImage.backgroundWidth;
        float y = (100 - bounds[3]) / 100 * backgroundImage.backgroundHeight;

        setBounds(
                backgroundImage.background.getX() + x,
                backgroundImage.background.getY() + y - height,
                width, height);
    }

    public abstract void init();

    public abstract boolean canBeDisplayed();

    public abstract float[] getPercentBounds();

}
