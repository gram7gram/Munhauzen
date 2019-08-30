package ua.gram.munhauzen.screen.menu.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
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
    public boolean hasLock;
    Actor lock;
    Image back;
    Table backContainer;
    final SpriteDrawable enabledBack;

    public MenuButton(MenuScreen screen) {
        this.screen = screen;

        iconSize = MunhauzenGame.WORLD_HEIGHT * .05f;
        buttonSize = MunhauzenGame.WORLD_WIDTH * .45f;

        enabledBack = new SpriteDrawable(new Sprite(
                screen.assetManager.get("menu/mmv_btn.png", Texture.class)
        ));
    }

    protected void create(String text, final ClickListener onClick) {

        clear();
        clearChildren();

        back = new FitImage();

        BitmapFont font = screen.game.fontProvider.getFont(FontProvider.h3);

        Label label = new Label(text, new Label.LabelStyle(font, Color.BLACK));
        label.setAlignment(Align.center);

        backContainer = new Table();
        backContainer.add(back)
                .width(buttonSize)
                .maxWidth(buttonSize)
                .grow();

        Table labelContainer = new Table();
        labelContainer.add(label)
                .center().fillX().expand();

        lock = createLock();

        addActor(backContainer);
        addActor(labelContainer);
        addActor(createHeader());
        addActor(lock);

        addListener(new ClickListener() {
            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                super.clicked(event, x, y);

                Gdx.input.setInputProcessor(null);

                animation.start();

                screen.isButtonClicked = true;

                Timer.instance().scheduleTask(new Timer.Task() {
                    @Override
                    public void run() {
                        onClick.clicked(event, x, y);
                    }
                }, 1);
            }
        });

        setBackground(enabledBack);
    }

    private void setBackground(SpriteDrawable drawable) {

        back.setDrawable(drawable);

        final float scale = 1f * buttonSize / drawable.getMinWidth();
        float height = scale * drawable.getMinHeight();

        backContainer.getCell(back)
                .height(height)
                .maxHeight(height);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if (lock != null) {
            lock.setVisible(hasLock);
        }
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

    private Actor createLock() {

        Texture txt = screen.assetManager.get("menu/b_lock.png", Texture.class);

        Image img = new Image(txt);

        float width = iconSize * .9f;
        float scale = 1f * width / txt.getWidth();
        float height = scale * txt.getHeight();

        Table table = new Table();
        table.add(img).expand()
                .align(Align.bottomRight)
                .width(width)
                .height(height);

        return table;
    }

    abstract AnimatedImage createAnimationIcon();
}