package ua.gram.munhauzen.screen.menu.animation;

import com.badlogic.gdx.graphics.Texture;

import ua.gram.munhauzen.animation.AnimatedImage;
import ua.gram.munhauzen.screen.MenuScreen;

public class HelmAnimation extends AnimatedImage {

    public HelmAnimation(MenuScreen screen) {
        super();

        loop = false;

        animate(screen.assetManager.get("menu/icon_helm_sheet_1x5.png", Texture.class),
                5, 1, 5);
    }
}

