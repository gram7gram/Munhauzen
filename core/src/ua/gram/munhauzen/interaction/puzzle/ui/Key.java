package ua.gram.munhauzen.interaction.puzzle.ui;

import com.badlogic.gdx.graphics.Texture;

import ua.gram.munhauzen.interaction.PuzzleInteraction;
import ua.gram.munhauzen.interaction.puzzle.PuzzleItem;

public class Key extends PuzzleItem {

    public Key(PuzzleInteraction interaction, Texture texture) {
        super(interaction, texture);
    }

    @Override
    public float[] getPercentBounds() {
        return new float[]{
                6.14f, 1.57f, 7.54f, 64.94f
        };
    }
}
