package ua.gram.munhauzen.screen.menu.animation;

import com.badlogic.gdx.graphics.Texture;

import ua.gram.munhauzen.animation.AnimatedImage;
import ua.gram.munhauzen.screen.MenuScreen;

public class CrownAnimation extends AnimatedImage {

    public CrownAnimation(MenuScreen screen) {
        super();

        loop = false;

        animate(screen.assetManager.get("menu/icon_crown_sheet_1x9.png", Texture.class),
                9, 1, 9, 0.08f);
    }
}

