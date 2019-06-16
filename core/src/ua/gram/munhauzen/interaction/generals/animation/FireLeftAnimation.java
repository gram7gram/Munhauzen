package ua.gram.munhauzen.interaction.generals.animation;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Touchable;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.animation.AnimatedImage;
import ua.gram.munhauzen.interaction.generals.GeneralsStoryImage;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class FireLeftAnimation extends AnimatedImage {

    public FireLeftAnimation(Texture texture) {
        super(texture);

        animate(texture, 3, 1, 3, 0.08f);
        setTouchable(Touchable.disabled);
    }

    public void init(GeneralsStoryImage image) {

        setWidth(image.width * 280f / 1444);
        setHeight(image.height * 160f / 1200);
        setVisible(true);

        setPosition(0, MunhauzenGame.WORLD_HEIGHT * ((1200 - 660) / 1200f));
    }
}

