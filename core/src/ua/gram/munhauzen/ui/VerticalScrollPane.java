package ua.gram.munhauzen.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class VerticalScrollPane extends ScrollPane {

    public VerticalScrollPane(Actor widget) {
        super(widget);

        setScrollingDisabled(true, false);
    }
}
