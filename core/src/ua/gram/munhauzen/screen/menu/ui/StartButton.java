package ua.gram.munhauzen.screen.menu.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import ua.gram.munhauzen.animation.AnimatedImage;
import ua.gram.munhauzen.entity.Save;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.screen.MenuScreen;
import ua.gram.munhauzen.screen.menu.animation.CrownAnimation;

public class StartButton extends MenuButton {

    public StartButton(final MenuScreen screen) {
        super(screen);

        create(screen.game.t("menu.start_btn"), new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                screen.game.gameState.setActiveSave(new Save());

                screen.stopCurrentSfx();
                screen.currentSfx = screen.game.sfxService.onMenuStartClicked();

                screen.scaleAndNavigateTo(new GameScreen(screen.game));
            }
        });
    }

    @Override
    AnimatedImage createAnimationIcon() {
        return new CrownAnimation(screen);
    }
}
