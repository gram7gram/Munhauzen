package ua.gram.munhauzen.transition;

import ua.gram.munhauzen.entity.StoryImage;
import ua.gram.munhauzen.screen.GameScreen;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public abstract class Transition {

    final GameScreen gameScreen;

    public Transition(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    public abstract void prepare(StoryImage item);
}
