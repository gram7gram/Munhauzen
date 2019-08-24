package ua.gram.munhauzen.screen.loading.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import ua.gram.munhauzen.MunhauzenGame;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class Balloon extends Image {

    final float width, height;

    public Balloon(Texture texture) {

        super(texture);

        float originalW = getDrawable().getMinWidth();
        float originalH = getDrawable().getMinHeight();

        if (originalW < originalH) {
            width = MunhauzenGame.WORLD_WIDTH * .35f;
            float scale = 1f * width / originalW;
            height = 1f * originalH * scale;
        } else {
            height = MunhauzenGame.WORLD_HEIGHT * .15f;
            float scale = 1f * height / originalH;
            width = 1f * originalW * scale;
        }
    }

    public void start() {


        clearActions();
        addAction(
                Actions.sequence(
                        Actions.moveTo(-width, MunhauzenGame.WORLD_HEIGHT - height - 80),
                        Actions.rotateTo(0),
                        Actions.visible(true),
                        Actions.parallel(
                                Actions.moveTo(MunhauzenGame.WORLD_WIDTH * .4f, MunhauzenGame.WORLD_HEIGHT - height - 70, 2),
                                Actions.repeat(2, Actions.sequence(
                                        Actions.rotateBy(5, .25f),
                                        Actions.rotateBy(-5, .25f),
                                        Actions.rotateBy(-5, .25f),
                                        Actions.rotateBy(5, .25f)
                                ))
                        ),
                        Actions.parallel(
                                Actions.rotateTo(-60, .5f),
                                Actions.moveBy(60, -10, .5f)
                        ),
                        Actions.parallel(
                                Actions.moveTo(MunhauzenGame.WORLD_WIDTH - width, -height, 3),
                                Actions.repeat(3, Actions.sequence(
                                        Actions.rotateBy(15, .25f),
                                        Actions.rotateBy(-15, .25f),
                                        Actions.rotateBy(-15, .25f),
                                        Actions.rotateBy(15, .25f)
                                ))
                        ),
                        Actions.visible(false)
                )
        );
    }

    @Override
    public void layout() {
        super.layout();

        setSize(width, height);
    }
}
