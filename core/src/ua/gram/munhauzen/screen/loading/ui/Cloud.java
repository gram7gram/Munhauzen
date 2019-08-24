package ua.gram.munhauzen.screen.loading.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.utils.Random;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class Cloud extends Image {

    final float width, height, x, y;

    public Cloud(Texture texture, float x, float y) {

        super(texture);

        float originalW = getDrawable().getMinWidth();
        float originalH = getDrawable().getMinHeight();

        if (originalW < originalH) {
            width = MunhauzenGame.WORLD_WIDTH * .2f;
            float scale = 1f * width / originalW;
            height = 1f * originalH * scale;
        } else {
            height = MunhauzenGame.WORLD_HEIGHT * .08f;
            float scale = 1f * height / originalH;
            width = 1f * originalW * scale;
        }

        this.x = x;
        this.y = y;
    }

    public void start() {

        Random r = new Random();

        layout();

        clearActions();
        addAction(
                Actions.forever(
                        Actions.sequence(
                                Actions.visible(false),
                                Actions.delay(r.between(0, 2)),
                                Actions.moveTo(-width, y),
                                Actions.visible(true),
                                Actions.moveTo(MunhauzenGame.WORLD_WIDTH, y, r.between(5, 10)),
                                Actions.delay(r.between(1, 2))
                        )
                )
        );
    }

    @Override
    public void layout() {
        super.layout();

        setSize(width, height);
    }
}
