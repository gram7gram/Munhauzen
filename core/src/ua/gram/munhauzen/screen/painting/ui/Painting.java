package ua.gram.munhauzen.screen.painting.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.screen.PaintingScreen;

public abstract class Painting extends Stack {

    final PaintingScreen screen;
    public final Image background;
    public final Frame frame;
    public final Table backgroundTable;
    public float backgroundWidth, backgroundHeight, backgroundScale;
    public boolean isWide;

    public Painting(PaintingScreen screen) {
        super();

        this.screen = screen;

        background = new Image();

        frame = createFrame();

        backgroundTable = new Table();
        backgroundTable.add(background).top();

        setBackground(
                screen.assetManager.get(screen.imageResource, Texture.class)
        );

        setFrameBackground(
                frame.createTexture()
        );

        float paddingTop = MunhauzenGame.WORLD_HEIGHT / 7f;

        Container<Table> c1 = new Container<>(backgroundTable);
        c1.padTop(paddingTop + (backgroundHeight * frame.framePadding / 2f));
        c1.align(Align.top);

        Container<Frame> c2 = new Container<>(frame);
        c2.padTop(paddingTop);
        c2.align(Align.top);

        setFillParent(true);
        addActor(c1);
        addActor(c2);
    }

    public abstract Frame createFrame();

    public void setBackground(Texture texture) {

        SpriteDrawable drawable = new SpriteDrawable(new Sprite(texture));

        background.clear();

        background.setDrawable(drawable);

        isWide = drawable.getMinWidth() > drawable.getMinHeight();

        if (isWide) {

            backgroundWidth = MunhauzenGame.WORLD_WIDTH * 2 / 3f;
            backgroundScale = 1f * backgroundWidth / drawable.getMinWidth();
            backgroundHeight = 1f * drawable.getMinHeight() * backgroundScale;

        } else {

            backgroundWidth = MunhauzenGame.WORLD_WIDTH / 2f;
            backgroundScale = 1f * backgroundWidth / drawable.getMinWidth();
            backgroundHeight = 1f * drawable.getMinHeight() * backgroundScale;
        }

        backgroundTable.getCell(background)
                .width(backgroundWidth)
                .height(backgroundHeight);
    }

    public void setFrameBackground(Texture texture) {
        frame.setBackground(texture, isWide);
    }
}
