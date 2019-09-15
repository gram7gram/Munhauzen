package ua.gram.munhauzen.ui;

import com.badlogic.gdx.scenes.scene2d.Stage;

import ua.gram.munhauzen.MunhauzenGame;

public class MunhauzenStage extends Stage {

    public MunhauzenStage(MunhauzenGame game) {
        super(game.view, game.batch);

        setDebugAll(MunhauzenGame.DEBUG_UI);
    }
}
