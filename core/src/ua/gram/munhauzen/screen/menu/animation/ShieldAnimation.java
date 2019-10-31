package ua.gram.munhauzen.screen.menu.animation;

import com.badlogic.gdx.graphics.Texture;

import ua.gram.munhauzen.screen.MenuScreen;
import ua.gram.munhauzen.screen.menu.ui.MenuButton;

public class ShieldAnimation extends IconAnimation {

    public ShieldAnimation(MenuScreen screen, MenuButton button) {
        super(button);

        animate(screen.assetManager.get("menu/icon_an_shield_sheet.png", Texture.class),
                2, 4, 8);
    }
}

