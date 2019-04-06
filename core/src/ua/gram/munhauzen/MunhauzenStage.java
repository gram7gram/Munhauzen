package ua.gram.munhauzen;

import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class MunhauzenStage extends Stage {

    public MunhauzenStage(MunhauzenGame game) {
        super(game.view, game.batch);

        setDebugAll(MunhauzenGame.DEBUG);
    }
}
