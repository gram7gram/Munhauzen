package ua.gram.munhauzen.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class FragmentRoot extends Stack {

    public FragmentRoot() {
        setFillParent(true);
    }

    @Override
    public void addActor(Actor actor) {
        super.addActor(actor);
    }

    public void addContainer(Container container) {
        addActor(container);
    }

    public void addContainer(Table container) {
        addActor(container);
    }

    public void addContainer(Group container) {
        addActor(container);
    }
}
