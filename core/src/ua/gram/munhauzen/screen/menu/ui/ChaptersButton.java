package ua.gram.munhauzen.screen.menu.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import ua.gram.munhauzen.screen.ChaptersScreen;
import ua.gram.munhauzen.screen.MenuScreen;
import ua.gram.munhauzen.screen.menu.animation.IconAnimation;
import ua.gram.munhauzen.screen.menu.animation.ShieldAnimation;

public class ChaptersButton extends MenuButton {

    public ChaptersButton(final MenuScreen screen) {
        super(screen);

        create(screen.game.t("menu.chapters_btn"), new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                screen.game.stopCurrentSfx();

                screen.game.currentSfx = screen.game.sfxService.onMenuSaveClicked();

                screen.navigateTo(new ChaptersScreen(screen.game));
            }
        });
    }

    @Override
    IconAnimation createAnimationIcon() {
        return new ShieldAnimation(screen, this);
    }
}
