package ua.gram.munhauzen.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Stack;

import ua.gram.munhauzen.fragment.Fragment;
import ua.gram.munhauzen.screen.GameScreen;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class GameLayers extends Stack {

    final GameScreen gameScreen;
    public Fragment backgroundLayer, controlsLayer, interactionLayer, storyDecisionsLayer, progressBarLayer;

    public GameLayers(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    public void setBackgroundImageLayer(Fragment actor) {
        if (backgroundLayer != null) {
            backgroundLayer.destroy();
        }
        backgroundLayer = actor;
        addActorAt(0, actor.getRoot());

    }

    public void setInteractionLayer(Fragment actor) {
        if (interactionLayer != null) {
            interactionLayer.destroy();
        }
        interactionLayer = actor;
        addActorAt(1, actor.getRoot());

    }

    public void setStoryDecisionsLayer(Fragment actor) {
        if (storyDecisionsLayer != null) {
            storyDecisionsLayer.destroy();
        }
        storyDecisionsLayer = actor;
        addActorAt(2, actor.getRoot());
    }

    public void setProgressBarLayer(Fragment actor) {
        if (progressBarLayer != null) {
            progressBarLayer.destroy();
        }
        progressBarLayer = actor;

        addActorAt(3, actor.getRoot());
    }

    public void setControlsLayer(Fragment actor) {
        if (controlsLayer != null) {
            controlsLayer.destroy();
        }
        controlsLayer = actor;

        gameScreen.ui.addActor(actor.getRoot());
    }

}
