package ua.gram.munhauzen.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class WrapLabel extends Label {

    public int lineCount;

    public WrapLabel(CharSequence text, Label.LabelStyle skin, float width) {
        super(text, skin);

        setWrap(true);

        pack();
        setWidth(width);

        pack();
        setWidth(width);

        lineCount = (int) Math.ceil(getHeight() / skin.font.getLineHeight());
    }

}
