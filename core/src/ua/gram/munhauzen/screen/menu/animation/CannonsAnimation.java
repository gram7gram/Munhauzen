package ua.gram.munhauzen.screen.menu.animation;

import com.badlogic.gdx.graphics.Texture;

import ua.gram.munhauzen.animation.AnimatedImage;
import ua.gram.munhauzen.screen.MenuScreen;

public class CannonsAnimation extends AnimatedImage {

    public CannonsAnimation(MenuScreen screen) {
        super();

        loop = false;

        animate(screen.assetManager.get("menu/icon_cannons_sheet_1x14.png", Texture.class),
                14, 1, 14);
    }
}

