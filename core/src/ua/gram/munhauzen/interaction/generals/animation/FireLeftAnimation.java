package ua.gram.munhauzen.interaction.generals.animation;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Touchable;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.animation.AnimatedImage;
import ua.gram.munhauzen.screen.game.ui.BackgroundImage;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class FireLeftAnimation extends AnimatedImage {

    BackgroundImage generals;

    /**
     * 1444x1200 is original generals image
     */
    public FireLeftAnimation(Texture texture, BackgroundImage generals) {
        super(texture);

        this.generals = generals;

        animate(texture, 3, 1, 3, 0.08f);
        setTouchable(Touchable.disabled);
    }

    @Override
    public void layout() {
        super.layout();

        setWidth(generals.backgroundWidth * 400f / 1444);
        setHeight(generals.backgroundHeight * 221f / 1200);
        setVisible(true);
    }

    @Override
    public void act(float delta) {
        super.act(delta);


        setX(generals.background.getX() + generals.backgroundWidth * (116 / 1444f));
        setY(MunhauzenGame.WORLD_HEIGHT * (534 / 1200f));

    }
}

