package ua.gram.munhauzen.screen.menu.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import ua.gram.munhauzen.animation.AnimatedImage;
import ua.gram.munhauzen.screen.MenuScreen;
import ua.gram.munhauzen.screen.menu.fragment.RateFragment;
import ua.gram.munhauzen.utils.Log;

public class RateSideButton extends AnimatedImage {

    public RateSideButton(final MenuScreen screen) {

        animate(
                screen.assetManager.get("menu/b_rate_an_sheet.png", Texture.class),
                1, 3, 3, 1.5f
        );

        loop = false;

        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                screen.game.stopCurrentSfx();

                screen.game.sfxService.onAnyBtnClicked();

                try {

                    screen.rateFragment = new RateFragment(screen);
                    screen.rateFragment.create();

                    screen.layers.setBannerLayer(screen.rateFragment);

                    screen.rateFragment.fadeIn();

                } catch (Throwable e) {
                    Log.e(tag, e);

                    screen.onCriticalError(e);
                }
            }
        });
    }
}
