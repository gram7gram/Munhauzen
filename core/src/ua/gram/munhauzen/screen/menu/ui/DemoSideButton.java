package ua.gram.munhauzen.screen.menu.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import ua.gram.munhauzen.animation.AnimatedImage;
import ua.gram.munhauzen.screen.MenuScreen;
import ua.gram.munhauzen.screen.menu.fragment.DemoFragment;
import ua.gram.munhauzen.utils.Log;

public class DemoSideButton extends AnimatedImage {

    public DemoSideButton(final MenuScreen screen) {

        animate(
                screen.assetManager.get("menu/demo_version_sheet_1x4.png", Texture.class),
                1, 4, 4, 1f
        );

        loop = false;

        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                try {

                    screen.demoFragment = new DemoFragment(screen);
                    screen.demoFragment.create();

                    screen.layers.setBannerLayer(screen.demoFragment);

                    screen.demoFragment.fadeIn();

                } catch (Throwable e) {
                    Log.e(tag, e);

                    screen.onCriticalError(e);
                }
            }
        });
    }
}
