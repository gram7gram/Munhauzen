package ua.gram.munhauzen.interaction.puzzle.ui;

import com.badlogic.gdx.graphics.Texture;

import ua.gram.munhauzen.interaction.PuzzleInteraction;
import ua.gram.munhauzen.interaction.puzzle.PuzzleItem;

public class Hair extends PuzzleItem {

    public Hair(PuzzleInteraction interaction, Texture texture) {
        super(interaction, texture);
    }

    @Override
    public float[] getPercentBounds() {
        return new float[]{
                13.46f, 6.53f, 60.25f, 52.79f
        };
    }
}
