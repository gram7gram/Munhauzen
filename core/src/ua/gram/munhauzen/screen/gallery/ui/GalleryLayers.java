package ua.gram.munhauzen.screen.gallery.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.utils.Disposable;

import ua.gram.munhauzen.ui.Fragment;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class GalleryLayers extends Stack implements Disposable {

    final String tag = getClass().getSimpleName();
    public Fragment contentLayer, controlsLayer, paintingLayer, fullscreenLayer;

    public GalleryLayers() {
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

        if (paintingLayer != null) {
            addActor(paintingLayer.getRoot());
        } else {
            addActor(createDummy("paintingLayer"));
        }

        if (fullscreenLayer != null) {
            addActor(fullscreenLayer.getRoot());
        } else {
            addActor(createDummy("fullscreenLayer"));
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

    public void setControlsLayer(Fragment actor) {
        if (controlsLayer != null) {
            removeActor(controlsLayer.getRoot());
            controlsLayer.destroy();
        }
        controlsLayer = actor;

        update();
    }

    public void setFullscreenLayer(Fragment actor) {
        if (fullscreenLayer != null) {
            removeActor(fullscreenLayer.getRoot());
            fullscreenLayer.destroy();
        }
        fullscreenLayer = actor;

        update();
    }

    public void setPaintingLayer(Fragment actor) {
        if (paintingLayer != null) {
            removeActor(paintingLayer.getRoot());
            paintingLayer.destroy();
        }
        paintingLayer = actor;

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

        if (paintingLayer != null) {
            paintingLayer.destroy();
            paintingLayer = null;
        }

        if (fullscreenLayer != null) {
            fullscreenLayer.destroy();
            fullscreenLayer = null;
        }
    }
}
