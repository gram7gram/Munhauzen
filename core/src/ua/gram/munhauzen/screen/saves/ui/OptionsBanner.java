package ua.gram.munhauzen.screen.saves.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import ua.gram.munhauzen.ButtonBuilder;
import ua.gram.munhauzen.FontProvider;
import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.screen.SavesScreen;
import ua.gram.munhauzen.screen.saves.fragment.OptionsFragment;
import ua.gram.munhauzen.ui.Banner;
import ua.gram.munhauzen.ui.PrimaryButton;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class OptionsBanner extends Banner<SavesScreen> {

    private final String tag = getClass().getSimpleName();
    public final OptionsFragment fragment;
    PrimaryButton startBtn, saveBtn;

    public OptionsBanner(OptionsFragment fragment) {
        super(fragment.screen);

        this.fragment = fragment;
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        GameState state = screen.game.gameState;

        startBtn.setDisabled(fragment.save.chapter == null);
        saveBtn.setDisabled(state.activeSave.chapter == null);
    }

    @Override
    public Texture getBackgroundTexture() {
        return screen.assetManager.get("ui/banner_fond_3.png", Texture.class);
    }

    @Override
    public Table createContent() {

        Log.i(tag, "create");

        float minWidth = MunhauzenGame.WORLD_WIDTH * .9f;

        Table content = new Table();
        content.pad(70);

        float cellMinWidth = minWidth - content.getPadLeft() - content.getPadRight();

        saveBtn = screen.game.buttonBuilder.primary(screen.game.t("save_options_banner.save_btn"), new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                Log.i(tag, "save clicked");

                try {

                    screen.game.sfxService.onSaveOptionClicked();

                    fragment.showSaveOption();

                } catch (Throwable e) {
                    Log.e(tag, e);

                    screen.onCriticalError(e);
                }
            }
        });

        startBtn = screen.game.buttonBuilder.primary(screen.game.t("save_options_banner.load_btn"), new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                Log.i(tag, "load clicked");

                try {

                    screen.game.sfxService.onLoadOptionClicked();

                    fragment.showLoadOption();

                } catch (Throwable e) {
                    Log.e(tag, e);

                    screen.onCriticalError(e);
                }
            }
        });

        Label title = new Label(screen.game.t("save_options_banner.title"), new Label.LabelStyle(
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
                .height(ButtonBuilder.BTN_PRIMARY_HEIGHT)
                .padLeft(5)
                .padRight(5);

        content.add(startBtn).right()
                .width(cellMinWidth / 2f - 10)
                .height(ButtonBuilder.BTN_PRIMARY_HEIGHT)
                .padLeft(5)
                .padRight(5)
                .row();

        return content;
    }
}