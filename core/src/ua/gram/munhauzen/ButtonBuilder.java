package ua.gram.munhauzen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Disposable;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class ButtonBuilder implements Disposable {

    private final MunhauzenGame game;
    private final Texture decoration;

    public ButtonBuilder(MunhauzenGame game) {
        this.game = game;
        decoration = game.assetManager.get("ui/b_primary_enabled.9.png", Texture.class);
    }

    public Button primary(String text, final Runnable onClick) {

        NinePatchDrawable background = new NinePatchDrawable(new NinePatch(decoration, 90, 90, 0, 0));

        TextButton button = new TextButton(text, new TextButton.TextButtonStyle(
                background,
                background,
                background,
                game.fontProvider.getFont(FontProvider.BuxtonSketch, FontProvider.h3)
        ));
        button.getLabel().setColor(Color.BLACK);

        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                Gdx.app.postRunnable(onClick);
            }
        });

        return button;
    }

    @Override
    public void dispose() {

    }
}
