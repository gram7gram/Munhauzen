package ua.gram.munhauzen.service;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Timer;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.OptionImage;
import ua.gram.munhauzen.entity.Scenario;
import ua.gram.munhauzen.entity.ScenarioOption;
import ua.gram.munhauzen.screen.GameScreen;
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

    public void displayImage(OptionImage item) {

        Log.i(tag, "displayImage " + item.id);

        Scenario scenario = gameScreen.getScenario();
        for (ScenarioOption scenarioOption : scenario.options) {
            for (OptionImage image : scenarioOption.option.images) {
                image.isActive = false;
            }
        }

        item.isActive = true;
        gameScreen.currentImage.setDrawable(item.image);

        float scale = 1f * MunhauzenGame.WORLD_WIDTH / item.image.getMinWidth();
        float height = 1f * item.image.getMinHeight() * scale;

        item.width = MunhauzenGame.WORLD_WIDTH;
        item.height = height;

        gameScreen.currentImageTable.getCell(gameScreen.currentImage)
                .width(item.width)
                .height(item.height);

        toggleOverlay();

    }

    public void toggleOverlay() {

        Scenario scenario = gameScreen.getScenario();

        boolean isOverlayVisible = false;

        if (scenario.currentOption != null) {
            if (scenario.currentOption.currentImage != null) {
                isOverlayVisible = scenario.currentOption.currentImage.height < MunhauzenGame.WORLD_HEIGHT;
            }
        }

        gameScreen.overlayTop.setVisible(isOverlayVisible);
        gameScreen.overlayBottom.setVisible(isOverlayVisible);

        if (isOverlayVisible) {

            gameScreen.overlayTableBottom.getCell(gameScreen.overlayBottom).width(MunhauzenGame.WORLD_WIDTH).height(150);
            gameScreen.overlayTableTop.getCell(gameScreen.overlayTop).width(MunhauzenGame.WORLD_WIDTH).height(150);

            gameScreen.overlayTop.setPosition(0, gameScreen.currentImage.getY() - gameScreen.overlayTop.getHeight() / 2f);
            gameScreen.overlayBottom.setPosition(0, gameScreen.currentImage.getY() + gameScreen.currentImage.getHeight() - gameScreen.overlayTop.getHeight() / 2f);
        }
    }
}
