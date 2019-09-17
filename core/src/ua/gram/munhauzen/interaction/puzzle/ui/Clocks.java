package ua.gram.munhauzen.interaction.puzzle.ui;

import com.badlogic.gdx.graphics.Texture;

import ua.gram.munhauzen.interaction.PuzzleInteraction;
import ua.gram.munhauzen.interaction.puzzle.PuzzleItem;

public class Clocks extends PuzzleItem {

    public Clocks(PuzzleInteraction interaction, Texture texture) {
        super(interaction, texture);
    }

    @Override
    public float[] getPercentBounds() {
        return new float[]{
                13.85f, 5.76f, 31.98f, 60.58f
        };
    }
}
