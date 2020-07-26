package ua.gram.munhauzen.screen.menu.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import ua.gram.munhauzen.ButtonBuilder;
import ua.gram.munhauzen.FontProvider;
import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.screen.MenuScreen;
import ua.gram.munhauzen.screen.menu.fragment.RateFragment;
import ua.gram.munhauzen.ui.Banner;
import ua.gram.munhauzen.ui.FitImage;
import ua.gram.munhauzen.utils.Log;

public class RateBanner extends Banner<MenuScreen> {

    final RateFragment fragment;

    public RateBanner(RateFragment fragment) {
        super(fragment.screen);

        this.fragment = fragment;
    }

    @Override
    public Texture getBackgroundTexture() {
        return screen.assetManager.get("ui/banner_fond_1.png", Texture.class);
    }

    @Override
    public Table createContent() {

        float minWidth = MunhauzenGame.WORLD_WIDTH * .7f;

        Table content = new Table();
        content.pad(20, MunhauzenGame.WORLD_WIDTH * .1f, 40, MunhauzenGame.WORLD_WIDTH * .1f);

        float cellMinWidth = minWidth - content.getPadLeft() - content.getPadRight();

        Table rows = new Table();

        Label.LabelStyle style = new Label.LabelStyle(
                screen.game.fontProvider.getFont(FontProvider.h4),
                Color.BLACK
        );

        for (String sentence : screen.game.t("rate_banner.title").split("\n")) {
            Label label = new Label(sentence, style);
            label.setAlignment(Align.center);
            label.setWrap(true);

            rows.add(label)
                    .minWidth(cellMinWidth - 40)
                    .padBottom(10)
                    .row();
        }

        FitImage img = new FitImage(
                screen.assetManager.get("menu/b_rate_2.png", Texture.class)
        );

        Table columns = new Table();

        columns.add(img)
                .height(MunhauzenGame.WORLD_HEIGHT * .2f)
                .minWidth(cellMinWidth)
                .center()
                .row();

        columns.add(rows)
                .minWidth(cellMinWidth)
                .center().row();

        Table buttons = new Table();
        buttons.add(getActionBtn())
                .width(ButtonBuilder.BTN_PRIMARY_WIDTH)
                .height(ButtonBuilder.BTN_PRIMARY_HEIGHT);

        content.add(columns).row();
        content.add(buttons).row();

        return content;
    }

    private Actor getActionBtn() {

        return screen.game.buttonBuilder.danger(screen.game.t("rate_banner.btn"), new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                try {
                    fragment.fadeOut(new Runnable() {
                        @Override
                        public void run() {
                            try {

                                screen.openAdultGateBanner(new Runnable() {
                                    @Override
                                    public void run() {
                                        screen.game.params.appStore.openRateUrl();
                                    }
                                });
                            } catch (Throwable e) {
                                Log.e(tag, e);
                            }
                        }
                    });


                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }
        });

    }

}
