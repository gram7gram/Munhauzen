package ua.gram.munhauzen.screen.game.ui;

import com.badlogic.gdx.graphics.Texture;

import ua.gram.munhauzen.animation.AnimatedImage;
import ua.gram.munhauzen.screen.GameScreen;

public class StarsAnimation extends AnimatedImage {

    public StarsAnimation(GameScreen screen) {
        super();

//        this.loop = false;

        animate(screen.assetManager.get("GameScreen/an_stars_sheet.png", Texture.class),
                3, 5, 15);
    }
}
