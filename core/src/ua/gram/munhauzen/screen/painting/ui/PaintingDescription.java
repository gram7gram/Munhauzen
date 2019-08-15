package ua.gram.munhauzen.screen.painting.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;

import ua.gram.munhauzen.FontProvider;
import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.screen.PaintingScreen;

public class PaintingDescription extends Stack {

    final PaintingScreen screen;
    public final Frame frame;
    public final Image background;
    public final Table backgroundTable, lblTable;
    public float prefWidth, prefHeight;
    public float backgroundWidth, backgroundHeight;

    public PaintingDescription(PaintingScreen screen, Frame frame) {
        super();

        this.frame = frame;
        this.screen = screen;

        String text = screen.image.getDescription(screen.game.params.locale);

        background = new Image();

        Label label = new Label(text, new Label.LabelStyle(
                screen.game.fontProvider.getFont(FontProvider.h5),
                Color.BLACK
        ));
        label.setWrap(true);
        label.setAlignment(Align.center);

        backgroundTable = new Table();
        backgroundTable.add(background).top();

        lblTable = new Table();
        lblTable.pad(0, 50, 20, 30);
        lblTable.add(label).top().grow();

        addActor(backgroundTable);
        addActor(lblTable);

        setBackground(
                screen.assetManager.get("gallery/gv_paper_3.png", Texture.class)
        );

    }

    @Override
    public void layout() {
        super.layout();

        backgroundWidth = Math.max(
                prefWidth,
                lblTable.getPrefWidth() + lblTable.getPadRight() + lblTable.getPadLeft()
        );

        backgroundHeight = Math.max(
                prefHeight,
                lblTable.getPrefHeight() + lblTable.getPadTop() + lblTable.getPadBottom()
        );

        backgroundTable.getCell(background)
                .width(backgroundWidth)
                .height(backgroundHeight);

        Vector2 position = new Vector2();

        position.x = frame.getX();
        position.y = frame.getY();

        frame.localToStageCoordinates(position);

        position.x = (frame.frameWidth - backgroundWidth) / 2f;
        position.y -= backgroundHeight * .7f;

        setPosition(
                position.x,
                position.y
        );
    }

    public void setBackground(Texture texture) {

        SpriteDrawable drawable = new SpriteDrawable(new Sprite(texture));

        background.setDrawable(drawable);

        prefWidth = MunhauzenGame.WORLD_WIDTH / 2f;
        float scale = 1f * prefWidth / drawable.getMinWidth();
        prefHeight = 1f * drawable.getMinHeight() * scale;

        layout();
    }
}
