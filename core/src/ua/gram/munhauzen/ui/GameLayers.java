package ua.gram.munhauzen.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;

import ua.gram.munhauzen.fragment.Fragment;
import ua.gram.munhauzen.screen.GameScreen;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class GameLayers extends Stack {

    final GameScreen gameScreen;
    final Actor dummy;
    public Fragment backgroundLayer, controlsLayer, interactionLayer, storyDecisionsLayer, progressBarLayer;

    public GameLayers(GameScreen gameScreen) {
        this.gameScreen = gameScreen;

        setFillParent(true);

        dummy = new Actor();
        dummy.setName("dummy");
        dummy.setTouchable(Touchable.disabled);
        dummy.setVisible(false);

        addActorAt(0, dummy);
        addActorAt(1, dummy);
        addActorAt(2, dummy);
        addActorAt(3, dummy);
        addActorAt(4, dummy);
    }

    public void setBackgroundImageLayer(Fragment actor) {
        if (backgroundLayer != null) {
            removeActor(backgroundLayer.getRoot());
            backgroundLayer.destroy();
        }
        backgroundLayer = actor;

        if (actor != null) {
            addActorAt(0, actor.getRoot());
        } else {
            addActorAt(0, dummy);
        }

    }

    public void setInteractionLayer(Fragment actor) {
        if (interactionLayer != null) {
            removeActor(interactionLayer.getRoot());
            interactionLayer.destroy();
        }
        interactionLayer = actor;

        if (actor != null) {
            addActorAt(1, actor.getRoot());
        } else {
            addActorAt(1, dummy);
        }

    }

    public void setStoryDecisionsLayer(Fragment actor) {
        if (storyDecisionsLayer != null) {
            removeActor(storyDecisionsLayer.getRoot());
            storyDecisionsLayer.destroy();
        }
        storyDecisionsLayer = actor;

        if (actor != null) {
            addActorAt(2, actor.getRoot());
        } else {
            addActorAt(2, dummy);
        }
    }

    public void setProgressBarLayer(Fragment actor) {
        if (progressBarLayer != null) {
            removeActor(progressBarLayer.getRoot());
            progressBarLayer.destroy();
        }
        progressBarLayer = actor;

        if (actor != null) {
            addActorAt(3, actor.getRoot());
        } else {
            addActorAt(3, dummy);
        }
    }

    public void setControlsLayer(Fragment actor) {
        if (controlsLayer != null) {
            removeActor(controlsLayer.getRoot());
            controlsLayer.destroy();
        }
        controlsLayer = actor;

        if (actor != null) {
            addActorAt(4, actor.getRoot());
        } else {
            addActorAt(4, dummy);
        }
    }

}
