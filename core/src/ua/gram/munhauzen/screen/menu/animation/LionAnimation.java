package ua.gram.munhauzen.screen.menu.animation;

import com.badlogic.gdx.graphics.Texture;

import ua.gram.munhauzen.animation.AnimatedImage;
import ua.gram.munhauzen.screen.MenuScreen;

public class LionAnimation extends AnimatedImage {

    public LionAnimation(MenuScreen screen) {
        super();

        loop = false;

        animate(screen.assetManager.get("menu/icon_lion_sheet_1x8.png", Texture.class),
                8, 1, 8);
    }
}

