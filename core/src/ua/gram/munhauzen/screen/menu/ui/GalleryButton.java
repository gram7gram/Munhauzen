package ua.gram.munhauzen.screen.menu.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import ua.gram.munhauzen.animation.AnimatedImage;
import ua.gram.munhauzen.screen.GalleryScreen;
import ua.gram.munhauzen.screen.MenuScreen;
import ua.gram.munhauzen.screen.menu.animation.ShieldAnimation;

public class GalleryButton extends MenuButton {

    public GalleryButton(final MenuScreen screen) {
        super(screen);

        hasLock = screen.game.gameState.galleryState.hasUpdates;

        create("Gallery", new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                screen.scaleAndNavigateTo(new GalleryScreen(screen.game));
            }
        });
    }

    @Override
    AnimatedImage createAnimationIcon() {
        return new ShieldAnimation(
                screen.assetManager.get("menu/icon_shield_sheet_1x8.png", Texture.class)
        );
    }
}
