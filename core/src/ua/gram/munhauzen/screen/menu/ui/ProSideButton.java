package ua.gram.munhauzen.screen.menu.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import ua.gram.munhauzen.animation.AnimatedImage;
import ua.gram.munhauzen.screen.MenuScreen;
import ua.gram.munhauzen.screen.menu.fragment.ProFragment;
import ua.gram.munhauzen.utils.Log;

public class ProSideButton extends AnimatedImage {

    public ProSideButton(final MenuScreen screen) {

        animate(
                screen.assetManager.get("menu/b_full_version_an_sheet.png", Texture.class),
                1, 2, 2, 1f
        );

        loop = false;

        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                screen.game.stopCurrentSfx();

                screen.game.sfxService.onAnyBtnClicked();

                try {

                    screen.proFragment = new ProFragment(screen);
                    screen.proFragment.create();

                    screen.layers.setBannerLayer(screen.proFragment);

                    screen.proFragment.fadeIn();

                } catch (Throwable e) {
                    Log.e(tag, e);

                    screen.onCriticalError(e);
                }
            }
        });
    }
}
