package ua.gram.munhauzen.interaction.generals.animation;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Touchable;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.animation.AnimatedImage;
import ua.gram.munhauzen.ui.BackgroundImage;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class FumesAnimation extends AnimatedImage {

    BackgroundImage generals;

    public FumesAnimation(Texture texture, BackgroundImage generals) {
        super(texture);
        this.generals = generals;

        animate(texture, 3, 1, 3, 0.08f);
        setTouchable(Touchable.disabled);
    }

    @Override
    public void layout() {
        super.layout();

        setWidth(generals.backgroundWidth * 500f / 1444);
        setHeight(generals.backgroundHeight * 345f / 1200);
        setVisible(true);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        setX(generals.background.getX() + generals.backgroundWidth * (78 / 1444f));
        setY(MunhauzenGame.WORLD_HEIGHT * (497 / 1200f));

    }
}

