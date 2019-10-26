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

public class BonusNotice extends Group {

    final PaintingScreen screen;
    public final Image stick, back;
    Table lblTable;
    float backgroundWidth, backgroundHeight;
    float stickWidth, stickHeight;

    public BonusNotice(PaintingScreen screen) {
        super();

        this.screen = screen;

        stick = new Image();
        back = new Image();

        Label lbl = new Label(screen.game.t("gallery.bonus_title"), new Label.LabelStyle(
                screen.game.fontProvider.getFont(FontProvider.h4),
                Color.BLACK
        ));
        lbl.setWrap(true);
        lbl.setAlignment(Align.center);

        lblTable = new Table();
        lblTable.pad(30);
        lblTable.add(lbl).top().grow();

        setBackground(
                screen.paintingFragment.assetManager.get("gallery/gv2_bonus_back.png", Texture.class)
        );

        setStickBackground(
                screen.paintingFragment.assetManager.get("gallery/gv2_bonus_stick.png", Texture.class)
        );

        addActor(stick);
        addActor(back);
        addActor(lblTable);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        float maxWidth = backgroundWidth;
        float maxHeight = Math.max(backgroundHeight, lblTable.getPrefHeight());

        lblTable.setSize(
                maxWidth,
                maxHeight
        );

        back.setSize(
                maxWidth,
                maxHeight
        );

        back.setPosition(30, 120);

        lblTable.setPosition(
                back.getX(),
                back.getY()
        );

        stick.setSize(
                stickWidth,
                back.getY() + 10
        );

        stick.setPosition(
                back.getX() + (maxWidth - stickWidth) / 2 - 10,
                0
        );
    }

    public void setBackground(Texture texture) {

        SpriteDrawable drawable = new SpriteDrawable(new Sprite(texture));

        back.setDrawable(drawable);

        backgroundWidth = MunhauzenGame.WORLD_WIDTH * .4f;
        float scale = 1f * backgroundWidth / drawable.getMinWidth();
        backgroundHeight = 1f * drawable.getMinHeight() * scale;

        back.setSize(backgroundWidth, backgroundHeight);
    }

    public void setStickBackground(Texture texture) {

        SpriteDrawable drawable = new SpriteDrawable(new Sprite(texture));

        stick.setDrawable(drawable);

        stickHeight = MunhauzenGame.WORLD_WIDTH / 3f;
        float scale = 1f * stickHeight / drawable.getMinHeight();
        stickWidth = 1f * drawable.getMinWidth() * scale;

        stick.setSize(stickWidth, stickHeight);

    }
}
