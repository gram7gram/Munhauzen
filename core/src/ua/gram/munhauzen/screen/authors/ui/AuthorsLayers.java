package ua.gram.munhauzen.screen.authors.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.utils.Disposable;

import ua.gram.munhauzen.GameLayerInterface;
import ua.gram.munhauzen.ui.Fragment;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class AuthorsLayers extends Stack implements Disposable, GameLayerInterface {

    final String tag = getClass().getSimpleName();
    public Fragment contentLayer, controlsLayer, bannerLayer;

    public AuthorsLayers() {
        setFillParent(true);

        update();
    }

    public void update() {

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

    public void setBannerLayer(Fragment actor) {
        if (bannerLayer != null) {
            removeActor(bannerLayer.getRoot());
            bannerLayer.destroy();
        }
        bannerLayer = actor;

        update();
    }

    @Override
    public Fragment getBannerLayer() {
        return bannerLayer;
    }

    public void setContentLayer(Fragment actor) {
        if (contentLayer != null) {
            removeActor(contentLayer.getRoot());
            contentLayer.destroy();
        }
        contentLayer = actor;

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

        if (controlsLayer != null) {
            controlsLayer.destroy();
            controlsLayer = null;
        }

        if (bannerLayer != null) {
            bannerLayer.destroy();
            bannerLayer = null;
        }
    }
}
