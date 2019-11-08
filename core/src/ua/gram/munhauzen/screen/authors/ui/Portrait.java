package ua.gram.munhauzen.screen.authors.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import ua.gram.munhauzen.MunhauzenGame;

public class Portrait extends Table {

    Image image;
    Label title;

    public Portrait(Texture texture, String text, final String link, Label.LabelStyle style) {

        float width = MunhauzenGame.WORLD_WIDTH * .3f;

        image = new Image(texture);

        title = new Label(text, style);
        title.setWrap(true);
        title.setAlignment(Align.center);

        float scale = width / texture.getWidth();
        float height = texture.getHeight() * scale;

        padTop(30);

        add(image).size(width, height).center().row();
        add(title).expandX().center().row();
        add(new Underline()).height(3).center().width(title.getWidth()).row();

        padBottom(30);

        if (link != null)
            addListener(new ClickListener() {

                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);

                    Gdx.net.openURI(link);
                }
            });
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }
}
