package ua.gram.munhauzen.interaction.swamp.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.interaction.SwampInteraction;
import ua.gram.munhauzen.ui.FitImage;

public class SwampBackground extends Group {

    final SwampInteraction interaction;

    public final Image background;
    public final Table backgroundTable;
    public float backgroundWidth, backgroundHeight, backgroundScale;

    public SwampBackground(SwampInteraction interaction) {

        setTouchable(Touchable.disabled);

        this.interaction = interaction;

        background = new FitImage();

        backgroundTable = new Table();
        backgroundTable.setTouchable(Touchable.childrenOnly);
        backgroundTable.setFillParent(true);
        backgroundTable.add(background).center().expand().fill();

        addActor(backgroundTable);

        setBackground(
                interaction.assetManager.get("swamp/int_swamp_1.jpg", Texture.class),
                "swamp/int_swamp_1.jpg"
        );
    }

    public void setBackground(Texture texture, String file) {

        interaction.gameScreen.hideImageFragment();

        SpriteDrawable drawable = new SpriteDrawable(new Sprite(texture));

        background.setDrawable(drawable);

        backgroundWidth = MunhauzenGame.WORLD_WIDTH;
        backgroundScale = 1f * backgroundWidth / drawable.getMinWidth();
        backgroundHeight = 1f * drawable.getMinHeight() * backgroundScale;

        backgroundTable.getCell(background)
                .width(backgroundWidth)
                .height(backgroundHeight);

        interaction.gameScreen.setLastBackground(file);

    }
}
