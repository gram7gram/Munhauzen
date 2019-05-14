package ua.gram.munhauzen.transition;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.OptionImage;
import ua.gram.munhauzen.screen.GameScreen;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class NormalTransition extends Transition {

    Image targetImage;

    public NormalTransition(GameScreen gameScreen) {
        super(gameScreen);
    }

    @Override
    public void prepare(final OptionImage item) {

        gameScreen.layer2Image.clearListeners();
        gameScreen.layer2Image.clearActions();
        gameScreen.layer1Image.clearListeners();
        gameScreen.layer1Image.clearActions();

        gameScreen.layer1ImageGroup.setVisible(true);
        gameScreen.layer2ImageGroup.setVisible(false);

        gameScreen.layer1Image.addAction(Actions.alpha(1));
        //gameScreen.layer2Image.addAction(Actions.alpha(1));

        targetImage = gameScreen.layer1Image;

        targetImage.setDrawable(item.image);

        if (item.image.getMinWidth() > item.image.getMinHeight()) {

            float scale = 1f * MunhauzenGame.WORLD_HEIGHT / item.image.getMinHeight();
            float width = 1f * item.image.getMinWidth() * scale;

            item.height = MunhauzenGame.WORLD_HEIGHT;
            item.width = width;

            targetImage.addListener(new ActorGestureListener() {
                @Override
                public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
                    super.pan(event, x, y, deltaX, deltaY);

                    float newX = targetImage.getX() + deltaX;
                    float currentWidth = item.width;
                    int viewportWidth = gameScreen.game.view.getScreenWidth();

                    float leftBound = -currentWidth + viewportWidth;
                    float rightBound = 0;

                    if (leftBound < newX && newX < rightBound) {
                        targetImage.addAction(Actions.moveBy(deltaX, 0));
                    }

                }
            });

        } else {

            float scale = 1f * MunhauzenGame.WORLD_WIDTH / item.image.getMinWidth();
            float height = 1f * item.image.getMinHeight() * scale;

            item.width = MunhauzenGame.WORLD_WIDTH;
            item.height = height;
        }

        gameScreen.layer1ImageTable.getCell(targetImage)
                .width(item.width)
                .height(item.height);

    }
}
