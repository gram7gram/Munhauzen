package ua.gram.munhauzen.screen.painting.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;

import ua.gram.munhauzen.FontProvider;
import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.screen.PaintingScreen;

public abstract class Painting extends Group {

    final PaintingScreen screen;
    public final Image background, descriptionBackground;
    public final Frame frame;
    Table lblTable;

    public float backgroundWidth, backgroundHeight, backgroundScale;
    public float descriptionBackgroundWidth, descriptionBackgroundHeight;
    public boolean isWide;

    public Painting(PaintingScreen screen) {
        super();

        this.screen = screen;

        background = new Image();

        frame = createFrame();

        String text = screen.image.getDescription(screen.game.params.locale);

        descriptionBackground = new Image();

        Label descriptionLabel = new Label(text, new Label.LabelStyle(
                screen.game.fontProvider.getFont(FontProvider.h5),
                Color.BLACK
        ));
        descriptionLabel.setWrap(true);
        descriptionLabel.setAlignment(Align.center);

        lblTable = new Table();
        lblTable.pad(0, 50, 20, 30);
        lblTable.add(descriptionLabel).top().grow();

        setBackground(
                screen.assetManager.get(screen.imageResource, Texture.class)
        );

        setFrameBackground(
                frame.createTexture()
        );

        setDescriptionBackground(
                screen.assetManager.get("gallery/gv_paper_3.png", Texture.class)
        );

        addActor(background);
        addActor(frame);
        addActor(descriptionBackground);
        addActor(lblTable);
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

        frame.setPosition(
                (MunhauzenGame.WORLD_WIDTH - frameWidth) / 2f,
                MunhauzenGame.WORLD_HEIGHT * .25f
        );

        background.setPosition(
                (MunhauzenGame.WORLD_WIDTH - backgroundWidth) / 2f,
                frame.getY() + pad
        );

        descriptionBackground.setPosition(
                (MunhauzenGame.WORLD_WIDTH - descriptionWidth) / 2f,
                background.getY() - descriptionHeight
        );

        lblTable.setPosition(
                (MunhauzenGame.WORLD_WIDTH - descriptionWidth) / 2f,
                background.getY() - descriptionHeight
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

        if (isWide) {

            backgroundWidth = MunhauzenGame.WORLD_WIDTH * 2 / 3f;
            backgroundScale = 1f * backgroundWidth / drawable.getMinWidth();
            backgroundHeight = 1f * drawable.getMinHeight() * backgroundScale;

        } else {

            backgroundWidth = MunhauzenGame.WORLD_WIDTH / 2f;
            backgroundScale = 1f * backgroundWidth / drawable.getMinWidth();
            backgroundHeight = 1f * drawable.getMinHeight() * backgroundScale;
        }

        background.setSize(backgroundWidth, backgroundHeight);
    }

    public void setFrameBackground(Texture texture) {
        frame.setBackground(texture, isWide);
    }
}
