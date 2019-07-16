package ua.gram.munhauzen.interaction.cannons.actor;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import ua.gram.munhauzen.interaction.cannons.fragment.CannonsImageFragment;

public class BurnWorm extends Image {

    final CannonsImageFragment fragment;

    public BurnWorm(Texture texture, CannonsImageFragment fragment) {
        super(texture);

        this.fragment = fragment;
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        float width = fragment.backgroundWidth * .327f;
        float height = fragment.backgroundHeight * .478f;

        float topLeftX = fragment.background.getX() + fragment.backgroundWidth * .259f;
        float topLeftY = fragment.background.getY() + fragment.backgroundHeight * .016f;

        setBounds(topLeftX, topLeftY - height, width, height);
    }
}
