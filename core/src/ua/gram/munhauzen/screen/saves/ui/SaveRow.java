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
import ua.gram.munhauzen.entity.Save;
import ua.gram.munhauzen.repository.ChapterRepository;
import ua.gram.munhauzen.screen.SavesScreen;
import ua.gram.munhauzen.screen.saves.fragment.OptionsFragment;
import ua.gram.munhauzen.utils.DateUtils;
import ua.gram.munhauzen.utils.Log;

public class SaveRow extends Table {

    final String tag = getClass().getSimpleName();
    final SavesScreen screen;
    public final Save save;
    Label title, date;
    Image icon;
    float blockWidth;

    public SaveRow(final Save save, final SavesScreen screen) {

        blockWidth = MunhauzenGame.WORLD_WIDTH * .7f;
        float leftColumnWidth = blockWidth * .4f;
        float rightColumnWidth = blockWidth * .6f;

        this.screen = screen;
        this.save = save;

        icon = new Image();

        title = new Label("", new Label.LabelStyle(
                screen.game.fontProvider.getFont(FontProvider.h3),
                Color.BLACK
        ));
        title.setWrap(true);
        title.setAlignment(Align.left);

        date = new Label("", new Label.LabelStyle(
                screen.game.fontProvider.getFont(FontProvider.h5),
                Color.BLACK
        ));
        date.setAlignment(Align.left);

        Table lblContent = new Table();
        lblContent.add(title).width(rightColumnWidth).padBottom(5).row();
        lblContent.add(date).width(rightColumnWidth).left().row();

        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                try {

                    screen.game.sfxService.onListItemClicked();

                    screen.optionsFragment = new OptionsFragment(screen, save);
                    screen.optionsFragment.create();

                    screen.layers.setBannerLayer(screen.optionsFragment);

                    screen.optionsFragment.fadeIn();

                } catch (Throwable e) {
                    Log.e(tag, e);

                    screen.onCriticalError(e);
                }
            }
        });

        add(icon).padRight(5).size(leftColumnWidth - 5);
        add(lblContent).width(rightColumnWidth).row();

        init();
    }

    public void init() {

        String text = save.id + ". ";

        if (save.chapter != null) {

            Chapter chapter = ChapterRepository.find(screen.game.gameState, save.chapter);

            text += " " + chapter.description;

            date.setText(DateUtils.format(save.updatedAt, "HH:mm dd.MM.yyyy"));

            icon.setDrawable(new SpriteDrawable(new Sprite(
                    screen.assetManager.get(chapter.icon, Texture.class)
            )));

        } else {
            text += screen.game.t("saves.empty_save_title");

            date.setText("");
            icon.setDrawable(new SpriteDrawable(new Sprite(
                    screen.assetManager.get("saves/icon_question.png", Texture.class)
            )));

        }

        title.setText(text);

    }
}
