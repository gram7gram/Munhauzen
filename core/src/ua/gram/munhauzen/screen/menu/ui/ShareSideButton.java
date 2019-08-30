package ua.gram.munhauzen.screen.menu.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import ua.gram.munhauzen.animation.AnimatedImage;
import ua.gram.munhauzen.screen.MenuScreen;
import ua.gram.munhauzen.screen.menu.fragment.ShareFragment;
import ua.gram.munhauzen.utils.Log;

public class ShareSideButton extends AnimatedImage {

    public ShareSideButton(final MenuScreen screen) {

        animate(
                screen.assetManager.get("menu/share_sheet_1x2.png", Texture.class),
                1, 2, 2, 1f
        );

        loop = false;

        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                try {

                    screen.shareFragment = new ShareFragment(screen);
                    screen.shareFragment.create();

                    screen.layers.setBannerLayer(screen.shareFragment);

                    screen.shareFragment.fadeIn();

                } catch (Throwable e) {
                    Log.e(tag, e);

                    screen.onCriticalError(e);
                }
            }
        });
    }
}
