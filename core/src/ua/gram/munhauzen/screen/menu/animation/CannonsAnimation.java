package ua.gram.munhauzen.screen.menu.animation;

import com.badlogic.gdx.graphics.Texture;

import ua.gram.munhauzen.screen.MenuScreen;
import ua.gram.munhauzen.screen.menu.ui.MenuButton;

public class CannonsAnimation extends IconAnimation {

    public CannonsAnimation(MenuScreen screen, MenuButton button) {
        super(button);

        iconHeight *= 1.1f;

        animate(screen.assetManager.get("menu/icon_an_cannons_sheet.png", Texture.class),
                2, 7, 14);
    }
}

