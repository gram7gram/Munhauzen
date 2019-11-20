package ua.gram.munhauzen.screen.menu.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import ua.gram.munhauzen.screen.FailsScreen;
import ua.gram.munhauzen.screen.MenuScreen;
import ua.gram.munhauzen.screen.menu.animation.IconAnimation;
import ua.gram.munhauzen.screen.menu.animation.LionAnimation;

public class GoofsButton extends MenuButton {

    public GoofsButton(final MenuScreen screen) {
        super(screen);

        hasLock = screen.game.gameState.failsState.hasUpdates;

        create(screen.game.t("menu.goofs_btn"), new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                screen.stopCurrentSfx();

                screen.game.currentSfx = screen.game.sfxService.onMenuGoofsClicked();

                if (screen.game.gameState.menuState.isGoofsBannerViewed) {
                    screen.navigateTo(new FailsScreen(screen.game));
                } else {

                    screen.unlockUI();

                    screen.game.gameState.menuState.isGoofsBannerViewed = true;

                    screen.openGoofsBanner();
                }
            }
        });
    }

    @Override
    IconAnimation createAnimationIcon() {
        return new LionAnimation(screen, this);
    }
}
