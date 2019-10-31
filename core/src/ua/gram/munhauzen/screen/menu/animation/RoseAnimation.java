package ua.gram.munhauzen.screen.menu.animation;

import com.badlogic.gdx.graphics.Texture;

import ua.gram.munhauzen.screen.MenuScreen;
import ua.gram.munhauzen.screen.menu.ui.MenuButton;

public class RoseAnimation extends IconAnimation {

    public RoseAnimation(MenuScreen screen, MenuButton button) {
        super(button);

        animate(screen.assetManager.get("menu/icon_an_rose_sheet.png", Texture.class),
                2, 3, 6);
    }
}

