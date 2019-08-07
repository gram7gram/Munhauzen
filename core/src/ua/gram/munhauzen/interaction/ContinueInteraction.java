package ua.gram.munhauzen.interaction;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.entity.Story;
import ua.gram.munhauzen.entity.StoryInteraction;
import ua.gram.munhauzen.interaction.continye.fragment.ContinueImageFragment;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class ContinueInteraction extends AbstractInteraction {

    public boolean isLoaded;
    public ContinueImageFragment imageFragment;

    public ContinueInteraction(GameScreen gameScreen) {
        super(gameScreen);
    }

    @Override
    public void start() {
        super.start();

        gameScreen.showProgressBar();

        assetManager.load("continue/btn_enabled.png", Texture.class);
        assetManager.load("continue/btn_disabled.png", Texture.class);
    }

    private void onResourcesLoaded() {

        imageFragment = new ContinueImageFragment(this);
        imageFragment.create();

        gameScreen.gameLayers.setInteractionLayer(imageFragment);

        imageFragment.fadeInRoot();

        GameState.pause();
    }

    @Override
    public void update() {
        super.update();

        assetManager.update();

        if (!isLoaded) {
            if (assetManager.isFinished()) {
                onResourcesLoaded();
                isLoaded = true;
            }
            return;
        }

        try {

            if (imageFragment != null) {
                imageFragment.update();
            }

            Story story = gameScreen.getStory();
            if (story.currentScenario == null) return;

            String name = story.currentScenario.scenario.interaction;
            if (name == null) return;

            StoryInteraction interaction = story.currentInteraction;

            if (interaction != null) {

                interaction.isCompleted = false;
            }
        } catch (Throwable e) {
            Log.e(tag, e);

            gameScreen.onCriticalError(e);
        }
    }

    public void complete() {

        Log.i(tag, "complete");

        try {
            imageFragment.root.setTouchable(Touchable.disabled);

            imageFragment.root.addAction(
                    Actions.sequence(
                            Actions.alpha(0, .4f),
                            Actions.run(new Runnable() {
                                @Override
                                public void run() {
                                    try {

                                        GameState.unpause();

                                        //Continue interaction is NEVER completed.
                                        //When scrolling back the interaction should reappear
                                        gameScreen.interactionService.destroy();

                                        gameScreen.interactionService.findStoryAfterInteraction();

                                        gameScreen.restoreProgressBarIfDestroyed();
                                    } catch (Throwable e) {
                                        Log.e(tag, e);
                                    }
                                }
                            })
                    )
            );
        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }

    @Override
    public void dispose() {
        super.dispose();

        isLoaded = false;
    }
}
