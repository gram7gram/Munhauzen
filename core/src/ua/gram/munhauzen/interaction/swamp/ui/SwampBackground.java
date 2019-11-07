package ua.gram.munhauzen.interaction.swamp.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.interaction.SwampInteraction;

public class SwampBackground extends Image {

    final SwampInteraction interaction;
    float width, height, x, y;

    public SwampBackground(SwampInteraction interaction) {

        setTouchable(Touchable.disabled);

        this.interaction = interaction;

        setBackground(
                interaction.assetManager.get("swamp/int_swamp_1.jpg", Texture.class),
                "swamp/int_swamp_1.jpg"
        );
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        setBounds(x, y, width, height);
    }

    public void setBackground(Texture texture, String file) {

        interaction.gameScreen.hideImageFragment();

        SpriteDrawable drawable = new SpriteDrawable(new Sprite(texture));

        setDrawable(drawable);

        interaction.gameScreen.setLastBackground(file);

        width = MunhauzenGame.WORLD_WIDTH;
        float scale = 1f * width / drawable.getMinWidth();
        height = 1f * drawable.getMinHeight() * scale;

        x = 0;
        y = (MunhauzenGame.WORLD_HEIGHT - height) / 2f;

    }
}
