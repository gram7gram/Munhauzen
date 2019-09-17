package ua.gram.munhauzen.screen.menu.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import ua.gram.munhauzen.animation.AnimatedImage;
import ua.gram.munhauzen.screen.GalleryScreen;
import ua.gram.munhauzen.screen.MenuScreen;
import ua.gram.munhauzen.screen.menu.animation.RoseAnimation;

public class GalleryButton extends MenuButton {

    public GalleryButton(final MenuScreen screen) {
        super(screen);

        hasLock = screen.game.gameState.galleryState.hasUpdates;

        create("Gallery", new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                screen.stopCurrentSfx();

                screen.currentSfx = screen.game.sfxService.onMenuGalleryClicked();

                screen.navigateTo(new GalleryScreen(screen.game));
            }
        });
    }

    @Override
    AnimatedImage createAnimationIcon() {
        return new RoseAnimation(screen);
    }
}
