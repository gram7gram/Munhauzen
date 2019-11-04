package ua.gram.munhauzen.ui;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;

public class PrimaryImageButton extends ImageButton {

    public PrimaryImageButton(ImageButtonStyle style) {
        super(style);
    }

    @Override
    public void setDisabled(boolean isDisabled) {
        super.setDisabled(isDisabled);

        setTouchable(isDisabled ? Touchable.disabled : Touchable.enabled);
    }
}
