package ua.gram.munhauzen.service;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Timer;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.entity.Scenario;
import ua.gram.munhauzen.entity.Story;
import ua.gram.munhauzen.entity.StoryImage;
import ua.gram.munhauzen.entity.StoryScenario;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.transition.FadeTransition;
import ua.gram.munhauzen.transition.NormalTransition;
import ua.gram.munhauzen.transition.Transition;
import ua.gram.munhauzen.utils.DateUtils;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public abstract class ImageService implements Disposable {

    protected final String tag = getClass().getSimpleName();
    protected final GameScreen gameScreen;
    protected final AssetManager assetManager;

    public ImageService(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        assetManager = createAssetManager();
    }

    public abstract AssetManager createAssetManager();

    public abstract String getResource(StoryImage item);

    public void prepare(StoryImage item, Timer.Task onComplete) {
        if (item.isPrepared && item.drawable != null) {
            if (item.isLocked && !item.isActive) {
                Timer.post(onComplete);
            }
            return;
        }

        String resource = getResource(item);

        boolean isLoaded = assetManager.isLoaded(resource, Texture.class);

        if (!item.isPreparing) {

            if (!isLoaded) {
                item.isPreparing = true;
                item.prepareStartedAt = new Date();

                assetManager.load(resource, Texture.class);
                return;
            }

        }

        if (isLoaded) {

            item.isPreparing = false;
            item.isPrepared = true;
            item.prepareCompletedAt = new Date();

            item.drawable = new SpriteDrawable(new Sprite(assetManager.get(resource, Texture.class)));

            Timer.post(onComplete);
        }
    }

    public void onPrepared(StoryImage item) {

        if (!item.isLocked) return;
        if (GameState.isPaused) return;
        if (item.isActive) return;

        Log.i(tag, "onPrepared " + getResource(item)
//                + " " + item.drawable.getMinWidth() + "x" + item.drawable.getMinHeight()
//                + " (" + MunhauzenGame.WORLD_WIDTH + "x" + MunhauzenGame.WORLD_HEIGHT + ")"
                + " in " + DateUtils.getDateDiff(item.prepareCompletedAt, item.prepareStartedAt, TimeUnit.MILLISECONDS) + "ms");

        item.prepareCompletedAt = null;
        item.prepareStartedAt = null;

        displayImage(item);
    }

    public void displayImage(final StoryImage item) {

        Log.i(tag, "displayImage " + getResource(item));

        Story story = gameScreen.getStory();
        for (StoryScenario scenarioOption : story.scenarios) {
            for (StoryImage image : scenarioOption.scenario.images) {
                image.isActive = false;
            }
        }

        item.isActive = true;

        Transition transition;

        if (Scenario.FADE_IN.equals(item.transition)) {
            transition = new FadeTransition(gameScreen);
        } else {
            transition = new NormalTransition(gameScreen);
        }

        transition.prepare(item);
    }

    public void update() {

        assetManager.update();
    }

    @Override
    public void dispose() {
        assetManager.clear();
    }
}
