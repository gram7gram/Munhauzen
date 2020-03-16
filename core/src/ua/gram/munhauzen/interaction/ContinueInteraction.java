package ua.gram.munhauzen.interaction;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.entity.Story;
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
    public boolean isValid() {
        return true;
    }

    @Override
    public void start() {
        super.start();

        gameScreen.showProgressBar();

        assetManager.load("continue/btn_enabled.png", Texture.class);
        assetManager.load("continue/btn_disabled.png", Texture.class);
    }

    private void onResourcesLoaded() {
        isLoaded = true;

        imageFragment = new ContinueImageFragment(this);
        imageFragment.create();

        gameScreen.gameLayers.setInteractionLayer(imageFragment);

        imageFragment.fadeInRoot();

        GameState.pause(tag);
    }

    @Override
    public void update() {
        super.update();

        if (assetManager == null) return;

        if (!isLoaded) {
            if (assetManager.isFinished()) {
                onResourcesLoaded();
            }
            return;
        }

        gameScreen.showProgressBar();
        gameScreen.progressBarFragment.cancelFadeOut();

        if (imageFragment != null) {
            imageFragment.update();
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

                                        Story story = gameScreen.getStory();
                                        if (story == null) return;

                                        story.update(story.totalDuration, story.totalDuration);

                                        GameState.unpause(tag);

                                        gameScreen.interactionService.complete();

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

        if (imageFragment != null) {
            imageFragment.destroy();
            imageFragment = null;
        }

        isLoaded = false;
    }
}
