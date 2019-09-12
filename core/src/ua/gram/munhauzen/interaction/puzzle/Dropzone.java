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

        float scaleFactor = interaction.gameScreen.game.params.scaleFactor;
        float backgroundScale = interaction.imageFragment.backgroundImage.backgroundScale;

        float scale = .85f;
        float width = 200 * scaleFactor;
        float height = width * (1f / scale);

        setSize(
                width * backgroundScale,
                height * backgroundScale
        );

        interaction.imageFragment.setPositionRelativeToBackground(this, 310, 630);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        bounds.set(getX(), getY(), getWidth(), getHeight());
    }
}