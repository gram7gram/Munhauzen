package ua.gram.munhauzen.interaction.cannons.actor;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import ua.gram.munhauzen.interaction.cannons.fragment.CannonsImageFragment;

public class EatWorm extends Image {

    final CannonsImageFragment fragment;

    public EatWorm(Texture texture, CannonsImageFragment fragment) {
        super(texture);

        setTouchable(Touchable.disabled);

        this.fragment = fragment;
    }

    public void updateBounds() {

        float width = fragment.backgroundImage.backgroundWidth * .087f;
        float height = fragment.backgroundImage.backgroundHeight * .102f;

        float topLeftX = fragment.backgroundImage.background.getX() + fragment.backgroundImage.backgroundWidth * .166f;
        float topLeftY = fragment.backgroundImage.background.getY() + fragment.backgroundImage.backgroundHeight * .44f;

        setBounds(topLeftX, topLeftY - height, width, height);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        updateBounds();
    }
}
