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

public class AdultGateBanner extends Banner<MunhauzenScreen> {

    final AdultGateFragment fragment;
    final Runnable task;

    public AdultGateBanner(AdultGateFragment fragment, Runnable task) {
        super(fragment.screen);

        this.fragment = fragment;
        this.task = task;
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

        FixedImage adultImg = new FixedImage(screen.game.internalAssetManager.get("purchase/adult.png", Texture.class), minWidth * .25f);
        FixedImage questionImg = new FixedImage(fragment.getQuestionTexture(), minWidth * .3f);

        float cellMinWidth = minWidth - content.getPadLeft() - content.getPadRight();

        ClickListener correctListener = new ClickListener() {
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

                    if (task != null)
                        task.run();

                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }
        };

        ClickListener incorrectListener = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                try {

                    fragment.showError();

                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }
        };

        PrimaryButton option1 = screen.game.buttonBuilder.danger(screen.game.t("adult_gate_screen.q"
                + fragment.question + "a1"), incorrectListener);
        PrimaryButton option2 = screen.game.buttonBuilder.danger(screen.game.t("adult_gate_screen.q"
                + fragment.question + "a2"), incorrectListener);
        PrimaryButton option3 = screen.game.buttonBuilder.danger(screen.game.t("adult_gate_screen.q"
                + fragment.question + "a3"), correctListener);
        PrimaryButton option4 = screen.game.buttonBuilder.danger(screen.game.t("adult_gate_screen.q"
                + fragment.question + "a4"), incorrectListener);

        Label title = new Label(screen.game.t("adult_gate_screen.title"), new Label.LabelStyle(
                screen.game.fontProvider.getFont(FontProvider.h3),
                Color.BLACK
        ));
        title.setAlignment(Align.center);
        title.setWrap(true);

        Label subtitle = new Label(screen.game.t("adult_gate_screen.subtitle"), new Label.LabelStyle(
                screen.game.fontProvider.getFont(FontProvider.p),
                Color.BLACK
        ));
        subtitle.setAlignment(Align.center);
        subtitle.setWrap(true);

        Table columns = new Table();

        columns.add(adultImg)
                .size(adultImg.width, adultImg.height)
                .padBottom(10)
                .center().row();

        columns.add(title)
                .minWidth(cellMinWidth)
                .padBottom(5)
                .center().row();

        columns.add(subtitle)
                .minWidth(cellMinWidth)
                .padBottom(5)
                .center().row();

        columns.add(questionImg)
                .size(questionImg.width, questionImg.height)
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
        buttons.add(option3).pad(5)
                .width(ButtonBuilder.BTN_PRIMARY_SM_WIDTH)
                .height(ButtonBuilder.BTN_PRIMARY_SM_HEIGHT);
        buttons.add(option4).pad(5)
                .width(ButtonBuilder.BTN_PRIMARY_SM_WIDTH)
                .height(ButtonBuilder.BTN_PRIMARY_SM_HEIGHT)
                .row();

        content.add(columns).row();
        content.add(buttons).row();

        return content;
    }

}
