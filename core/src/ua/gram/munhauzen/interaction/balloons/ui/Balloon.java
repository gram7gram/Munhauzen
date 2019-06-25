package ua.gram.munhauzen.interaction.balloons.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.ui.FitImage;
import ua.gram.munhauzen.utils.Random;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class Balloon extends FitImage {

    public boolean isLocked;
    public Runnable onMiss;

    public Balloon(Texture texture, int width, int height, float x, float y) {
        super(texture);

        setSize(width, height);
        setPosition(x, y);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

    }

    public void start() {

        clear();

        Random r = new Random();

        int newX = r.between(0, (int) (MunhauzenGame.WORLD_WIDTH - getWidth()));

        isLocked = true;

        setVisible(true);
        addAction(
                Actions.sequence(
                        Actions.moveTo(newX, -getHeight()),
                        Actions.delay(r.between(0, 2)),
                        Actions.moveTo(newX, MunhauzenGame.WORLD_HEIGHT, 5),

                        Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                isLocked = false;

                                if (onMiss != null) {
                                    addAction(Actions.run(onMiss));
                                }
                            }
                        })
                )

        );
    }
}
