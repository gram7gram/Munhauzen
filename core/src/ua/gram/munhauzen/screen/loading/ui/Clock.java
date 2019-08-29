package ua.gram.munhauzen.screen.loading.ui;

import com.badlogic.gdx.graphics.Texture;

public class Clock extends RotatingObject {

    public Clock(Texture texture) {
        super(texture);
    }

    @Override
    protected float preferredWidth() {
        return super.preferredWidth() * .5f;
    }

    @Override
    protected float preferredHeight() {
        return super.preferredHeight() * .5f;
    }
}
