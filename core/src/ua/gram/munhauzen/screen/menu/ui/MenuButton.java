package ua.gram.munhauzen.screen.menu.ui;

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
import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.screen.MenuScreen;
import ua.gram.munhauzen.screen.menu.animation.IconAnimation;
import ua.gram.munhauzen.ui.FitImage;
import ua.gram.munhauzen.utils.Log;

public abstract class MenuButton extends Stack {

    final String tag = getClass().getSimpleName();
    final MenuScreen screen;
    public float buttonWidth, buttonHeight;
    float lockWidth, lockHeight;
    int textSize;
    IconAnimation animation;
    public boolean hasLock;
    Actor lock;
    Image back;
    Table backContainer;
    final SpriteDrawable drawable;

    public MenuButton(MenuScreen screen) {
        this.screen = screen;

        buttonHeight = (MunhauzenGame.WORLD_HEIGHT - 40) / 7f;

        drawable = new SpriteDrawable(new Sprite(
                screen.assetManager.get("menu/mmv_btn.png", Texture.class)
        ));
    }

    protected void create(String text, final ClickListener onClick) {

        clear();
        clearChildren();

        back = new FitImage();
        backContainer = new Table();
        backContainer.add(back).grow();

        setBackground(drawable);

        lockHeight = buttonHeight * .5f;

        if (text.length() > 10) {
            textSize = FontProvider.h5;
        } else if (text.length() > 7) {
            textSize = FontProvider.h4;
        } else {
            textSize = FontProvider.h3;
        }

        BitmapFont font = screen.game.fontProvider.getFont(textSize);

        Label label = new Label(text, new Label.LabelStyle(font, Color.BLACK));
        label.setAlignment(Align.center);

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

                try {

                    screen.lockUI();

                    screen.game.stopCurrentSfx();

                    screen.game.sfxService.onAnyBtnClicked();

                    animation.start();

                    screen.layers.setBannerLayer(null);

                    GameState.clearTimer(tag);

                    Timer.instance().scheduleTask(new Timer.Task() {
                        @Override
                        public void run() {
                            try {
                                onClick.clicked(event, x, y);
                            } catch (Throwable e) {
                                Log.e(tag, e);

                                screen.onCriticalError(e);
                            }
                        }
                    }, 1);
                } catch (Throwable e) {
                    Log.e(tag, e);

                    screen.onCriticalError(e);
                }
            }
        });
    }

    private void setBackground(SpriteDrawable drawable) {

        back.setDrawable(drawable);

        float scale = 1f * buttonHeight / drawable.getMinHeight();
        buttonWidth = scale * drawable.getMinWidth();

        float maxWidth = MunhauzenGame.WORLD_WIDTH * .5f;
        if (buttonWidth > maxWidth) {
            buttonWidth = maxWidth;
            scale = 1f * buttonWidth / drawable.getMinWidth();
            buttonHeight = scale * drawable.getMinHeight();
        }

        backContainer.getCell(back)
                .width(buttonWidth)
                .height(buttonHeight);
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
        animation.layout();

        Table table = new Table();
        table.add(animation).expand()
                .align(Align.top)
                .width(animation.iconWidth)
                .height(animation.iconHeight);

        return table;
    }

    private Actor createLock() {

        Texture txt = screen.assetManager.get("menu/b_lock.png", Texture.class);

        Image img = new Image(txt);

        float scale = 1f * lockHeight / txt.getHeight();
        lockWidth = scale * txt.getWidth();

        Table table = new Table();
        table.add(img).expand()
                .align(Align.bottomRight)
                .width(lockWidth)
                .height(lockHeight);

        return table;
    }

    abstract IconAnimation createAnimationIcon();
}