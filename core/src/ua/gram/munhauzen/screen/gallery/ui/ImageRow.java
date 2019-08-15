package ua.gram.munhauzen.screen.gallery.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;

import ua.gram.munhauzen.FontProvider;
import ua.gram.munhauzen.entity.GalleryState;
import ua.gram.munhauzen.entity.Image;
import ua.gram.munhauzen.history.History;
import ua.gram.munhauzen.screen.GalleryScreen;
import ua.gram.munhauzen.screen.PaintingScreen;
import ua.gram.munhauzen.ui.FitImage;
import ua.gram.munhauzen.utils.Log;

public class ImageRow extends Stack {

    final String tag = getClass().getSimpleName();
    final GalleryScreen screen;
    final Image image;
    final int index;
    Label title, number;
    FitImage lock, unlock;
    Table content;
    float iconSize = 30;
    ClickListener clickListener;

    public ImageRow(final GalleryScreen screen, final Image image, int index, float width) {

        this.index = index;
        this.screen = screen;
        this.image = image;

        Label.LabelStyle style = new Label.LabelStyle(
                screen.game.fontProvider.getFont(FontProvider.h3),
                Color.BLACK
        );

        number = new Label(index + ".", style);
        number.setWrap(false);
        number.setAlignment(Align.center);

        title = new Label("", style);
        title.setWrap(false);
        title.setAlignment(Align.left);

        unlock = new FitImage();

        lock = new FitImage();
        lock.setRotation(-15);

        content = new Table();

        Container<Label> clippedLabel = new Container<>(title);
        clippedLabel.setClip(true);
        clippedLabel.align(Align.topLeft);

        content.add().width(iconSize).padTop(15).padRight(5).align(Align.topLeft);
        content.add(number).align(Align.topLeft).padRight(5);
        content.add(clippedLabel)
                .width(width - iconSize - number.getWidth())
                .align(Align.topLeft).row();

        addActor(content);

        clickListener = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                try {

                    screen.game.setScreen(new PaintingScreen(screen.game, image));
                    screen.dispose();

                } catch (Throwable e) {
                    Log.e(tag, e);

                    screen.onCriticalError(e);
                }
            }
        };
    }

    @Override
    public void layout() {
        super.layout();

        GalleryState galleryState = screen.game.gameState.galleryState;
        History history = screen.game.gameState.history;

        Cell iconCell = content.getCells().get(0);
        iconCell.clearActor();

        clearListeners();

        String text = image.getDescription(screen.game.params.locale);

        if (!history.viewedImages.contains(image.name)) {
            text = text.replaceAll("[^.\\s]", "#");

            iconCell.setActor(lock);

            setLockBackground(
                    screen.assetManager.get("gallery/b_closed_0.png", Texture.class)
            );
        } else if (!galleryState.visitedImages.contains(image.name)) {

            iconCell.setActor(unlock);

            setUnlockBackground(
                    screen.assetManager.get("gallery/b_opened_0.png", Texture.class)
            );

            addListener(clickListener);
        } else {
            addListener(clickListener);
        }

        title.setText(text);
    }

    public void setLockBackground(Texture texture) {

        lock.setDrawable(new SpriteDrawable(new Sprite(texture)));

        float width = iconSize;
        float scale = 1f * width / lock.getDrawable().getMinWidth();
        float height = 1f * lock.getDrawable().getMinHeight() * scale;

        content.getCell(lock)
                .width(width)
                .height(height);
    }

    public void setUnlockBackground(Texture texture) {

        unlock.setDrawable(new SpriteDrawable(new Sprite(texture)));

        float width = iconSize;
        float scale = 1f * width / unlock.getDrawable().getMinWidth();
        float height = 1f * unlock.getDrawable().getMinHeight() * scale;

        content.getCell(unlock)
                .width(width)
                .height(height);
    }
}
