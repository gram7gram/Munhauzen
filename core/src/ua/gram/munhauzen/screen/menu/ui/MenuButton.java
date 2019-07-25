package ua.gram.munhauzen.screen.menu.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;

import ua.gram.munhauzen.FontProvider;
import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.animation.AnimatedImage;
import ua.gram.munhauzen.screen.MenuScreen;
import ua.gram.munhauzen.ui.FitImage;

public abstract class MenuButton extends Stack {

    final MenuScreen screen;
    float iconSize, buttonSize;
    AnimatedImage animation;

    public MenuButton(MenuScreen screen) {
        this.screen = screen;

        iconSize = MunhauzenGame.WORLD_HEIGHT * .05f;
        buttonSize = MunhauzenGame.WORLD_WIDTH * .45f;
    }

    protected void create(String text, final ClickListener onClick) {

        clear();
        clearChildren();

        SpriteDrawable backSprite = new SpriteDrawable(new Sprite(
                screen.assetManager.get("menu/mmv_btn.png", Texture.class)
        ));

        Image back = new FitImage(backSprite);

        float scale = 1f * buttonSize / backSprite.getMinWidth();
        float height = scale * backSprite.getMinHeight();

        BitmapFont font = screen.game.fontProvider.getFont(FontProvider.h3);

        Label label = new Label(text, new Label.LabelStyle(font, Color.BLACK));
        label.setAlignment(Align.center);

        Table imgContainer = new Table();
        imgContainer.add(back)
                .width(buttonSize)
                .maxWidth(buttonSize)
                .height(height)
                .maxHeight(height)
                .grow();

        Table labelContainer = new Table();
        labelContainer.add(label)
                .center().fillX().expand();

        addActor(imgContainer);
        addActor(labelContainer);
        addActor(createHeader());

        addListener(new ClickListener() {
            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                super.clicked(event, x, y);

                setTouchable(Touchable.disabled);

                animation.start();

                Timer.instance().scheduleTask(new Timer.Task() {
                    @Override
                    public void run() {
                        onClick.clicked(event, x, y);
                    }
                }, 1);
            }
        });
    }

    private Actor createHeader() {

        animation = createAnimationIcon();

        float scale = 1f * iconSize / animation.getCurrentDrawable().getMinWidth();
        float height = scale * animation.getCurrentDrawable().getMinHeight();

        Table table = new Table();
        table.add(animation).expand()
                .align(Align.top)
                .width(iconSize)
                .height(height);

        return table;
    }

    abstract AnimatedImage createAnimationIcon();
}