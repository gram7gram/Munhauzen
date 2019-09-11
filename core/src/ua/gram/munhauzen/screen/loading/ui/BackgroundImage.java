package ua.gram.munhauzen.screen.loading.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.screen.LoadingScreen;
import ua.gram.munhauzen.ui.FitImage;

public class BackgroundImage extends Group {

    final LoadingScreen screen;
    public final Image background, overlayTop, overlayBottom;
    public final Table backgroundTable;
    public float backgroundWidth, backgroundHeight, overlayHeight, backgroundScale;
    public boolean isWide;

    public BackgroundImage(LoadingScreen screen) {
        super();

        this.screen = screen;

        background = new FitImage();
        overlayTop = new Image();
        overlayBottom = new Image();

        backgroundTable = new Table();
        backgroundTable.setTouchable(Touchable.childrenOnly);
        backgroundTable.setFillParent(true);
        backgroundTable.add(background).bottom().expand().fill();

        overlayBottom.setVisible(false);
        overlayTop.setVisible(false);

        addActor(backgroundTable);
        addActor(overlayTop);
//        addActor(overlayBottom);

        setOverlayTexture(
                screen.assetManager.get("loading/t_putty.png", Texture.class)
        );

        setBackgroundDrawable(new SpriteDrawable(new Sprite(
                screen.assetManager.get("loading/p_loading.jpg", Texture.class)
        )));
    }

    public void setOverlayTexture(Texture texture) {

        SpriteDrawable drawable = new SpriteDrawable(new Sprite(texture));
        overlayBottom.setDrawable(drawable);
        overlayTop.setDrawable(drawable);

//        Pixmap red = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
//        red.setColor(Color.RED);
//        red.fill();
//
//        Pixmap blue = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
//        blue.setColor(Color.BLUE);
//        blue.fill();
//
//        overlayBottom.setDrawable(new SpriteDrawable(new Sprite(new Texture(red))));
//        overlayTop.setDrawable(new SpriteDrawable(new Sprite(new Texture(blue))));
    }

    public void setBackgroundDrawable(SpriteDrawable drawable) {

        background.clear();

        background.setDrawable(drawable);

        isWide = drawable.getMinWidth() > drawable.getMinHeight();

        if (isWide) {

            backgroundHeight = MunhauzenGame.WORLD_HEIGHT;
            backgroundScale = 1f * backgroundHeight / drawable.getMinHeight();
            backgroundWidth = 1f * drawable.getMinWidth() * backgroundScale;

        } else {

            backgroundWidth = MunhauzenGame.WORLD_WIDTH;
            backgroundScale = 1f * backgroundWidth / drawable.getMinWidth();
            backgroundHeight = 1f * drawable.getMinHeight() * backgroundScale;
        }

        backgroundTable.getCell(background)
                .width(backgroundWidth)
                .height(backgroundHeight);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        toggleOverlay();

        updateOverlayPosition();
    }

    public void toggleOverlay() {

        overlayHeight = Math.min(150, MunhauzenGame.WORLD_HEIGHT / 20);

        overlayTop.setWidth(MunhauzenGame.WORLD_WIDTH);
        overlayTop.setHeight(overlayHeight);

        overlayBottom.setWidth(MunhauzenGame.WORLD_WIDTH);
        overlayBottom.setHeight(overlayHeight);

        boolean isOverlayVisible = !isWide && 0 < backgroundHeight && backgroundHeight < MunhauzenGame.WORLD_HEIGHT;

        overlayBottom.setVisible(isOverlayVisible);
        overlayTop.setVisible(isOverlayVisible);

    }

    public void updateOverlayPosition() {

        if (backgroundHeight > 0) {

            overlayBottom.setPosition(
                    0,
                    background.getY() - overlayHeight / 2f);

            overlayTop.setPosition(
                    0,
                    background.getY() + backgroundHeight - overlayHeight / 2f);
        } else {
            overlayBottom.setPosition(0, -overlayHeight);
            overlayTop.setPosition(0, MunhauzenGame.WORLD_HEIGHT);
        }

    }
}
