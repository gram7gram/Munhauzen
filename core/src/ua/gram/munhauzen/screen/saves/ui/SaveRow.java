package ua.gram.munhauzen.screen.saves.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;

import ua.gram.munhauzen.FontProvider;
import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.Chapter;
import ua.gram.munhauzen.entity.ChapterTranslation;
import ua.gram.munhauzen.history.Save;
import ua.gram.munhauzen.repository.ChapterRepository;
import ua.gram.munhauzen.screen.SavesScreen;
import ua.gram.munhauzen.screen.saves.fragment.SaveDialog;
import ua.gram.munhauzen.utils.DateUtils;
import ua.gram.munhauzen.utils.Log;

public class SaveRow extends Table {

    final String tag = getClass().getSimpleName();
    final SavesScreen screen;
    final Save save;
    Label title, time, date;
    Image icon;
    final SpriteDrawable fallbackIcon;
    float blockWidth;

    public SaveRow(final Save save, final SavesScreen screen) {

        blockWidth = MunhauzenGame.WORLD_WIDTH * .8f;
        float leftColumnWidth = blockWidth * .25f;
        float rightColumnWidth = blockWidth * .7f;

        this.screen = screen;
        this.save = save;

        icon = new Image();

        title = new Label("", new Label.LabelStyle(
                screen.game.fontProvider.getFont(FontProvider.h2),
                Color.BLACK
        ));
        title.setWrap(true);
        title.setAlignment(Align.center);

        time = new Label("", new Label.LabelStyle(
                screen.game.fontProvider.getFont(FontProvider.h4),
                Color.BLACK
        ));
        time.setAlignment(Align.center);

        date = new Label("", new Label.LabelStyle(
                screen.game.fontProvider.getFont(FontProvider.h4),
                Color.BLACK
        ));
        date.setAlignment(Align.center);

        Table lblContent = new Table();
        lblContent.add(title).width(rightColumnWidth).padBottom(5).row();
        lblContent.add(time).width(rightColumnWidth).row();
        lblContent.add(date).width(rightColumnWidth).row();

        fallbackIcon = new SpriteDrawable(new Sprite(
                screen.assetManager.get("saves/sv_baron.png", Texture.class)
        ));

        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                try {

                    screen.saveDialog = new SaveDialog(screen, save);
                    screen.saveDialog.create();

                    screen.layers.setBannerLayer(screen.saveDialog);

                    screen.saveDialog.fadeIn();

                } catch (Throwable e) {
                    Log.e(tag, e);

                    screen.onCriticalError(e);
                }
            }
        });

        add(icon).size(leftColumnWidth);
        add(lblContent).width(rightColumnWidth).row();
    }

    @Override
    public void layout() {
        super.layout();

        if (save.chapter != null) {

            Chapter chapter = ChapterRepository.find(screen.game.gameState, save.chapter);

            String text;
            ChapterTranslation translation = null;
            if (chapter.translations != null) {
                for (ChapterTranslation trans : chapter.translations) {
                    if (trans.locale.equals(screen.game.params.locale)) {
                        translation = trans;
                        break;
                    }
                }
            }

            if (translation == null) {
                text = save.id;
            } else {
                text = translation.description;
            }

            title.setText(text);
            time.setText(DateUtils.format(save.updatedAt, "HH:mm"));
            date.setText(DateUtils.format(save.updatedAt, "dd.MM.yyyy"));

            if (chapter.icon != null) {
                icon.setDrawable(new SpriteDrawable(new Sprite(
                        screen.assetManager.get(chapter.icon, Texture.class)
                )));
            } else {
                icon.setDrawable(fallbackIcon);
            }

        } else {
            title.setText("Empty save");
            date.setText("");
            time.setText("");
            icon.setDrawable(fallbackIcon);

        }

    }
}
