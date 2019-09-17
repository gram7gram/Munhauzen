package ua.gram.munhauzen.screen.painting.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;

import ua.gram.munhauzen.FontProvider;
import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.screen.PaintingScreen;
import ua.gram.munhauzen.screen.gallery.entity.PaintingImage;
import ua.gram.munhauzen.screen.painting.fragment.FullscreenFragment;
import ua.gram.munhauzen.utils.Log;

public abstract class Painting extends Group {

    final String tag = getClass().getSimpleName();
    final PaintingScreen screen;
    public final Image background, descriptionBackground;
    public final Frame frame;
    Table lblTable;
    Image lock, unlock;

    public float backgroundWidth, backgroundHeight, backgroundScale;
    public float lockWidth, lockHeight;
    public float descriptionBackgroundWidth, descriptionBackgroundHeight;
    public boolean isWide;

    public Painting(final PaintingScreen screen) {
        super();

        this.screen = screen;

        final PaintingImage img = screen.paintingFragment.paintingImage;

        background = new Image();
        lock = new Image();
        unlock = new Image();

        frame = createFrame();
        frame.addListener(new ActorGestureListener() {

            int delta;

            @Override
            public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
                super.touchDown(event, x, y, pointer, button);

                delta = (int) x;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);

                delta = (int) (delta - x);

                if (delta > 100) {
                    screen.nextPainting();
                } else if (delta < -100) {
                    screen.prevPainting();
                }
            }

            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                super.tap(event, x, y, count, button);

                try {

                    screen.fullscreenFragment = new FullscreenFragment(screen);
                    screen.fullscreenFragment.create();

                    screen.layers.setFullscreenLayer(screen.fullscreenFragment);

                    screen.fullscreenFragment.fadeIn();

                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }
        });

        descriptionBackground = new Image();

        Label descriptionLabel = new Label("", new Label.LabelStyle(
                screen.game.fontProvider.getFont(FontProvider.h5),
                Color.BLACK
        ));
        descriptionLabel.setWrap(true);
        descriptionLabel.setAlignment(Align.center);

        lblTable = new Table();
        lblTable.pad(0, 50, 20, 30);
        lblTable.add(descriptionLabel).top().grow();

        addActor(background);
        addActor(frame);

        setBackground(
                screen.paintingFragment.getPaintingTexture()
        );

        setFrameBackground(
                frame.createTexture()
        );

        if (img.isOpened) {
            addActor(descriptionBackground);
            addActor(lblTable);

            String text = img.image.description;
            descriptionLabel.setText(text);

            setDescriptionBackground(
                    screen.assetManager.get("ui/gv_paper_3.png", Texture.class)
            );
        }

        if (!img.isOpened) {
            addActor(lock);

            setLockBackground(
                    screen.assetManager.get("gallery/b_closed_1.png", Texture.class)
            );

        } else if (!img.isViewed) {

            addActor(unlock);

            setUnlockBackground(
                    screen.assetManager.get("gallery/b_opened_3.png", Texture.class)
            );

        }
    }

    public abstract Frame createFrame();

    @Override
    public void act(float delta) {
        super.act(delta);

        float descriptionWidth = descriptionBackground.getWidth();
        float descriptionHeight = Math.max(descriptionBackgroundHeight, lblTable.getPrefHeight());

        float pad = backgroundWidth * frame.framePadding;
        float frameWidth = backgroundWidth + 2 * pad;
        float frameHeight = backgroundHeight + 2 * pad;

        lblTable.setSize(
                descriptionWidth,
                descriptionHeight
        );

        descriptionBackground.setSize(
                descriptionWidth,
                descriptionHeight
        );

        frame.setSize(
                frameWidth,
                frameHeight
        );

        background.setPosition(
                (MunhauzenGame.WORLD_WIDTH - backgroundWidth) / 2f,
                isWide ? MunhauzenGame.WORLD_HEIGHT * .4f : MunhauzenGame.WORLD_HEIGHT * .3f
        );

        frame.setPosition(
                background.getX() - pad,
                background.getY() - pad
        );

        descriptionBackground.setPosition(
                (MunhauzenGame.WORLD_WIDTH - descriptionWidth) / 2f,
                background.getY() - descriptionHeight
        );

        lblTable.setPosition(
                (MunhauzenGame.WORLD_WIDTH - descriptionWidth) / 2f,
                background.getY() - descriptionHeight
        );

        lock.setPosition(
                background.getX() + backgroundWidth - lockWidth * .5f,
                background.getY() - lockHeight * .5f
        );

        unlock.setPosition(
                background.getX() + backgroundWidth - lockWidth * .5f,
                background.getY() - lockHeight * .5f
        );
    }

    public void setDescriptionBackground(Texture texture) {

        SpriteDrawable drawable = new SpriteDrawable(new Sprite(texture));

        descriptionBackground.setDrawable(drawable);

        descriptionBackgroundWidth = MunhauzenGame.WORLD_WIDTH / 2f;
        float scale = 1f * descriptionBackgroundWidth / drawable.getMinWidth();
        descriptionBackgroundHeight = 1f * drawable.getMinHeight() * scale;

        descriptionBackground.setSize(descriptionBackgroundWidth, descriptionBackgroundHeight);
    }

    public void setBackground(Texture texture) {

        SpriteDrawable drawable = new SpriteDrawable(new Sprite(texture));

        background.clear();

        background.setDrawable(drawable);

        isWide = drawable.getMinWidth() > drawable.getMinHeight();

        backgroundWidth = MunhauzenGame.WORLD_WIDTH * .625f;
        backgroundScale = 1f * backgroundWidth / drawable.getMinWidth();
        backgroundHeight = 1f * drawable.getMinHeight() * backgroundScale;

        background.setSize(backgroundWidth, backgroundHeight);
    }

    public void setFrameBackground(Texture texture) {
        frame.setBackground(texture, isWide);
    }

    public void setLockBackground(Texture texture) {

        lock.setDrawable(new SpriteDrawable(new Sprite(texture)));

        lockWidth = MunhauzenGame.WORLD_WIDTH * .1f;
        float scale = 1f * lockWidth / lock.getDrawable().getMinWidth();
        lockHeight = 1f * lock.getDrawable().getMinHeight() * scale;

        lock.setSize(lockWidth, lockHeight);
    }

    public void setUnlockBackground(Texture texture) {

        unlock.setDrawable(new SpriteDrawable(new Sprite(texture)));

        lockWidth = MunhauzenGame.WORLD_WIDTH * .1f;
        float scale = 1f * lockWidth / unlock.getDrawable().getMinWidth();
        lockHeight = 1f * unlock.getDrawable().getMinHeight() * scale;

        unlock.setSize(lockWidth, lockHeight);
    }
}
