package ua.gram.munhauzen.screen.menu.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;

import ua.gram.munhauzen.FontProvider;
import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.screen.menu.fragment.ShareFragment;
import ua.gram.munhauzen.ui.FitImage;

public class ShareBanner extends Banner {

    final ShareFragment fragment;

    public ShareBanner(ShareFragment fragment) {
        super(fragment.screen);

        this.fragment = fragment;
    }

    @Override
    Texture getBackgroundTexture() {
        return screen.assetManager.get("ui/banner_fond_1.png", Texture.class);
    }

    @Override
    Table createContent() {

        String[] sentences = {
                "Tell your friends about audio-book",
        };

        float minWidth = MunhauzenGame.WORLD_WIDTH * .9f;

        Table content = new Table();
        content.pad(20, 70, 20, 70);

        float cellMinWidth = minWidth - content.getPadLeft() - content.getPadRight();

        Table rows = new Table();

        Label.LabelStyle style = new Label.LabelStyle(
                screen.game.fontProvider.getFont(FontProvider.h4),
                Color.BLACK
        );

        for (String sentence : sentences) {
            Label label = new Label(sentence, style);
            label.setAlignment(Align.center);
            label.setWrap(true);

            rows.add(label)
                    .minWidth(cellMinWidth - 40)
                    .padBottom(10)
                    .row();
        }

        FitImage img = new FitImage(
                screen.assetManager.get("menu/b_share_2.png", Texture.class)
        );

        Table columns = new Table();

        columns.add(img)
                .height(MunhauzenGame.WORLD_HEIGHT * .2f)
                .minWidth(cellMinWidth)
                .center()
                .row();

        columns.add(rows)
                .minWidth(cellMinWidth)
                .center()
                .row();

        Table buttons = new Table();
        buttons.add(getFbBtn())
                .padBottom(20).padRight(10)
                .width(cellMinWidth / 2f - 10);
        buttons.add(getInstaBtn())
                .padBottom(20).padLeft(10)
                .width(cellMinWidth / 2f - 10)
                .row();

        buttons.add(getVkBtn())
                .padRight(10)
                .width(cellMinWidth / 2f - 10);
        buttons.add(getTwBtn())
                .padLeft(10)
                .width(cellMinWidth / 2f - 10).row();

        content.add(columns).row();
        content.add(buttons).padTop(20).padBottom(20).row();

        return content;
    }

    private Actor getFbBtn() {
        Texture txt = screen.assetManager.get("menu/fb_icon.jpg", Texture.class);

        Label label = new Label("via Facebook", new Label.LabelStyle(
                screen.game.fontProvider.getFont(FontProvider.h5),
                Color.WHITE
        ));

        FitImage img = new FitImage(txt);

        Table table = new Table();
        table.add(img).width(80).height(80);
        table.add(label).padLeft(5).padRight(5).expandX();

        Pixmap px = new Pixmap(1, 1, Pixmap.Format.RGB888);
        px.setColor(.235f, .341f, .62f, 1);
        px.fill();

        table.setBackground(new SpriteDrawable(new Sprite(new Texture(px))));

        table.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                fragment.onBtnCLicked("https://www.facebook.com/photo.php?fbid=233858484056409&set=gm.253300848748389&type=3&theater&ifg=1");

            }
        });

        return table;

    }

    private Actor getTwBtn() {
        Texture txt = screen.assetManager.get("menu/twitter_icon.jpg", Texture.class);

        Label label = new Label("via Twitter", new Label.LabelStyle(
                screen.game.fontProvider.getFont(FontProvider.h5),
                Color.WHITE
        ));

        FitImage img = new FitImage(txt);

        Table table = new Table();
        table.add(img).width(80).height(80);
        table.add(label).padLeft(5).padRight(5).expandX();

        Pixmap px = new Pixmap(1, 1, Pixmap.Format.RGB888);
        px.setColor(.337f, .557f, .788f, 1);
        px.fill();

        table.setBackground(new SpriteDrawable(new Sprite(new Texture(px))));

        table.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                fragment.onBtnCLicked("https://twitter.com/Finger_Tips_C/status/1005920810295611393");
            }
        });

        return table;
    }

    private Actor getVkBtn() {
        Texture txt = screen.assetManager.get("menu/vk_icon.jpg", Texture.class);

        Label label = new Label("via Vkontakte", new Label.LabelStyle(
                screen.game.fontProvider.getFont(FontProvider.h5),
                Color.WHITE
        ));

        FitImage img = new FitImage(txt);

        Table table = new Table();
        table.add(img).width(80).height(80);
        table.add(label).padLeft(5).padRight(5).expandX();

        Pixmap px = new Pixmap(1, 1, Pixmap.Format.RGB888);
        px.setColor(.322f, .506f, .725f, 1);
        px.fill();

        table.setBackground(new SpriteDrawable(new Sprite(new Texture(px))));

        table.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                fragment.onBtnCLicked("https://vk.com/fingertipsandcompany?z=photo491072996_456239025%2Fb76a7cd5942e3325a8");

            }
        });

        return table;
    }

    private Actor getInstaBtn() {
        Texture txt = screen.assetManager.get("menu/instagram_icon.jpg", Texture.class);

        Label label = new Label("via Instagram", new Label.LabelStyle(
                screen.game.fontProvider.getFont(FontProvider.h5),
                Color.WHITE
        ));

        FitImage img = new FitImage(txt);

        Table table = new Table();
        table.add(img).width(80).height(80);
        table.add(label).padLeft(5).padRight(5).expandX();

        Pixmap px = new Pixmap(1, 1, Pixmap.Format.RGB888);
        px.setColor(.31f, .498f, .655f, 1);
        px.fill();

        table.setBackground(new SpriteDrawable(new Sprite(new Texture(px))));

        table.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                fragment.onBtnCLicked("https://www.instagram.com/p/Bj2-Y58gPRR/");

            }
        });

        return table;
    }

}
