package ua.gram.munhauzen.screen.menu.listenter;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;

import ua.gram.munhauzen.screen.MenuScreen;
import ua.gram.munhauzen.utils.Log;

public class MenuStageListener extends ActorGestureListener {

    final String tag = getClass().getSimpleName();
    final MenuScreen screen;

    public MenuStageListener(MenuScreen screen) {
        this.screen = screen;
    }

    @Override
    public void tap(InputEvent event, float x, float y, int count, int button) {
        super.tap(event, x, y, count, button);

        if (event.isHandled()) return;

        Log.i(tag, "ui clicked");

        try {

            if (screen.controlsFragment != null) {
                if (screen.controlsFragment.isVisible) {
                    if (!screen.controlsFragment.isFadeOut) {
                        screen.controlsFragment.fadeOut();
                    }
                } else {
                    if (!screen.controlsFragment.isFadeIn) {
                        screen.controlsFragment.fadeIn();
                    }
                }

            }

        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }

    @Override
    public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
        super.touchDown(event, x, y, pointer, button);

        try {

            if (screen.controlsFragment != null) {
                screen.controlsFragment.scheduleFadeOut();
            }

        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }

}
