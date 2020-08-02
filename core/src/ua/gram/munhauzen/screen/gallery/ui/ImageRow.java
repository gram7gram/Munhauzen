package ua.gram.munhauzen.screen.gallery.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;

import ua.gram.munhauzen.FontProvider;
import ua.gram.munhauzen.screen.GalleryScreen;
import ua.gram.munhauzen.screen.PaintingScreen;
import ua.gram.munhauzen.screen.gallery.entity.PaintingImage;
import ua.gram.munhauzen.ui.FitImage;
import ua.gram.munhauzen.utils.Log;

public class ImageRow extends Stack {

    final String tag = getClass().getSimpleName();
    final GalleryScreen screen;
    final PaintingImage paintingImage;
    final int index;
    Label title, number;
    FitImage lock, unlock;
    Table content;
    float iconSize = 35;
    final Label.LabelStyle openedStyle, hiddenStyle;

    public ImageRow(final GalleryScreen screen, final PaintingImage paintingImage, int index, float width) {

        this.index = index;
        this.screen = screen;
        this.paintingImage = paintingImage;

        iconSize *= screen.game.params.scaleFactor;

        openedStyle = new Label.LabelStyle(
                screen.game.fontProvider.getFont(FontProvider.h4),
                Color.BLACK
        );

        hiddenStyle = new Label.LabelStyle(
                screen.game.fontProvider.getFont(FontProvider.SEGUISYM, FontProvider.h5),
                Color.BLACK
        );

        number = new Label(index + ".", openedStyle);
        number.setWrap(false);
        number.setAlignment(Align.left);

        title = new Label("", openedStyle);
        title.setWrap(true);
        title.setAlignment(Align.left);

        unlock = new FitImage();

        lock = new FitImage();
        lock.setRotation(-15);

        content = new Table();

        float lblWidth = width - iconSize - number.getWidth();

        content.add().width(iconSize).padTop(15).padRight(5).align(Align.topLeft);
        content.add(number).align(Align.topLeft).padRight(5);
        content.add(title)
                .width(lblWidth)
                .grow()
                .align(Align.topLeft).row();

        addActor(content);

        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                try {

                    screen.game.sfxService.onListItemClicked();

                    screen.navigateTo(new PaintingScreen(screen.game, paintingImage));

                } catch (Throwable e) {
                    Log.e(tag, e);

                    screen.onCriticalError(e);
                }
            }
        });

        init();
    }

    public void init() {

        Cell<?> iconCell = content.getCells().get(0);
        iconCell.clearActor();

        String text = paintingImage.image.description;

        title.setStyle(openedStyle);

        if (!paintingImage.isOpened) {

            title.setStyle(hiddenStyle);

            String altText = " ";//space

            for (int i = 0; i < text.length(); i++) {
                String ch = text.charAt(i) + "";

                if (!" ".equals(ch)) {
                    if (i % 4 == 0) {
                        ch = FontProvider.star1;
                    } else if (i % 3 == 0) {
                        ch = FontProvider.star2;
                    } else if (i % 2 == 0) {
                        ch = FontProvider.star3;
                    } else {
                        ch = FontProvider.star4;
                    }
                }

                altText += ch;
            }

            text = altText;

            iconCell.setActor(lock);

            updateLockBackground();

        } else if (!paintingImage.isViewed) {

            iconCell.setActor(unlock);

            updateUnlockBackground();
        }

        title.setText(text);
    }

    public void updateLockBackground() {

        Texture txt;
        if (!paintingImage.isStatue()) {
            txt = screen.assetManager.get("gallery/b_closed_0.png", Texture.class);
        } else {
            txt = screen.assetManager.get("gallery/b_star_black.png", Texture.class);
        }

        lock.setDrawable(new SpriteDrawable(new Sprite(txt)));

        float width = iconSize;
        float scale = 1f * width / lock.getDrawable().getMinWidth();
        float height = 1f * lock.getDrawable().getMinHeight() * scale;

        content.getCell(lock)
                .width(width)
                .height(height);
    }

    public void updateUnlockBackground() {
        Texture txt;
        if (!paintingImage.isStatue()) {
            txt = screen.assetManager.get("gallery/b_opened_0.png", Texture.class);
        } else {
            txt = screen.assetManager.get("gallery/b_star_color.png", Texture.class);
        }

        unlock.setDrawable(new SpriteDrawable(new Sprite(txt)));

        float width = iconSize;
        float scale = 1f * width / unlock.getDrawable().getMinWidth();
        float height = 1f * unlock.getDrawable().getMinHeight() * scale;

        content.getCell(unlock)
                .width(width)
                .height(height);
    }
}
