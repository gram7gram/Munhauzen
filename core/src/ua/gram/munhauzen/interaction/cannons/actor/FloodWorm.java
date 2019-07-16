package ua.gram.munhauzen.interaction.cannons.actor;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import ua.gram.munhauzen.interaction.cannons.fragment.CannonsImageFragment;

public class FloodWorm extends Image {

    final CannonsImageFragment fragment;

    public FloodWorm(Texture texture, CannonsImageFragment fragment) {
        super(texture);

        this.fragment = fragment;
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        float width = fragment.backgroundWidth * .519f;
        float height = fragment.backgroundHeight * .499f;

        float topLeftX = fragment.background.getX() + fragment.backgroundWidth * .461f;
        float topLeftY = fragment.background.getY() + fragment.backgroundHeight * .5f;

        setBounds(topLeftX, topLeftY - height, width, height);
    }
}
