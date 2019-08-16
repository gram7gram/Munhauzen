package ua.gram.munhauzen.screen.painting.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.screen.painting.fragment.FullscreenFragment;
import ua.gram.munhauzen.utils.Log;

public class FullscreenImage extends Container<Image> {

    final String tag = getClass().getSimpleName();
    public final Image background;

    public float backgroundWidth, backgroundHeight, backgroundScale, zoom;
    public boolean isWide;

    public FullscreenImage(final FullscreenFragment fragment) {
        super();

        background = new Image();

        setBackground(
                fragment.screen.assetManager.get(fragment.screen.paintingImage.imageResource, Texture.class)
        );

        align(Align.center);
        setActor(background);

        addListener(new ActorGestureListener() {

            @Override
            public void zoom(InputEvent event, float initialDistance, float distance) {
                super.zoom(event, initialDistance, distance);

                if (distance > initialDistance) {
                    zoom += .1f;
                } else {
                    zoom -= .1f;
                }

                if (zoom < 1.3) {
                    zoom = 1.3f;
                }

                if (zoom > 2.5) {
                    zoom = 2.5f;
                }

                layout();
            }

            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                super.tap(event, x, y, count, button);

                fragment.fadeOut();
            }

            @Override
            public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
                super.pan(event, x, y, deltaX, deltaY);

                try {
                    float xBefore = background.getX();
                    float yBefore = background.getY();
                    int newX = (int) (xBefore + deltaX);
                    int newY = (int) (yBefore + deltaY);

                    int xBound = (int) (-backgroundWidth + MunhauzenGame.WORLD_WIDTH);
                    int yBound = (int) (-backgroundHeight + MunhauzenGame.WORLD_HEIGHT);

                    if (xBound <= newX && newX <= 0) {
                        background.setX(xBefore + deltaX);
                    }

                    if (yBound <= newY && newY <= 0) {
                        background.setY(yBefore + deltaY);
                    }

                } catch (Throwable e) {
                    Log.e(tag, e);
                }

            }
        });
    }

    public void setBackground(Texture texture) {

        SpriteDrawable drawable = new SpriteDrawable(new Sprite(texture));

        background.clear();

        background.setDrawable(drawable);

        isWide = drawable.getMinWidth() > drawable.getMinHeight();

        zoom = 1.3f;
    }

    @Override
    public void layout() {
        super.layout();

        backgroundHeight = MunhauzenGame.WORLD_HEIGHT * zoom;
        backgroundScale = 1f * backgroundHeight / background.getDrawable().getMinHeight();
        backgroundWidth = 1f * background.getDrawable().getMinWidth() * backgroundScale;

        background.setSize(backgroundWidth, backgroundHeight);

        background.setPosition(0, 0);

    }

}
