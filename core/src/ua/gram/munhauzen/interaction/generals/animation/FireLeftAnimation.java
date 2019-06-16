package ua.gram.munhauzen.interaction.generals.animation;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.animation.AnimatedImage;
import ua.gram.munhauzen.interaction.generals.GeneralsStoryImage;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class FireLeftAnimation extends AnimatedImage {

    Image generals;

    /**
     * 1444x1200 is original generals image
     */
    public FireLeftAnimation(Texture texture, Image generals) {
        super(texture);

        this.generals = generals;

        animate(texture, 3, 1, 3, 0.08f);
        setTouchable(Touchable.disabled);
    }

    public void init(GeneralsStoryImage image) {

        setWidth(image.width * 400f / 1444);
        setHeight(image.height * 221f / 1200);
        setVisible(true);

        setY(MunhauzenGame.WORLD_HEIGHT * (534 / 1200f));
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        setX(generals.getX() + generals.getWidth() * (116 / 1444f));
    }
}

