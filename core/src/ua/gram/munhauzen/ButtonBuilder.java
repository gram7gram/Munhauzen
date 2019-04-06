package ua.gram.munhauzen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class ButtonBuilder implements Disposable {

    private final MunhauzenGame game;
    private Texture decoration, middle;

    public ButtonBuilder(MunhauzenGame game) {
        this.game = game;
        decoration = new Texture("ui/button_primary_decoration.png");
        middle = new Texture("ui/button_primary_middle.png");
    }

    public Actor primary(String text, final Runnable onClick) {
        HorizontalGroup button = new HorizontalGroup();

        Image decorLeft = new Image(decoration);
        Image decorRight = new Image(decoration);
        Image decorMiddle = new Image(middle);

        decorRight.setOrigin(decorRight.getWidth() / 2f, decorRight.getHeight() / 2f);
        decorRight.setRotation(180);

        Label label = new Label(text, new Label.LabelStyle(
                game.fontProvider.getFont(FontProvider.BuxtonSketch, FontProvider.h4),
                Color.BLACK
        ));
        label.setAlignment(Align.center);

        decorMiddle.setWidth(label.getWidth());

        Stack middleGroup = new Stack();
        middleGroup.add(decorMiddle);
        middleGroup.add(label);

        button.addActor(decorLeft);
        button.addActor(middleGroup);
        button.addActor(decorRight);

        button.setTouchable(Touchable.enabled);
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
        decoration.dispose();
        middle.dispose();
    }
}
