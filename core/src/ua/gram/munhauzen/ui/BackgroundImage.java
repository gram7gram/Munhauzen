package ua.gram.munhauzen.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Timer;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.utils.Log;

public class BackgroundImage extends Group {

    final String tag = getClass().getSimpleName();
    final GameScreen gameScreen;
    public ImageButton leftArrow, rightArrow;
    public final Image background, overlayTop, overlayBottom;
    public final Table backgroundTable, arrowsTable;
    public float backgroundWidth, backgroundHeight, overlayHeight, backgroundScale;
    ActorGestureListener backgroundListener;
    public boolean isWide;

    public BackgroundImage(GameScreen screen) {
        super();

        this.gameScreen = screen;

        leftArrow = getArrowLeft();
        rightArrow = getArrowRight();

        background = new FitImage();
        overlayTop = new Image();
        overlayBottom = new Image();

        backgroundTable = new Table();
        backgroundTable.setTouchable(Touchable.childrenOnly);
        backgroundTable.setFillParent(true);
        backgroundTable.add(background).center().expand().fill();

        float controlsSize = MunhauzenGame.WORLD_WIDTH / 15f;

        arrowsTable = new Table();
        arrowsTable.setTouchable(Touchable.childrenOnly);
        arrowsTable.pad(10);
        arrowsTable.setFillParent(true);
        arrowsTable.add(leftArrow).size(controlsSize).left();
        arrowsTable.add().center().grow();
        arrowsTable.add(rightArrow).size(controlsSize).right();

        arrowsTable.setVisible(false);
        overlayBottom.setVisible(false);
        overlayTop.setVisible(false);

        addActor(backgroundTable);
        addActor(arrowsTable);
        addActor(overlayTop);
        addActor(overlayBottom);

        backgroundListener = new ActorGestureListener() {

            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                super.tap(event, x, y, count, button);

                try {
                    gameScreen.stageInputListener.clicked(event, x, y);
                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }

            @Override
            public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
                super.pan(event, x, y, deltaX, deltaY);

                moveBackground(deltaX);

            }
        };

        leftArrow.addListener(new ActorGestureListener() {
            Timer.Task task;

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);

                if (task != null) {
                    task.cancel();
                    task = null;
                }

            }

            @Override
            public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
                super.touchDown(event, x, y, pointer, button);

                task = Timer.instance().scheduleTask(new Timer.Task() {
                    @Override
                    public void run() {
                        moveBackground(15);
                    }
                }, 0, 0.05f);
            }
        });

        rightArrow.addListener(new ActorGestureListener() {
            Timer.Task task;

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);

                if (task != null) {
                    task.cancel();
                    task = null;
                }

            }

            @Override
            public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
                super.touchDown(event, x, y, pointer, button);

                task = Timer.instance().scheduleTask(new Timer.Task() {
                    @Override
                    public void run() {
                        moveBackground(-15);
                    }
                }, 0, 0.05f);
            }
        });

        setOverlayTexture(
                gameScreen.assetManager.get("GameScreen/t_putty.png", Texture.class)
        );
    }

    public void setOverlayTexture(Texture texture) {

        SpriteDrawable drawable = new SpriteDrawable(new Sprite(texture));

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

        overlayBottom.setDrawable(drawable);
        overlayTop.setDrawable(drawable);
    }

    public void setBackgroundDrawable(SpriteDrawable drawable) {

        background.clear();

        background.setDrawable(drawable);

        isWide = drawable.getMinWidth() > drawable.getMinHeight();

        arrowsTable.setVisible(isWide);

        if (isWide) {

            backgroundHeight = MunhauzenGame.WORLD_HEIGHT;
            backgroundScale = 1f * backgroundHeight / drawable.getMinHeight();
            backgroundWidth = 1f * drawable.getMinWidth() * backgroundScale;

            background.addListener(backgroundListener);

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

    private ImageButton getArrowRight() {
        Texture skipForward = gameScreen.assetManager.get("ui/playbar_skip_forward.png", Texture.class);
        Texture skipForwardOff = gameScreen.assetManager.get("ui/playbar_skip_forward_off.png", Texture.class);

        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.up = new SpriteDrawable(new Sprite(skipForward));
        style.down = new SpriteDrawable(new Sprite(skipForward));
        style.disabled = new SpriteDrawable(new Sprite(skipForwardOff));

        return new ImageButton(style);
    }

    private ImageButton getArrowLeft() {
        Texture skipBack = gameScreen.assetManager.get("ui/playbar_skip_backward.png", Texture.class);
        Texture skipBackOff = gameScreen.assetManager.get("ui/playbar_skip_backward_off.png", Texture.class);

        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.up = new SpriteDrawable(new Sprite(skipBack));
        style.down = new SpriteDrawable(new Sprite(skipBack));
        style.disabled = new SpriteDrawable(new Sprite(skipBackOff));

        return new ImageButton(style);
    }

    public void setBackgroundListener(ActorGestureListener backgroundListener) {
        this.backgroundListener = backgroundListener;
    }

    public float moveBackground(float deltaX) {
        try {
            float xBefore = background.getX();
            float newX = xBefore + deltaX;

            float leftBound = -backgroundWidth + MunhauzenGame.WORLD_WIDTH;
            float rightBound = 0;

            if (leftBound <= newX && newX <= rightBound) {
                background.setX(newX);
            }

            if (background.getX() > rightBound) background.setX(rightBound);
            if (background.getX() < leftBound) background.setX(leftBound);

            rightArrow.setDisabled((int) background.getX() <= (int) leftBound);
            leftArrow.setDisabled((int) background.getX() >= (int) rightBound);

            return background.getX() - xBefore;

        } catch (Throwable e) {
            Log.e(tag, e);
        }

        return 0;
    }
}