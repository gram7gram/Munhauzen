package ua.gram.munhauzen.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.utils.Disposable;

import ua.gram.munhauzen.screen.MenuScreen;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class MenuLayers extends Stack implements Disposable {

    final String tag = getClass().getSimpleName();
    final MenuScreen screen;
    public Fragment backgroundLayer, controlsLayer, bannerLayer;

    public MenuLayers(MenuScreen screen) {
        this.screen = screen;

        setFillParent(true);

        update();
    }

    public void update() {

        Log.i(tag, "update");

        clearChildren();

        if (backgroundLayer != null) {
            addActor(backgroundLayer.getRoot());
        } else {
            addActor(createDummy("backgroundLayer"));
        }

        if (controlsLayer != null) {
            addActor(controlsLayer.getRoot());
        } else {
            addActor(createDummy("controlsLayer"));
        }

        if (bannerLayer != null) {
            addActor(bannerLayer.getRoot());
        } else {
            addActor(createDummy("bannerLayer"));
        }

    }

    public void setBackgroundLayer(Fragment actor) {
        if (backgroundLayer != null) {
            removeActor(backgroundLayer.getRoot());
            backgroundLayer.destroy();
        }
        backgroundLayer = actor;

        update();
    }

    public void setControlsLayer(Fragment actor) {
        if (controlsLayer != null) {
            removeActor(controlsLayer.getRoot());
            controlsLayer.destroy();
        }
        controlsLayer = actor;

        update();
    }

    public void setBannerLayer(Fragment actor) {
        if (bannerLayer != null) {
            removeActor(bannerLayer.getRoot());
            bannerLayer.destroy();
        }
        bannerLayer = actor;

        update();
    }

    private Actor createDummy(String suffix) {
        Actor dummy = new Actor();
        dummy.setName("dummy-" + suffix);
        dummy.setTouchable(Touchable.disabled);
        dummy.setVisible(false);

        return dummy;
    }

    @Override
    public void dispose() {
        if (controlsLayer != null) {
            controlsLayer.destroy();
            controlsLayer = null;
        }

        if (backgroundLayer != null) {
            backgroundLayer.destroy();
            backgroundLayer = null;
        }

        if (bannerLayer != null) {
            bannerLayer.destroy();
            bannerLayer = null;
        }
    }
}
