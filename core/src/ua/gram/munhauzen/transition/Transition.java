package ua.gram.munhauzen.transition;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.OptionImage;
import ua.gram.munhauzen.entity.Scenario;
import ua.gram.munhauzen.screen.GameScreen;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public abstract class Transition {

    final String tag = getClass().getSimpleName();
    final GameScreen gameScreen;

    public Transition(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    public abstract void prepare(OptionImage item);

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

            gameScreen.overlayTop.setPosition(0, gameScreen.layer1Image.getY() - gameScreen.overlayTop.getHeight() / 2f);
            gameScreen.overlayBottom.setPosition(0, gameScreen.layer1Image.getY() + gameScreen.layer1Image.getHeight() - gameScreen.overlayTop.getHeight() / 2f);
        }
    }
}
