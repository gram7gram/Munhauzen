package ua.gram.munhauzen.interaction.generals.animation;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.animation.AnimatedImage;
import ua.gram.munhauzen.interaction.generals.GeneralsStoryImage;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class FumesAnimation extends AnimatedImage {

    public FumesAnimation(Texture texture) {
        super(texture);

        animate(texture, 3, 1, 3, 0.08f);
        setTouchable(Touchable.disabled);
    }

    public void init(GeneralsStoryImage image) {
        float width = image.width * .346f;
        TextureRegionDrawable frame = animation.getKeyFrame(0);

        float scale = frame.getMinWidth() > 0 ? 1f * width / frame.getMinWidth() : 1;

        setWidth(width);
        setHeight(frame.getMinHeight() * scale);
        setVisible(true);

        setPosition(0, MunhauzenGame.WORLD_HEIGHT / 2f);
    }
}

