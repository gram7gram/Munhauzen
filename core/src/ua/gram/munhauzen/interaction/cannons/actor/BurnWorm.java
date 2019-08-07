package ua.gram.munhauzen.interaction.cannons.actor;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import ua.gram.munhauzen.interaction.cannons.fragment.CannonsImageFragment;

public class BurnWorm extends Image {

    final CannonsImageFragment fragment;

    public BurnWorm(Texture texture, CannonsImageFragment fragment) {
        super(texture);

        setTouchable(Touchable.disabled);

        this.fragment = fragment;
    }

    public void updateBounds() {
        float width = fragment.backgroundImage.backgroundWidth * .327f;
        float height = fragment.backgroundImage.backgroundHeight * .478f;

        float topLeftX = fragment.backgroundImage.background.getX() + fragment.backgroundImage.backgroundWidth * .259f;
        float topLeftY = fragment.backgroundImage.background.getY() + fragment.backgroundImage.backgroundHeight;

        setBounds(topLeftX, topLeftY - height, width, height);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        updateBounds();
    }
}
