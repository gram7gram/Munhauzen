package ua.gram.munhauzen.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

import ua.gram.munhauzen.MunhauzenGame;

public class MunhauzenStage extends Stage {

    final int notchPadding;

    public MunhauzenStage(MunhauzenGame game) {
        super(game.view, game.batch);

        setDebugAll(MunhauzenGame.DEBUG);

        notchPadding = 100;
    }

    @Override
    public void addActor(Actor actor) {

//        Container<Actor> container = new Container<>(actor);
//        container.setBounds(0,0, MunhauzenGame.WORLD_WIDTH, MunhauzenGame.WORLD_HEIGHT);
//        container.setFillParent(true);
//        container.padTop(notchPadding);
//        container.align(Align.bottom);

        super.addActor(actor);
    }
}
