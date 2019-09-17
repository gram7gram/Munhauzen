package ua.gram.munhauzen.interaction.puzzle.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Touchable;

import ua.gram.munhauzen.interaction.PuzzleInteraction;
import ua.gram.munhauzen.interaction.puzzle.PuzzleItem;

public class Foot extends PuzzleItem {

    public Foot(PuzzleInteraction interaction, Texture texture) {
        super(interaction, texture);

        setTouchable(Touchable.disabled);
    }

    @Override
    public float[] getPercentBounds() {
        return new float[]{
                4.84f, 7.88f, 79.56f, 65.44f
        };
    }
}
