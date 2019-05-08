package ua.gram.munhauzen.service;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Timer;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.OptionImage;
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
        if (item.isPrepared) return;

        String resource = item.getResource();

        if (!item.isPreparing) {

            if (!gameScreen.assetManager.isLoaded(resource, Texture.class)) {
                item.isPreparing = true;
                item.prepareStartedAt = new Date();

                gameScreen.assetManager.load(resource, Texture.class);
            }

        } else {

            if (gameScreen.assetManager.isLoaded(resource, Texture.class)) {

                item.isPreparing = false;
                item.isPrepared = true;
                item.prepareCompletedAt = new Date();

                item.image = gameScreen.assetManager.get(resource, Texture.class);

                Timer.post(onComplete);
            }
        }
    }

    public void onPrepared(OptionImage item) {

        Log.i(tag, "onPrepared " + item.id
                + " " + item.image.getWidth() + "x" + item.image.getHeight()
                + " (" + MunhauzenGame.WORLD_WIDTH + "x" + MunhauzenGame.WORLD_HEIGHT + ")"
                + " in " + DateUtils.getDateDiff(item.prepareCompletedAt, item.prepareStartedAt, TimeUnit.MILLISECONDS) + "ms");

        gameScreen.currentImage.setDrawable(new SpriteDrawable(new Sprite(item.image)));

        float scale = 1f * MunhauzenGame.WORLD_WIDTH / item.image.getWidth();
        float height = 1f * item.image.getHeight() * scale;

        gameScreen.currentImageTable.getCell(gameScreen.currentImage).width(MunhauzenGame.WORLD_WIDTH).height(height);

        Log.i(tag, "currentImage " + gameScreen.currentImage.getWidth() + "x" + gameScreen.currentImage.getHeight());

        boolean isOverlayVisible = gameScreen.currentImage.getHeight() < MunhauzenGame.WORLD_HEIGHT;
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
