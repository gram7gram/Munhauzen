package ua.gram.munhauzen.screen.menu.animation;

import com.badlogic.gdx.graphics.Texture;

import ua.gram.munhauzen.animation.AnimatedImage;
import ua.gram.munhauzen.screen.MenuScreen;

public class ShieldAnimation extends AnimatedImage {

    public ShieldAnimation(MenuScreen screen) {
        super();

        loop = false;

        animate(screen.assetManager.get("menu/icon_shield_sheet_1x8.png", Texture.class),
                8, 1, 8);
    }
}

