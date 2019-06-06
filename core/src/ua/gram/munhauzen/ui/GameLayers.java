package ua.gram.munhauzen.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class GameLayers extends Stack {

    public Actor backgroundLayer, interactionLayer, storeDecisionsLayer, storyUILayer;

    public void setBackgroundImageLayer(Actor actor) {
        backgroundLayer = actor;
        addActorAt(0, actor);
    }

    public void setInteractionLayer(Actor actor) {
        interactionLayer = actor;
        addActorAt(1, actor);
    }

    public void setStoryDecisionsLayer(Actor actor) {
        storeDecisionsLayer = actor;
        addActorAt(2, actor);
    }

    public void setStoryUILayer(Actor actor) {
        storyUILayer = actor;
        addActorAt(3, actor);
    }

}
