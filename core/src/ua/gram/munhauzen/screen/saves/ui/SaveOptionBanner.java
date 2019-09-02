package ua.gram.munhauzen.screen.saves.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import ua.gram.munhauzen.FontProvider;
import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.screen.saves.fragment.OptionsFragment;
import ua.gram.munhauzen.ui.PrimaryButton;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class SaveOptionBanner extends Banner {

    private final String tag = getClass().getSimpleName();
    public final OptionsFragment fragment;

    public SaveOptionBanner(OptionsFragment fragment) {
        super(fragment.screen);

        this.fragment = fragment;
    }

    @Override
    Texture getBackgroundTexture() {
        return screen.assetManager.get("ui/banner_fond_3.png", Texture.class);
    }

    @Override
    Table createContent() {

        Log.i(tag, "create");

        float minWidth = MunhauzenGame.WORLD_WIDTH * .9f;

        Table content = new Table();
        content.pad(70);

        float cellMinWidth = minWidth - content.getPadLeft() - content.getPadRight();

        PrimaryButton saveBtn = screen.game.buttonBuilder.primary("Yes", new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                Log.i(tag, "yes clicked");

                try {

                    screen.game.sfxService.onSaveOptionYesClicked();

                    fragment.onSaveClicked();

                } catch (Throwable e) {
                    Log.e(tag, e);

                    screen.onCriticalError(e);
                }
            }
        });

        PrimaryButton startBtn = screen.game.buttonBuilder.primary("No", new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                Log.i(tag, "no clicked");

                try {

                    screen.game.sfxService.onSaveOptionNoClicked();

                    fragment.restoreOptions();

                } catch (Throwable e) {
                    Log.e(tag, e);

                    screen.onCriticalError(e);
                }
            }
        });

        Label title = new Label("Current story progress will be saved here.\nProceed?", new Label.LabelStyle(
                screen.game.fontProvider.getFont(FontProvider.h2),
                Color.BLACK
        ));
        title.setWrap(true);
        title.setAlignment(Align.center);

        content.add(title).center().colspan(2).padBottom(30)
                .width(cellMinWidth)
                .row();

        content.add(saveBtn).left()
                .width(cellMinWidth / 2f - 10)
                .height(MunhauzenGame.WORLD_HEIGHT / 15f)
                .padLeft(5)
                .padRight(5);

        content.add(startBtn).right()
                .width(cellMinWidth / 2f - 10)
                .height(MunhauzenGame.WORLD_HEIGHT / 15f)
                .padLeft(5)
                .padRight(5)
                .row();

        return content;
    }
}