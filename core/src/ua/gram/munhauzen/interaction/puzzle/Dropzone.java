package ua.gram.munhauzen.interaction.puzzle;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

import ua.gram.munhauzen.interaction.PuzzleInteraction;
import ua.gram.munhauzen.screen.game.ui.BackgroundImage;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class Dropzone extends Actor {

    final String tag = getClass().getSimpleName();
    final PuzzleInteraction interaction;
    final BackgroundImage backgroundImage;
    final Rectangle bounds;

    public Dropzone(PuzzleInteraction interaction) {
        bounds = new Rectangle();
        this.interaction = interaction;
        this.backgroundImage = interaction.imageFragment.backgroundImage;
    }

    public float[] getPercentBounds() {
        return new float[]{
                24.21f, 22.59f, 40.54f, 41.36f
        };
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        float[] numbers = getPercentBounds();

        float width = backgroundImage.backgroundWidth * numbers[0] / 100;
        float height = backgroundImage.backgroundHeight * numbers[1] / 100;
        float x = backgroundImage.background.getX() + backgroundImage.backgroundWidth * numbers[2] / 100;
        float y = backgroundImage.background.getY() + (backgroundImage.backgroundHeight * (100 - numbers[3]) / 100) - height;

        setBounds(x, y, width, height);

        bounds.set(getX(), getY(), getWidth(), getHeight());
    }

    public void addPuzzle(String name) {

        name = name.toLowerCase();

        Log.i(tag, "addPuzzle " + name);

        try {
            if (interaction.decisionManager.items.contains(name)) {
                interaction.decisionManager.items.remove(name);
            } else {
                interaction.decisionManager.items.add(name);
            }

            interaction.decisionManager.decide();
        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }
}