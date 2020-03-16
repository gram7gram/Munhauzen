package ua.gram.munhauzen.screen.menu.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.screen.MenuScreen;
import ua.gram.munhauzen.screen.menu.animation.CannonsAnimation;
import ua.gram.munhauzen.screen.menu.animation.IconAnimation;

public class ContinueButton extends MenuButton {

    public ContinueButton(final MenuScreen screen) {
        super(screen);

        create(screen.game.t("menu.continue_btn"), new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                screen.game.stopCurrentSfx();

                screen.game.currentSfx = screen.game.sfxService.onMenuContinueClicked();

                screen.scaleAndNavigateTo(new GameScreen(screen.game));
            }
        });
    }

    @Override
    IconAnimation createAnimationIcon() {
        return new CannonsAnimation(screen, this);
    }
}
