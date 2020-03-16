package ua.gram.munhauzen.interaction;

import ua.gram.munhauzen.entity.Story;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.utils.Log;
import ua.gram.munhauzen.utils.Random;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class RandomInteraction extends AbstractInteraction {

    public String scenario1, scenario2;

    public RandomInteraction(GameScreen gameScreen, String scenario1, String scenario2) {
        super(gameScreen);
        this.scenario1 = scenario1;
        this.scenario2 = scenario2;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public void start() {
        super.start();

        try {
            Random random = new Random();
            int value = random.between(1, 100);

            Story newStory;
            if (value == 1) {

                Log.i(tag, "Redirect to " + scenario1);

                newStory = gameScreen.storyManager.create(scenario1);
            } else {
                Log.i(tag, "Redirect to " + scenario2);

                newStory = gameScreen.storyManager.create(scenario2);
            }

            gameScreen.interactionService.complete();

            gameScreen.restoreProgressBarIfDestroyed();

            gameScreen.setStory(newStory);

            gameScreen.storyManager.startLoadingResources();
        } catch (Throwable e) {
            Log.e(tag, e);

            gameScreen.onCriticalError(e);
        }

    }
}
