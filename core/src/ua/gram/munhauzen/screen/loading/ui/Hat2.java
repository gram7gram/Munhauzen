package ua.gram.munhauzen.screen.loading.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;

import ua.gram.munhauzen.MunhauzenGame;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class Hat2 extends Image {

    final float width, height;

    public Hat2(Texture texture) {

        super(texture);

        this.width = 244;
        this.height = 100;
    }

    public void start() {

        float startX = MunhauzenGame.WORLD_WIDTH - width;
        setOrigin(Align.center);

        clearActions();
        addAction(
                Actions.sequence(
                        Actions.visible(false),
                        Actions.moveTo(startX - 50, -height),
                        Actions.rotateTo(0),
                        Actions.visible(true),
                        Actions.parallel(
                                Actions.forever(
                                        Actions.rotateBy(30, .2f)
                                ),
                                Actions.sequence(
                                        Actions.moveTo(startX - 70, MunhauzenGame.WORLD_HEIGHT / 2f, 1, Interpolation.fastSlow),
                                        Actions.moveTo(startX - 100, -height, 1, Interpolation.slowFast),
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
