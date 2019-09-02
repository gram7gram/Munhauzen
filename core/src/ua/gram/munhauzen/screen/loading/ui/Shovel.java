package ua.gram.munhauzen.screen.loading.ui;

import com.badlogic.gdx.graphics.Texture;

public class Shovel extends SlowRotatingObject {

    public Shovel(Texture texture) {
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
