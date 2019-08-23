package ua.gram.munhauzen.screen.menu.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import ua.gram.munhauzen.animation.AnimatedImage;
import ua.gram.munhauzen.screen.AuthorsScreen;
import ua.gram.munhauzen.screen.MenuScreen;
import ua.gram.munhauzen.screen.menu.animation.HelmAnimation;

public class AuthorsButton extends MenuButton {

    public AuthorsButton(final MenuScreen screen) {
        super(screen);

        create("Authors", new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                screen.navigateTo(new AuthorsScreen(screen.game));
            }
        });
    }

    @Override
    AnimatedImage createAnimationIcon() {
        return new HelmAnimation(
                screen.assetManager.get("menu/icon_helm_sheet_1x5.png", Texture.class)
        );
    }
}
