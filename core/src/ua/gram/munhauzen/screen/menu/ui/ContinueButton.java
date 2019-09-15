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

        iconSize = 90;

        create("Continue", new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                screen.stopCurrentSfx();

                screen.game.sfxService.onAnyBtnClicked();

                screen.currentSfx = screen.game.sfxService.onMenuContinueClicked();

                screen.scaleAndNavigateTo(new GameScreen(screen.game));
            }
        });
    }

    @Override
    AnimatedImage createAnimationIcon() {
        return new CannonsAnimation(screen);
    }
}
