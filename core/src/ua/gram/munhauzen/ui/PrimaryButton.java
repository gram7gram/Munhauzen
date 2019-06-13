package ua.gram.munhauzen.ui;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class PrimaryButton extends TextButton {

    public PrimaryButton(String text, TextButtonStyle style) {
        super(text, style);
    }

    @Override
    public void setDisabled(boolean isDisabled) {
        super.setDisabled(isDisabled);

        setTouchable(isDisabled ? Touchable.disabled : Touchable.enabled);
    }
}
