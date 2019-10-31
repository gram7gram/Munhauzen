package ua.gram.munhauzen.screen.menu.animation;

import com.badlogic.gdx.graphics.Texture;

import ua.gram.munhauzen.screen.MenuScreen;
import ua.gram.munhauzen.screen.menu.ui.MenuButton;

public class CrownAnimation extends IconAnimation {

    public CrownAnimation(MenuScreen screen, MenuButton button) {
        super(button);

        animate(screen.assetManager.get("menu/icon_an_crown_sheet.png", Texture.class),
                2, 5, 9, 0.08f);
    }
}

