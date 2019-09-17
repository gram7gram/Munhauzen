package ua.gram.munhauzen.interaction.puzzle.ui;

import com.badlogic.gdx.graphics.Texture;

import ua.gram.munhauzen.interaction.PuzzleInteraction;
import ua.gram.munhauzen.interaction.puzzle.PuzzleItem;

public class Arrows extends PuzzleItem {

    public Arrows(PuzzleInteraction interaction, Texture texture) {
        super(interaction, texture);
    }

    @Override
    public float[] getPercentBounds() {
        return new float[]{
                7.32f, 5.13f, 26.69f, 56.66f
        };
    }
}
