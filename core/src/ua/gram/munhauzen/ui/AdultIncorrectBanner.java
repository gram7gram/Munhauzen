package ua.gram.munhauzen.ui;

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
import ua.gram.munhauzen.screen.MunhauzenScreen;
import ua.gram.munhauzen.utils.Log;

public class AdultIncorrectBanner extends Banner<MunhauzenScreen> {

    final AdultGateFragment fragment;

    public AdultIncorrectBanner(AdultGateFragment fragment) {
        super(fragment.screen);

        this.fragment = fragment;
    }

    @Override
    public Texture getBackgroundTexture() {
        return screen.game.internalAssetManager.get("ui/banner_fond_1.png", Texture.class);
    }

    @Override
    public Table createContent() {

        float minWidth = MunhauzenGame.WORLD_WIDTH * .9f;

        Table content = new Table();
        content.pad(20, MunhauzenGame.WORLD_WIDTH * .1f, 40, MunhauzenGame.WORLD_WIDTH * .1f);

        float cellMinWidth = minWidth - content.getPadLeft() - content.getPadRight();

        PrimaryButton option1 = screen.game.buttonBuilder.danger(screen.game.t("adult_gate_screen.incorrect_yes"), new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                try {

                    screen.openAdultGateBanner(fragment.banner.task);

                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }
        });

        PrimaryButton option2 = screen.game.buttonBuilder.danger(screen.game.t("adult_gate_screen.incorrect_no"), new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                try {

                    fragment.fadeOut(new Runnable() {
                        @Override
                        public void run() {
                            screen.destroyBanners();
                        }
                    });

                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }
        });

        Label title = new Label(screen.game.t("adult_gate_screen.incorrect_title"), new Label.LabelStyle(
                screen.game.fontProvider.getFont(FontProvider.h2),
                Color.BLACK
        ));
        title.setAlignment(Align.center);
        title.setWrap(true);

        Table columns = new Table();

        columns.add(title)
                .minWidth(cellMinWidth)
                .padBottom(10)
                .center().row();

        Table buttons = new Table();
        buttons.add(option1).pad(5)
                .width(ButtonBuilder.BTN_PRIMARY_SM_WIDTH)
                .height(ButtonBuilder.BTN_PRIMARY_SM_HEIGHT);
        buttons.add(option2).pad(5)
                .width(ButtonBuilder.BTN_PRIMARY_SM_WIDTH)
                .height(ButtonBuilder.BTN_PRIMARY_SM_HEIGHT)
                .row();

        content.add(columns).row();
        content.add(buttons).row();

        return content;
    }

}
