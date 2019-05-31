package ua.gram.munhauzen.transition;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.StoryImage;
import ua.gram.munhauzen.screen.GameScreen;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class FadeTransition extends Transition {

    Image targetImage;

    public FadeTransition(GameScreen gameScreen) {
        super(gameScreen);
    }

    @Override
    public void prepare(final StoryImage item) {

        gameScreen.layer2Image.clearListeners();
        gameScreen.layer2Image.clearActions();
        gameScreen.layer1Image.clearListeners();
        gameScreen.layer1Image.clearActions();

        gameScreen.layer1ImageGroup.setVisible(true);
        gameScreen.layer2ImageGroup.setVisible(true);

        gameScreen.layer1Image.addAction(Actions.alpha(1));
        gameScreen.layer2Image.addAction(Actions.alpha(0));

        targetImage = gameScreen.layer2Image;

        targetImage.setDrawable(item.drawable);

        if (item.drawable.getMinWidth() > item.drawable.getMinHeight()) {

            float scale = 1f * MunhauzenGame.WORLD_HEIGHT / item.drawable.getMinHeight();
            float width = 1f * item.drawable.getMinWidth() * scale;

            item.height = MunhauzenGame.WORLD_HEIGHT;
            item.width = width;

        } else {

            float scale = 1f * MunhauzenGame.WORLD_WIDTH / item.drawable.getMinWidth();
            float height = 1f * item.drawable.getMinHeight() * scale;

            item.width = MunhauzenGame.WORLD_WIDTH;
            item.height = height;
        }

        gameScreen.layer2ImageTable.getCell(targetImage)
                .width(item.width)
                .height(item.height);

        gameScreen.layer2Image.addAction(Actions.sequence(
                Actions.alpha(1, .3f),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        new NormalTransition(gameScreen).prepare(item);
                    }
                })
        ));
    }
}
