package ua.gram.munhauzen.interaction.puzzle.ui;

import com.badlogic.gdx.graphics.Texture;

import ua.gram.munhauzen.interaction.PuzzleInteraction;
import ua.gram.munhauzen.interaction.puzzle.PuzzleItem;

public class Peas extends PuzzleItem {

    public Peas(PuzzleInteraction interaction, Texture texture) {
        super(interaction, texture);
    }

    @Override
    public float[] getPercentBounds() {
        return new float[]{
                8.73f, 5.76f, 65.37f, 48.38f
        };
    }
}
