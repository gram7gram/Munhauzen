package ua.gram.munhauzen.interaction.horn.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;

import ua.gram.munhauzen.MunhauzenGame;

public class Note1 extends Image {

    float height, width;

    public Note1() {
        super();

        setOrigin(Align.bottom);
        setRotation(25);
        setVisible(false);
    }

    public void start() {

        float startX = MunhauzenGame.WORLD_WIDTH * .86f - width * .5f;
        float startY = -height;

        float endX = -width - width * .5f;
        float endY = MunhauzenGame.WORLD_HEIGHT * .7f;

        float rotation = 25;

        setVisible(false);
        addAction(Actions.sequence(
                Actions.rotateTo(rotation),
                Actions.moveTo(startX, startY),
                Actions.delay(2),
                Actions.forever(
                        Actions.sequence(
                                Actions.moveTo(startX, startY),
                                Actions.visible(true),
                                Actions.moveTo(endX, endY, 5.5f),
                                Actions.visible(false)
                        )
                )
        ));
    }

    public void setBackground(Texture texture) {

        setDrawable(new SpriteDrawable(new Sprite(texture)));

        height = MunhauzenGame.WORLD_HEIGHT * .65f;
        float scale = 1f * height / getDrawable().getMinHeight();
        width = 1f * getDrawable().getMinWidth() * scale;

        setSize(width, height);
    }
}
