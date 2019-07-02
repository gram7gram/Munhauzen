package ua.gram.munhauzen.interaction.timer.animation;

import com.badlogic.gdx.graphics.Texture;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.ui.FitImage;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class Bomb extends FitImage {

    public Bomb(Texture texture) {
        super(texture);

        float width = MunhauzenGame.WORLD_WIDTH / 8f;

        float scale = 1f * width / getDrawable().getMinWidth();
        float height = 1f * getDrawable().getMinHeight() * scale;

        setSize(width, height);
        setPosition(0, MunhauzenGame.WORLD_HEIGHT / 4f - height / 2f);
    }
}
