package ua.gram.munhauzen.service;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Timer;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.Option;
import ua.gram.munhauzen.entity.OptionImage;
import ua.gram.munhauzen.entity.Scenario;
import ua.gram.munhauzen.entity.ScenarioOption;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.transition.FadeTransition;
import ua.gram.munhauzen.transition.NormalTransition;
import ua.gram.munhauzen.transition.Transition;
import ua.gram.munhauzen.utils.DateUtils;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class ImageService {

    private final String tag = getClass().getSimpleName();
    private final GameScreen gameScreen;

    public ImageService(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    public void prepare(OptionImage item, Timer.Task onComplete) {
        if (item.isPrepared) {
            if (item.isLocked && !item.isActive) {
                Timer.post(onComplete);
            }
            return;
        }

        String resource = item.getResource();

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

            item.image = new SpriteDrawable(new Sprite(gameScreen.assetManager.get(resource, Texture.class)));

            Timer.post(onComplete);
        }
    }


    public void onPrepared(OptionImage item) {

        if (!item.isLocked) return;

        Log.i(tag, "onPrepared " + item.id
                + " " + item.image.getMinWidth() + "x" + item.image.getMinHeight()
                + " (" + MunhauzenGame.WORLD_WIDTH + "x" + MunhauzenGame.WORLD_HEIGHT + ")"
                + " in " + DateUtils.getDateDiff(item.prepareCompletedAt, item.prepareStartedAt, TimeUnit.MILLISECONDS) + "ms");

        displayImage(item);
    }

    public void displayImage(final OptionImage item) {

        Log.i(tag, "displayImage " + item.id);

        Scenario scenario = gameScreen.getScenario();
        for (ScenarioOption scenarioOption : scenario.options) {
            for (OptionImage image : scenarioOption.option.images) {
                image.isActive = false;
            }
        }

        item.isActive = true;

        Transition transition;

        if (Option.FADE_IN.equals(item.transition)) {
            transition = new FadeTransition(gameScreen);
        } else {
            transition = new NormalTransition(gameScreen);
        }

        transition.prepare(item);
    }
}
