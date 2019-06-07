package ua.gram.munhauzen.service;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Timer;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.entity.Scenario;
import ua.gram.munhauzen.entity.Story;
import ua.gram.munhauzen.entity.StoryImage;
import ua.gram.munhauzen.entity.StoryScenario;
import ua.gram.munhauzen.fragment.ImageFragment;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.transition.FadeTransition;
import ua.gram.munhauzen.transition.NormalTransition;
import ua.gram.munhauzen.transition.Transition;
import ua.gram.munhauzen.utils.DateUtils;
import ua.gram.munhauzen.utils.Log;
import ua.gram.munhauzen.utils.MathUtils;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class ImageService {

    private final String tag = getClass().getSimpleName();
    private final GameScreen gameScreen;

    public ImageService(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    public void prepare(StoryImage item, Timer.Task onComplete) {
        if (item.isPrepared && item.drawable != null) {
            if (item.isLocked && !item.isActive) {
                Timer.post(onComplete);
            }
            return;
        }

        String resource;
        if (MunhauzenGame.DEBUG) {
            ArrayList<String> randomResources = new ArrayList<>();
            randomResources.add("images/image1.jpg");
            randomResources.add("images/image2.jpg");
            randomResources.add("images/image3.jpg");
            randomResources.add("images/image4.jpg");
            randomResources.add("images/image5.jpg");

            resource = MathUtils.random(randomResources);
        } else {
            resource = item.getResource();
        }

        boolean isLoaded = gameScreen.assetManager.isLoaded(resource, Texture.class);

        if (!item.isPreparing) {

            if (!isLoaded) {
                item.isPreparing = true;
                item.prepareStartedAt = new Date();

                gameScreen.assetManager.load(resource, Texture.class);
                return;
            }

        }

        if (isLoaded) {

            item.isPreparing = false;
            item.isPrepared = true;
            item.prepareCompletedAt = new Date();

            item.drawable = new SpriteDrawable(new Sprite(gameScreen.assetManager.get(resource, Texture.class)));

            Timer.post(onComplete);
        }
    }


    public void onPrepared(StoryImage item) {

        if (!item.isLocked) return;
        if (GameState.isPaused) return;
        if (item.isActive) return;

        Log.i(tag, "onPrepared " + item.image
                + " " + item.drawable.getMinWidth() + "x" + item.drawable.getMinHeight()
                + " (" + MunhauzenGame.WORLD_WIDTH + "x" + MunhauzenGame.WORLD_HEIGHT + ")"
                + " in " + DateUtils.getDateDiff(item.prepareCompletedAt, item.prepareStartedAt, TimeUnit.MILLISECONDS) + "ms");

        item.prepareCompletedAt = null;
        item.prepareStartedAt = null;

        displayImage(item);
    }

    public void displayImage(final StoryImage item) {

        Log.i(tag, "displayImage " + item.image);

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
        toggleLevel1Overlay();
        toggleLevel2Overlay();
    }

    public void toggleLevel1Overlay() {

        int height = Math.min(150, MunhauzenGame.WORLD_HEIGHT / 20);

        ImageFragment fragment = gameScreen.imageFragment;

        boolean isOverlayVisible = fragment.layer1Image.getHeight() < MunhauzenGame.WORLD_HEIGHT;

        fragment.layer1OverlayBottom.setVisible(isOverlayVisible);
        fragment.layer1OverlayTop.setVisible(isOverlayVisible);

        if (isOverlayVisible) {

            fragment.layer1OverlayBottom.setWidth(MunhauzenGame.WORLD_WIDTH);
            fragment.layer1OverlayBottom.setHeight(height);

            fragment.layer1OverlayTop.setWidth(MunhauzenGame.WORLD_WIDTH);
            fragment.layer1OverlayTop.setHeight(height);

            fragment.layer1OverlayBottom.setPosition(
                    0,
                    fragment.layer1Image.getY() - height / 2f);

            fragment.layer1OverlayTop.setPosition(
                    0,
                    fragment.layer1Image.getY() + fragment.layer1Image.getHeight() - height / 2f);

        }
    }

    public void toggleLevel2Overlay() {

        int height = 150;

        ImageFragment fragment = gameScreen.imageFragment;

        boolean isOverlayVisible = fragment.layer2Image.getHeight() < MunhauzenGame.WORLD_HEIGHT;

        fragment.layer2OverlayBottom.setVisible(isOverlayVisible);
        fragment.layer2OverlayTop.setVisible(isOverlayVisible);

        if (isOverlayVisible) {

            fragment.layer2OverlayBottom.setWidth(MunhauzenGame.WORLD_WIDTH);
            fragment.layer2OverlayBottom.setHeight(height);

            fragment.layer2OverlayTop.setWidth(MunhauzenGame.WORLD_WIDTH);
            fragment.layer2OverlayTop.setHeight(height);

            fragment.layer2OverlayBottom.setPosition(
                    0,
                    fragment.layer2Image.getY() - height / 2f);

            fragment.layer2OverlayTop.setPosition(
                    0,
                    fragment.layer2Image.getY() + fragment.layer2Image.getHeight() - height / 2f);

        }
    }
}
