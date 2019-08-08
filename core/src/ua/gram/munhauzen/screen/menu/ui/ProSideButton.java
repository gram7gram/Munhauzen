package ua.gram.munhauzen.screen.menu.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import ua.gram.munhauzen.animation.AnimatedImage;
import ua.gram.munhauzen.screen.MenuScreen;
import ua.gram.munhauzen.screen.menu.fragment.ProBanner;
import ua.gram.munhauzen.utils.Log;

public class ProSideButton extends AnimatedImage {

    public ProSideButton(final MenuScreen screen) {

        animate(
                screen.assetManager.get("menu/full_version_sheet_1x2.png", Texture.class),
                1, 2, 2, 1f
        );

        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                try {

                    screen.proBanner = new ProBanner(screen);
                    screen.proBanner.create();

                    screen.layers.setBannerLayer(screen.proBanner);

                    screen.proBanner.fadeIn();

                } catch (Throwable e) {
                    Log.e(tag, e);

                    screen.onCriticalError(e);
                }
            }
        });
    }
}
