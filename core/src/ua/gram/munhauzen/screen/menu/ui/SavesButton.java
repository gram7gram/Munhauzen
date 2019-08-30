package ua.gram.munhauzen.screen.menu.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import ua.gram.munhauzen.animation.AnimatedImage;
import ua.gram.munhauzen.screen.MenuScreen;
import ua.gram.munhauzen.screen.SavesScreen;
import ua.gram.munhauzen.screen.menu.animation.LionAnimation;

public class SavesButton extends MenuButton {

    public SavesButton(final MenuScreen screen) {
        super(screen);

        create("Saves", new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                screen.game.sfxService.onMenuSaveClicked();

                screen.scaleAndNavigateTo(new SavesScreen(screen.game));
            }
        });
    }

    @Override
    AnimatedImage createAnimationIcon() {
        return new LionAnimation(
                screen.assetManager.get("menu/icon_lion_sheet_1x8.png", Texture.class)
        );
    }
}
