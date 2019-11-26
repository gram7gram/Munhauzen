package ua.gram.munhauzen.ui;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class ProgressIconButton extends ImageButton {

    public ProgressIconButton(ImageButtonStyle style) {
        super(style);

        addCaptureListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                if (isDisabled()) {
                    event.cancel();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void layout() {
        super.layout();

        if (isDisabled()) {
            getColor().a = .5f;
        } else {
            getColor().a = 1;
        }
    }
}
