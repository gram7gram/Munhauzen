package ua.gram.munhauzen.screen.menu.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import ua.gram.munhauzen.animation.AnimatedImage;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.screen.MenuScreen;
import ua.gram.munhauzen.screen.menu.animation.CannonsAnimation;

public class ContinueButton extends MenuButton {

    public ContinueButton(final MenuScreen screen) {
        super(screen);

        create(screen.game.t("menu.continue_btn"), new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                screen.stopCurrentSfx();

                screen.game.currentSfx = screen.game.sfxService.onMenuContinueClicked();

                screen.scaleAndNavigateTo(new GameScreen(screen.game));
            }
        });
    }

    @Override
    AnimatedImage createAnimationIcon() {
        return new CannonsAnimation(screen);
    }
}
