package ua.gram.munhauzen.interaction.puzzle;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

import ua.gram.munhauzen.interaction.PuzzleInteraction;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class Dropzone extends Actor {
    PuzzleInteraction interaction;
    Rectangle bounds;

    public Dropzone(PuzzleInteraction interaction) {
        bounds = new Rectangle();
        this.interaction = interaction;
    }

    public void init() {

        float scale = .85f;
        float width = 200;
        float height = width * (1 / scale);

        setSize(
                width * interaction.imageFragment.backgroundImage.backgroundScale,
                height * interaction.imageFragment.backgroundImage.backgroundScale
        );

        interaction.imageFragment.setPositionRelativeToBackground(this, 310, 630);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        bounds.set(getX(), getY(), getWidth(), getHeight());
    }
}