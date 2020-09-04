package ua.gram.munhauzen.screen.menu.ui;

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
import ua.gram.munhauzen.screen.MenuScreen;
import ua.gram.munhauzen.screen.menu.fragment.ReferalFragment;
import ua.gram.munhauzen.ui.Banner;
import ua.gram.munhauzen.ui.PrimaryButton;
import ua.gram.munhauzen.utils.Log;

public class ReferalBanner extends Banner<MenuScreen> {

    final ReferalFragment fragment;

    public ReferalBanner(ReferalFragment fragment) {
        super(fragment.screen);

        this.fragment = fragment;
    }

    @Override
    public Texture getBackgroundTexture() {
        return screen.assetManager.get("ui/banner_fond_1.png", Texture.class);
    }

    @Override
    public Table createContent() {

        float minWidth = MunhauzenGame.WORLD_WIDTH * .9f;

        Table content = new Table();
        content.pad(20, MunhauzenGame.WORLD_WIDTH * .1f, 40, MunhauzenGame.WORLD_WIDTH * .1f);

        float cellMinWidth = minWidth - content.getPadLeft() - content.getPadRight();

        Table rows = new Table();

        Label.LabelStyle style = new Label.LabelStyle(
                screen.game.fontProvider.getFont(FontProvider.h4),
                Color.BLACK
        );

        for (String sentence : screen.game.t("referal_banner.title").split("\n")) {
            Label label = new Label(sentence, style);
            label.setAlignment(Align.center);
            label.setWrap(true);

            rows.add(label)
                    .minWidth(cellMinWidth - 40)
                    .padBottom(10)
                    .row();
        }

        Label label2 = new Label(screen.game.t("referal_banner.link")
                .replace("_NUM_", game.gameState.purchaseState.referralCount + ""), style);
        label2.setAlignment(Align.center);
        label2.setWrap(true);


        WauAnimation img = new WauAnimation(
                screen.assetManager.get("wau/wau_sheet_1x4.png", Texture.class),
                cellMinWidth * .6f
        );
        img.start();

        ReferralProgress progress = new ReferralProgress(screen, cellMinWidth * .9f);

        ReferralInput input = new ReferralInput(screen.game);
        input.setText(screen.game.referralService.getPersonalReferralLink());

        Table columns = new Table();

        columns.add(img)
                .size(img.width, img.height)
                .padBottom(10)
                .center()
                .row();

        columns.add(rows)
                .minWidth(cellMinWidth)
                .padBottom(10)
                .center()
                .row();

        columns.add(progress)
                .size(progress.width, progress.height)
                .padBottom(10)
                .row();

        columns.add(label2)
                .minWidth(cellMinWidth)
                .padBottom(10)
                .row();

        columns.add(input)
                .minWidth(cellMinWidth)
                .padBottom(10)
                .row();

        PrimaryButton button = screen.game.buttonBuilder.primary(screen.game.t("referal_banner.btn"), new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                try {

                    screen.game.referralService.copyReferralLink();

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

        Table buttons = new Table();
        buttons.add(button)
                .width(ButtonBuilder.BTN_PRIMARY_WIDTH)
                .height(ButtonBuilder.BTN_PRIMARY_HEIGHT);

        content.add(columns).row();
        content.add(buttons).row();

        return content;
    }


}
