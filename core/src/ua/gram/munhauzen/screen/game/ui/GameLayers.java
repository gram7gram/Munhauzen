package ua.gram.munhauzen.screen.game.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.utils.Disposable;

import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.screen.game.fragment.ImageFragment;
import ua.gram.munhauzen.ui.Fragment;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class GameLayers extends Stack implements Disposable {

    final GameScreen gameScreen;
    public ImageFragment backgroundLayer;
    public Fragment controlsLayer, interactionLayer,
            storyDecisionsLayer, progressBarLayer, interactionProgressBarLayer,
            purchaseLayer, achievementLayer;

    public GameLayers(GameScreen gameScreen) {
        this.gameScreen = gameScreen;

        setFillParent(true);

        update();
    }

    public void update() {

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

        if (purchaseLayer != null) {
            purchaseLayer.getRoot().setTouchable(Touchable.childrenOnly);
            addActor(purchaseLayer.getRoot());
        } else {
            addActor(createDummy("purchaseLayer"));
        }

        if (progressBarLayer != null) {
            progressBarLayer.getRoot().setTouchable(Touchable.childrenOnly);
            addActor(progressBarLayer.getRoot());
        } else {
            addActor(createDummy("progressBarLayer"));
        }

        if (interactionProgressBarLayer != null) {
            interactionProgressBarLayer.getRoot().setTouchable(Touchable.childrenOnly);
            addActor(interactionProgressBarLayer.getRoot());
        } else {
            addActor(createDummy("interactionProgressBarLayer"));
        }

        if (achievementLayer != null) {
            achievementLayer.getRoot().setTouchable(Touchable.childrenOnly);
            addActor(achievementLayer.getRoot());
        } else {
            addActor(createDummy("achievementLayer"));
        }

        if (controlsLayer != null) {
            controlsLayer.getRoot().setTouchable(Touchable.childrenOnly);
            addActor(controlsLayer.getRoot());
        } else {
            addActor(createDummy("controlsLayer"));
        }

    }

    public void setAchievementLayer(Fragment actor) {
        if (achievementLayer != null) {
            removeActor(achievementLayer.getRoot());
            achievementLayer.destroy();
        }
        achievementLayer = actor;

        update();
    }

    public void setPurchaseLayer(Fragment actor) {
        if (purchaseLayer != null) {
            removeActor(purchaseLayer.getRoot());
            purchaseLayer.destroy();
        }
        purchaseLayer = actor;

        update();
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

        progressBarLayer = actor;

        update();
    }

    public void setInteractionProgressBarLayer(Fragment actor) {
        if (interactionProgressBarLayer != null) {
            removeActor(interactionProgressBarLayer.getRoot());
            interactionProgressBarLayer.destroy();
        }
        interactionProgressBarLayer = actor;

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
            controlsLayer.destroy();
            controlsLayer = null;
        }
        if (progressBarLayer != null) {
            progressBarLayer.destroy();
            progressBarLayer = null;
        }
        if (storyDecisionsLayer != null) {
            storyDecisionsLayer.destroy();
            storyDecisionsLayer = null;
        }
        if (interactionLayer != null) {
            interactionLayer.destroy();
            interactionLayer = null;
        }
        if (backgroundLayer != null) {
            backgroundLayer.destroy();
            backgroundLayer = null;
        }
        if (interactionProgressBarLayer != null) {
            interactionProgressBarLayer.destroy();
            interactionProgressBarLayer = null;
        }
        if (purchaseLayer != null) {
            purchaseLayer.destroy();
            purchaseLayer = null;
        }
        if (achievementLayer != null) {
            achievementLayer.destroy();
            achievementLayer = null;
        }
    }
}
