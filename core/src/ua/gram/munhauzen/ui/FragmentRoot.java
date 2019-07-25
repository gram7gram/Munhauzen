package ua.gram.munhauzen.ui;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class FragmentRoot extends Stack {

    public FragmentRoot() {
        setFillParent(true);
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
