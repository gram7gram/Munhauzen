package ua.gram.munhauzen.screen.menu.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import ua.gram.munhauzen.screen.AuthorsScreen;
import ua.gram.munhauzen.screen.MenuScreen;
import ua.gram.munhauzen.screen.menu.animation.HelmAnimation;
import ua.gram.munhauzen.screen.menu.animation.IconAnimation;

public class AuthorsButton extends MenuButton {

    public AuthorsButton(final MenuScreen screen) {
        super(screen);

        create(screen.game.t("menu.authors_btn"), new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                screen.game.stopCurrentSfx();

                screen.game.currentSfx = screen.game.sfxService.onMenuAuthorsClicked();

                screen.navigateTo(new AuthorsScreen(screen.game));
            }
        });
    }

    @Override
    IconAnimation createAnimationIcon() {
        return new HelmAnimation(screen, this);
    }
}
