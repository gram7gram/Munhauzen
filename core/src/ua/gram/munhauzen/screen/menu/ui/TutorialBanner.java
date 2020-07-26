package ua.gram.munhauzen.screen.menu.ui;

import com.badlogic.gdx.Gdx;
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
import ua.gram.munhauzen.screen.menu.fragment.TutorialFragment;
import ua.gram.munhauzen.ui.Banner;
import ua.gram.munhauzen.ui.FixedImage;
import ua.gram.munhauzen.utils.Log;

public class TutorialBanner extends Banner<MenuScreen> {

    final TutorialFragment fragment;

    public TutorialBanner(TutorialFragment fragment) {
        super(fragment.screen);

        this.fragment = fragment;
    }

    @Override
    public Texture getBackgroundTexture() {
        return screen.assetManager.get("ui/banner_fond_0.png", Texture.class);
    }

    @Override
    public Table createContent() {

        float minWidth = MunhauzenGame.WORLD_WIDTH * .9f;
        float pad = MunhauzenGame.WORLD_WIDTH * .1f;

        Table content = new Table();
        content.pad(20, pad, 40, pad);

        float cellMinWidth = minWidth - content.getPadLeft() - content.getPadRight();

        Table rows = new Table();

        Label.LabelStyle style = new Label.LabelStyle(
                screen.game.fontProvider.getFont(FontProvider.h4),
                Color.BLACK
        );

        Label.LabelStyle titleStyle = new Label.LabelStyle(
                screen.game.fontProvider.getFont(FontProvider.h2),
                Color.BLACK
        );

        Label title = new Label(screen.game.t("tutorial_banner.title"), titleStyle);
        title.setAlignment(Align.center);
        title.setWrap(true);

        rows.add(title)
                .minWidth(cellMinWidth - 40)
                .padBottom(10)
                .row();

        for (String sentence : screen.game.t("tutorial_banner.content").split("\n")) {
            Label label = new Label(sentence, style);
            label.setAlignment(Align.center);
            label.setWrap(true);

            rows.add(label)
                    .minWidth(cellMinWidth - 40)
                    .padBottom(10)
                    .row();
        }

        Texture txt = screen.assetManager.get("menu/tutorial.png", Texture.class);

        FixedImage img = new FixedImage(txt, cellMinWidth);
        img.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                try {

                    screen.openAdultGateBanner(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Gdx.net.openURI(screen.game.params.tutorialLink);
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

        Table columns = new Table();

        columns.add(rows)
                .minWidth(cellMinWidth)
                .center().row();

        Table buttons = new Table();

        buttons.add(getSecondaryBtn())
                .width(ButtonBuilder.BTN_PRIMARY_WIDTH)
                .height(ButtonBuilder.BTN_PRIMARY_SM_HEIGHT)
                .padBottom(10).row();

        content.add(columns).row();
        content.add(img).center().size(img.width, img.height)
                .pad(20, 0, 20, 0).row();
        content.add(buttons).row();

        return content;
    }

    private Actor getSecondaryBtn() {

        return screen.game.buttonBuilder.primary(screen.game.t("tutorial_banner.skip_btn"), new ClickListener() {
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

    }

}
