package ua.gram.munhauzen.transition;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import ua.gram.munhauzen.entity.StoryImage;
import ua.gram.munhauzen.fragment.ImageFragment;
import ua.gram.munhauzen.screen.GameScreen;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class NormalTransition extends Transition {

    public NormalTransition(GameScreen gameScreen) {
        super(gameScreen);
    }

    @Override
    public void prepare(final StoryImage item) {
        isLocked = true;

        final ImageFragment fragment = gameScreen.imageFragment;

        fragment.backgroundBottomImage.setVisible(true);
        fragment.backgroundTopImage.setVisible(false);

        fragment.backgroundBottomImage.addAction(Actions.alpha(1));

        fragment.backgroundBottomImage.setBackgroundDrawable(item.drawable);

        isLocked = false;
    }
}
