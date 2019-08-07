package ua.gram.munhauzen.service;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Timer;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import ua.gram.munhauzen.entity.Image;
import ua.gram.munhauzen.entity.Scenario;
import ua.gram.munhauzen.entity.Story;
import ua.gram.munhauzen.entity.StoryImage;
import ua.gram.munhauzen.entity.StoryScenario;
import ua.gram.munhauzen.repository.ImageRepository;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.screen.game.transition.FadeTransition;
import ua.gram.munhauzen.screen.game.transition.NormalTransition;
import ua.gram.munhauzen.screen.game.transition.Transition;
import ua.gram.munhauzen.utils.DateUtils;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public abstract class ImageService implements Disposable {

    protected final String tag = getClass().getSimpleName();
    protected final GameScreen gameScreen;
    public final AssetManager assetManager;
    Transition transition;

    public ImageService(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        assetManager = createAssetManager();
    }

    public abstract AssetManager createAssetManager();

    public abstract String getResource(StoryImage item);

    public void prepareAndDisplay(StoryImage item) {

        String resource = getResource(item);
        if (resource == null) return;

        item.resource = resource;

        Log.i(tag, "prepareAndDisplay " + item.resource);

        if (item.isPrepared && item.drawable != null) {
            onPrepared(item);
            return;
        }

        if (!assetManager.isLoaded(resource, Texture.class)) {
            assetManager.load(resource, Texture.class);

            assetManager.finishLoading();
        }

        item.isPreparing = false;
        item.isPrepared = true;
        item.prepareCompletedAt = new Date();

        item.drawable = new SpriteDrawable(new Sprite(assetManager.get(resource, Texture.class)));

        displayImage(item);
    }

    public void prepare(StoryImage item, Timer.Task onComplete) {
        if (item.isPrepared && item.drawable != null) {
            if (item.isLocked && !item.isActive) {
                Timer.post(onComplete);
            }
            return;
        }

        String resource = getResource(item);
        if (resource == null) return;

        item.resource = resource;

        boolean isLoaded = assetManager.isLoaded(resource, Texture.class);

        if (!isLoaded) {
            if (!item.isPreparing) {

                item.isPreparing = true;
                item.prepareStartedAt = new Date();

                assetManager.load(resource, Texture.class);
            }

        } else {

            item.isPreparing = false;
            item.isPrepared = true;
            item.prepareCompletedAt = new Date();

            item.drawable = new SpriteDrawable(new Sprite(assetManager.get(resource, Texture.class)));

            Timer.post(onComplete);
        }
    }

    public void onPrepared(StoryImage item) {

        if (item.isActive) return;

        long diff = DateUtils.getDateDiff(item.prepareCompletedAt, item.prepareStartedAt, TimeUnit.MILLISECONDS);

        item.prepareCompletedAt = null;
        item.prepareStartedAt = null;

        Log.i(tag, "onPrepared " + item.resource + " in " + diff + "ms");

        Story story = gameScreen.getStory();
        if (story != null) {
            for (StoryScenario scenarioOption : story.scenarios) {
                for (StoryImage image : scenarioOption.scenario.images) {
                    image.isActive = false;
                }
            }
        }

        saveCurrentBackground(item);

        displayImage(item);
    }

    public void displayImage(final StoryImage item) {

        if (transition != null) {
            if (transition.isLocked) return;
        }

        Log.i(tag, "displayImage " + item.resource);

        item.isActive = true;

        gameScreen.showImageFragment();

        if (Scenario.FADE_IN.equals(item.transition)) {
            transition = new FadeTransition(gameScreen);
        } else {
            transition = new NormalTransition(gameScreen);
        }

        transition.prepare(item);
    }

    public void update() {

        assetManager.update();

        Story story = gameScreen.getStory();
        if (story != null) {
            for (StoryScenario scenario : story.scenarios) {
                if (scenario.startsAt < story.progress) {
                    if (scenario.scenario.images != null) {
                        for (StoryImage storyImage : scenario.scenario.images) {
                            if (storyImage.startsAt <= story.progress) {
                                saveCurrentBackground(storyImage);
                            }
                        }
                    }
                }
            }
        }
    }

    public void saveCurrentBackground(StoryImage item) {

        if (item.image != null && !ImageRepository.LAST.equals(item.image)) {
            Image image = ImageRepository.find(gameScreen.game.gameState, item.image);

            if (gameScreen.getLastBackground() != image) {
                gameScreen.setLastBackground(image);
            }
        }
    }

    @Override
    public void dispose() {
        assetManager.dispose();
    }
}
