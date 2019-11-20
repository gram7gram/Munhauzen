package ua.gram.munhauzen.screen.menu.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import ua.gram.munhauzen.screen.GalleryScreen;
import ua.gram.munhauzen.screen.MenuScreen;
import ua.gram.munhauzen.screen.menu.animation.IconAnimation;
import ua.gram.munhauzen.screen.menu.animation.RoseAnimation;

public class GalleryButton extends MenuButton {

    public GalleryButton(final MenuScreen screen) {
        super(screen);

        hasLock = screen.game.gameState.galleryState.hasUpdates;

        create(screen.game.t("menu.gallery_btn"), new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                screen.stopCurrentSfx();

                screen.game.currentSfx = screen.game.sfxService.onMenuGalleryClicked();

                if (screen.game.gameState.menuState.isGalleryBannerViewed) {
                    screen.navigateTo(new GalleryScreen(screen.game));
                } else {

                    screen.unlockUI();

                    screen.game.gameState.menuState.isGalleryBannerViewed = true;

                    screen.openGalleryBanner();
                }
            }
        });
    }

    @Override
    IconAnimation createAnimationIcon() {
        return new RoseAnimation(screen, this);
    }
}
