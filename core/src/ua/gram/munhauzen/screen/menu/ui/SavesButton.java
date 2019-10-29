package ua.gram.munhauzen.screen.menu.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import ua.gram.munhauzen.animation.AnimatedImage;
import ua.gram.munhauzen.screen.MenuScreen;
import ua.gram.munhauzen.screen.SavesScreen;
import ua.gram.munhauzen.screen.menu.animation.ShieldAnimation;

public class SavesButton extends MenuButton {

    public SavesButton(final MenuScreen screen) {
        super(screen);

        create(screen.game.t("menu.saves_btn"), new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                screen.stopCurrentSfx();

                screen.game.currentSfx = screen.game.sfxService.onMenuSaveClicked();

                screen.navigateTo(new SavesScreen(screen.game));
            }
        });
    }

    @Override
    AnimatedImage createAnimationIcon() {
        return new ShieldAnimation(screen);
    }
}
