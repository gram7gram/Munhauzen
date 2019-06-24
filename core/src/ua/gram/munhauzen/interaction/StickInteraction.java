package ua.gram.munhauzen.interaction;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

import ua.gram.munhauzen.screen.GameScreen;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class StickInteraction extends AbstractInteraction {

    public Table root;
    public String scenario1, scenario2;

    public StickInteraction(GameScreen gameScreen, String scenario1, String scenario2) {
        super(gameScreen);
        this.scenario1 = scenario1;
        this.scenario2 = scenario2;
    }

    @Override
    public void start() {
        super.start();


    }
}
