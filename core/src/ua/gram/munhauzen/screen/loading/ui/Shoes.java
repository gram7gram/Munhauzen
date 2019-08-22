package ua.gram.munhauzen.screen.loading.ui;

import com.badlogic.gdx.graphics.Texture;

public class Shoes extends RotatingObject {

    public Shoes(Texture texture) {
        super(texture);

        float scale = .75f;

        this.width = 400 * scale;
        this.height = 200 * scale;
    }
}
