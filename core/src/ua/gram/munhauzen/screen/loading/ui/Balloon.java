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

        width = MunhauzenGame.WORLD_WIDTH * .25f;
        float scale = getDrawable().getMinWidth() / width;
        height = getDrawable().getMinHeight() * scale;
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
