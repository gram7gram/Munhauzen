package ua.gram.munhauzen.screen.menu.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import ua.gram.munhauzen.animation.AnimatedImage;
import ua.gram.munhauzen.screen.MenuScreen;
import ua.gram.munhauzen.screen.menu.fragment.RateBanner;
import ua.gram.munhauzen.utils.Log;

public class RateSideButton extends AnimatedImage {

    public RateSideButton(final MenuScreen screen) {

        animate(
                screen.assetManager.get("menu/rate_sheet_1x2.png", Texture.class),
                1, 2, 2, 1f
        );

        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                try {

                    screen.rateBanner = new RateBanner(screen);
                    screen.rateBanner.create();

                    screen.layers.setBannerLayer(screen.rateBanner);

                    screen.rateBanner.fadeIn();

                } catch (Throwable e) {
                    Log.e(tag, e);

                    screen.onCriticalError(e);
                }
            }
        });
    }
}
