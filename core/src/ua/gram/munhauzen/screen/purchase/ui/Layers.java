package ua.gram.munhauzen.screen.purchase.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.utils.Disposable;

import ua.gram.munhauzen.ui.Fragment;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class Layers extends Stack implements Disposable {

    final String tag = getClass().getSimpleName();
    public Fragment contentLayer, bannerLayer, controlsLayer;

    public Layers() {
        setFillParent(true);

        update();
    }

    private void update() {

        Log.i(tag, "update");

        clearChildren();

        if (contentLayer != null) {
            addActor(contentLayer.getRoot());
        } else {
            addActor(createDummy("contentLayer"));
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

    public void setContentLayer(Fragment actor) {
        if (contentLayer != null) {
            removeActor(contentLayer.getRoot());
            contentLayer.destroy();
        }
        contentLayer = actor;

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

    public void setControlsLayer(Fragment actor) {
        if (controlsLayer != null) {
            removeActor(controlsLayer.getRoot());
            controlsLayer.destroy();
        }
        controlsLayer = actor;

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
        if (contentLayer != null) {
            contentLayer.destroy();
            contentLayer = null;
        }

        if (bannerLayer != null) {
            bannerLayer.destroy();
            bannerLayer = null;
        }

        if (controlsLayer != null) {
            controlsLayer.destroy();
            controlsLayer = null;
        }
    }
}
