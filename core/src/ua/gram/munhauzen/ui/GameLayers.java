package ua.gram.munhauzen.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.utils.Disposable;

import ua.gram.munhauzen.fragment.Fragment;
import ua.gram.munhauzen.fragment.ImageFragment;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class GameLayers extends Stack implements Disposable {

    final String tag = getClass().getSimpleName();
    final GameScreen gameScreen;
    public ImageFragment backgroundLayer;
    public Fragment controlsLayer, interactionLayer, storyDecisionsLayer, progressBarLayer;

    public GameLayers(GameScreen gameScreen) {
        this.gameScreen = gameScreen;

        setFillParent(true);

        update();
    }

    public void update() {

        Log.i(tag, "update");

        clearChildren();

        if (backgroundLayer != null) {
            backgroundLayer.getRoot().setTouchable(Touchable.childrenOnly);
            addActor(backgroundLayer.getRoot());
        } else {
            addActor(createDummy("backgroundLayer"));
        }

        if (interactionLayer != null) {
            interactionLayer.getRoot().setTouchable(Touchable.childrenOnly);
            addActor(interactionLayer.getRoot());
        } else {
            addActor(createDummy("interactionLayer"));
        }

        if (storyDecisionsLayer != null) {
            storyDecisionsLayer.getRoot().setTouchable(Touchable.childrenOnly);
            addActor(storyDecisionsLayer.getRoot());
        } else {
            addActor(createDummy("storyDecisionsLayer"));
        }

        if (progressBarLayer != null) {
            progressBarLayer.getRoot().setTouchable(Touchable.childrenOnly);
            addActor(progressBarLayer.getRoot());
        } else {
            addActor(createDummy("progressBarLayer"));
        }

        if (controlsLayer != null) {
            controlsLayer.getRoot().setTouchable(Touchable.childrenOnly);
            addActor(controlsLayer.getRoot());
        } else {
            addActor(createDummy("controlsLayer"));
        }

    }

    public void setBackgroundImageLayer(ImageFragment actor) {
        if (backgroundLayer != null) {
            removeActor(backgroundLayer.getRoot());
            backgroundLayer.destroy();
        }
        backgroundLayer = actor;

        update();
    }

    public void setInteractionLayer(Fragment actor) {
        if (interactionLayer != null) {
            removeActor(interactionLayer.getRoot());
            interactionLayer.destroy();
        }
        interactionLayer = actor;

        update();
    }

    public void setStoryDecisionsLayer(Fragment actor) {
        if (storyDecisionsLayer != null) {
            removeActor(storyDecisionsLayer.getRoot());
            storyDecisionsLayer.destroy();
        }
        storyDecisionsLayer = actor;

        update();
    }

    public void setProgressBarLayer(Fragment actor) {
        if (progressBarLayer != null) {
            removeActor(progressBarLayer.getRoot());
            progressBarLayer.destroy();
        }
        progressBarLayer = actor;

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
        if (controlsLayer != null) {
            controlsLayer.dispose();
            controlsLayer = null;
        }
        if (progressBarLayer != null) {
            progressBarLayer.dispose();
            progressBarLayer = null;
        }
        if (storyDecisionsLayer != null) {
            storyDecisionsLayer.dispose();
            storyDecisionsLayer = null;
        }
        if (interactionLayer != null) {
            interactionLayer.dispose();
            interactionLayer = null;
        }
        if (backgroundLayer != null) {
            backgroundLayer.dispose();
            backgroundLayer = null;
        }
    }
}
