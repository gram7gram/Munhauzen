package ua.gram.munhauzen.interaction.swamp.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.interaction.swamp.fragment.SwampImageFragment;

public class Overlay extends Image {

    final SwampImageFragment fragment;

    public Overlay(SwampImageFragment fragment, Texture img) {
        super(img);
        this.fragment = fragment;
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        setVisible(fragment.swamp.y > 0);

        setBounds(
                0, fragment.swamp.y - MunhauzenGame.WORLD_HEIGHT,
                MunhauzenGame.WORLD_WIDTH, MunhauzenGame.WORLD_HEIGHT);
    }
}
