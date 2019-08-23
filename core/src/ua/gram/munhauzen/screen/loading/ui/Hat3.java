package ua.gram.munhauzen.screen.loading.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;

import ua.gram.munhauzen.MunhauzenGame;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class Hat3 extends Hat1 implements Hat {

    final float width, height;

    public Hat3(Texture texture) {

        super(texture);

        float originalW = getDrawable().getMinWidth();
        float originalH = getDrawable().getMinHeight();

        if (originalW < originalH) {
            width = MunhauzenGame.WORLD_WIDTH * .25f;
            float scale = 1f * width / originalW;
            height = 1f * originalH * scale;
        } else {
            height = MunhauzenGame.WORLD_HEIGHT * .1f;
            float scale = 1f * height / originalH;
            width = 1f * originalW * scale;
        }
    }

    public void start() {

        float startX = MunhauzenGame.WORLD_WIDTH - width - 120;
        setOrigin(Align.center);

        clearActions();
        addAction(
                Actions.sequence(
                        Actions.visible(false),
                        Actions.moveTo(startX, -height),
                        Actions.rotateTo(0),
                        Actions.visible(true),
                        Actions.parallel(
                                Actions.forever(
                                        Actions.rotateBy(30, .2f)
                                ),
                                Actions.sequence(
                                        Actions.moveTo(startX - 20, MunhauzenGame.WORLD_HEIGHT / 2f, 1, Interpolation.fastSlow),
                                        Actions.moveTo(startX - 50, -height, 1, Interpolation.slowFast),
                                        Actions.visible(false)
                                )
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
