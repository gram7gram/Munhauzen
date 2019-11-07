package ua.gram.munhauzen.interaction.swamp.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

import ua.gram.munhauzen.interaction.SwampInteraction;

public class Swamp extends Image {

    final SwampInteraction interaction;

    public Swamp(SwampInteraction interaction) {

        setTouchable(Touchable.disabled);

        this.interaction = interaction;

        setBackground(
                interaction.assetManager.get("swamp/int_swamp_3.png", Texture.class)
        );
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        SwampBackground backgroundImage = interaction.imageFragment.swampBackground;

        float[] numbers = getPercentBounds();

        float width = backgroundImage.width * numbers[0] / 100;
        float height = backgroundImage.height * numbers[1] / 100;
        float x = backgroundImage.getX()
                + backgroundImage.width * numbers[2] / 100;
        float y = backgroundImage.getY()
                + (backgroundImage.height * (100 - numbers[3]) / 100)
                - height;

        setBounds(x, y, width, height);
    }

    private float[] getPercentBounds() {
        return new float[]{
                100.00f, 29.62f, 0.00f, 70.38f
        };
    }

    public void setBackground(Texture texture) {
        setDrawable(new SpriteDrawable(new Sprite(texture)));
    }
}
