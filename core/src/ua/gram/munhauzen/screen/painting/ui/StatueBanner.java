package ua.gram.munhauzen.screen.painting.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;

import ua.gram.munhauzen.FontProvider;
import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.screen.PaintingScreen;
import ua.gram.munhauzen.screen.gallery.entity.PaintingImage;

public class StatueBanner extends Group {

    final PaintingScreen screen;
    public final Statue statue;
    public final Image back;
    Table lblTable;
    float backgroundWidth, backgroundHeight;

    public StatueBanner(PaintingScreen screen, Statue statue) {
        super();

        this.screen = screen;
        this.statue = statue;

        PaintingImage img = screen.paintingFragment.paintingImage;

        back = new Image();

        String text = img.inventory.description;

        Label lbl = new Label(text, new Label.LabelStyle(
                screen.game.fontProvider.getFont(FontProvider.h5),
                Color.BLACK
        ));
        lbl.setWrap(true);
        lbl.setAlignment(Align.center);

        lblTable = new Table();
        lblTable.pad(20, 0, 20, 120);
        lblTable.add(lbl).top().grow();

        setBackground(
                screen.paintingFragment.assetManager.get("ui/banner_fond_3.png", Texture.class)
        );

        addActor(back);
        addActor(lblTable);

        setVisible(false);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        lblTable.padLeft(statue.statueWidth + 10);

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

        back.setPosition(
                0,
                statue.item.getY() - 50
        );

        lblTable.setPosition(
                back.getX(),
                back.getY()
        );
    }

    public void setBackground(Texture texture) {

        SpriteDrawable drawable = new SpriteDrawable(new Sprite(texture));

        back.setDrawable(drawable);

        backgroundWidth = MunhauzenGame.WORLD_WIDTH * .8f;
        float scale = 1f * backgroundWidth / drawable.getMinWidth();
        backgroundHeight = 1f * drawable.getMinHeight() * scale;

        back.setSize(backgroundWidth, backgroundHeight);
    }

    public void fadeIn() {

        setVisible(true);
        addAction(Actions.sequence(
                Actions.alpha(0),
                Actions.moveTo(-backgroundWidth, getY()),
                Actions.parallel(
                        Actions.alpha(1, .2f),
                        Actions.moveTo(0, getY(), .2f)
                )
        ));
    }

    public void fadeOut() {

        setVisible(true);
        addAction(Actions.sequence(
                Actions.parallel(
                        Actions.alpha(0, .15f),
                        Actions.moveTo(-backgroundWidth, getY(), .15f)
                ),
                Actions.visible(false)
        ));
    }

}
