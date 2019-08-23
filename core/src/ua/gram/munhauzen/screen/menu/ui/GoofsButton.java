package ua.gram.munhauzen.screen.menu.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import ua.gram.munhauzen.animation.AnimatedImage;
import ua.gram.munhauzen.screen.DebugScreen;
import ua.gram.munhauzen.screen.MenuScreen;
import ua.gram.munhauzen.screen.menu.animation.RoseAnimation;

public class GoofsButton extends MenuButton {

    public GoofsButton(final MenuScreen screen) {
        super(screen);

        create("Goofs", new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                screen.navigateTo(new DebugScreen(screen.game));
            }
        });
    }

    @Override
    AnimatedImage createAnimationIcon() {
        return new RoseAnimation(
                screen.assetManager.get("menu/icon_rose_sheet_1x6.png", Texture.class)
        );
    }
}
