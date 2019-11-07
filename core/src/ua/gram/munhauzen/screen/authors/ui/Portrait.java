package ua.gram.munhauzen.screen.authors.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

public class Portrait extends Table {

    Image image;
    Label title;

    public Portrait(Texture texture, String text, Label.LabelStyle style, float width) {

        image = new Image(texture);

        title = new Label(text, style);
        title.setWrap(true);
        title.setAlignment(Align.center);

        float scale = width / texture.getWidth();
        float height = texture.getHeight() * scale;

        add(image).size(width, height).center().row();
        add(title).expandX().center().row();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }
}
