package ua.gram.munhauzen.screen.chapters.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;

import ua.gram.munhauzen.FontProvider;
import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.Chapter;
import ua.gram.munhauzen.screen.ChaptersScreen;
import ua.gram.munhauzen.ui.FitImage;
import ua.gram.munhauzen.utils.Log;

public class ChapterRow extends Table {

    final String tag = getClass().getSimpleName();
    final ChaptersScreen screen;
    final Chapter chapter;
    final int index;
    Label title, number;
    FitImage icon;
    float iconSize, width;
    final Label.LabelStyle openedStyle, hiddenStyle;

    public ChapterRow(final ChaptersScreen screen, final Chapter chapter, int index, float width) {

        this.width = width;
        this.index = index;
        this.screen = screen;
        this.chapter = chapter;

        iconSize = MunhauzenGame.WORLD_WIDTH * .12f;

        openedStyle = new Label.LabelStyle(
                screen.game.fontProvider.getFont(FontProvider.h4),
                Color.BLACK
        );

        hiddenStyle = new Label.LabelStyle(
                screen.game.fontProvider.getFont(FontProvider.h4),
                Color.GRAY
        );

        number = new Label(index + ".", openedStyle);
        number.setWrap(false);
        number.setAlignment(Align.left);

        title = new Label("", openedStyle);
        title.setWrap(true);
        title.setAlignment(Align.left);

        icon = new FitImage();

        add(icon).padRight(5).align(Align.topLeft);
        add(number).align(Align.topLeft).padRight(5);
        add(title).expandX().align(Align.topLeft).row();

        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                try {

                    screen.game.sfxService.onListItemClicked();

//                    screen.navigateTo(new PaintingScreen(screen.game, paintingImage));

                } catch (Throwable e) {
                    Log.e(tag, e);

                    screen.onCriticalError(e);
                }
            }
        });

        init();
    }

    public void init() {

        String text = chapter.description;

        title.setStyle(openedStyle);
        number.setStyle(openedStyle);

        boolean isViewed = screen.game.gameState.history.visitedChapters.contains(chapter.name);

        if (!isViewed) {

            title.setStyle(hiddenStyle);
            number.setStyle(hiddenStyle);

            setIconBackground(
                    screen.assetManager.get("gallery/b_closed_0.png", Texture.class)
            );
        } else {

            setIconBackground(
                    screen.assetManager.get(chapter.icon, Texture.class)
            );
        }

        title.setText(text);

        float lblWidth = width - iconSize - number.getWidth();

        getCell(title).width(lblWidth);
    }

    public void setIconBackground(Texture texture) {

        icon.setDrawable(new SpriteDrawable(new Sprite(texture)));

        float width = iconSize;
        float scale = 1f * width / icon.getDrawable().getMinWidth();
        float height = 1f * icon.getDrawable().getMinHeight() * scale;

        getCell(icon)
                .width(width)
                .height(height);
    }
}
