package ua.gram.munhauzen.screen.authors.fragment;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

import ua.gram.munhauzen.FontProvider;
import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.screen.AuthorsScreen;
import ua.gram.munhauzen.screen.authors.ui.Portrait;
import ua.gram.munhauzen.screen.authors.ui.Underline;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class RuAuthorsFragment extends AuthorsFragment {

    public RuAuthorsFragment(AuthorsScreen screen) {
        super(screen);
    }

    @Override
    protected Table createBody() {

        Table rows = new Table();
        rows.align(Align.top);
        rows.pad(10, 100, 10, 100);

        float widthLimit = MunhauzenGame.WORLD_WIDTH * .65f;
        float imgWidth = MunhauzenGame.WORLD_WIDTH * .3f;

        Label.LabelStyle style = new Label.LabelStyle(
                screen.game.fontProvider.getFont(FontProvider.h4),
                Color.BLACK
        );

        Label.LabelStyle linkStyle = new Label.LabelStyle(
                screen.game.fontProvider.getFont(FontProvider.h4),
                Color.BLUE
        );

        Portrait img1 = new Portrait(
                screen.assetManager.get("authors/author_1.png", Texture.class),
                screen.game.t("authors.img_1_title"),
                style,
                imgWidth
        );
        Portrait img2 = new Portrait(
                screen.assetManager.get("authors/author_2.png", Texture.class),
                screen.game.t("authors.img_2_title"),
                style,
                imgWidth
        );
        Portrait img3 = new Portrait(
                screen.assetManager.get("authors/author_3_2.png", Texture.class),
                screen.game.t("authors.img_3_title"),
                style,
                imgWidth
        );
        Portrait img4 = new Portrait(
                screen.assetManager.get("authors/author_4_2.png", Texture.class),
                screen.game.t("authors.img_4_title"),
                style,
                imgWidth
        );
        Portrait img5 = new Portrait(
                screen.assetManager.get("authors/author_5.png", Texture.class),
                screen.game.t("authors.img_5_title"),
                style,
                imgWidth
        );
        Portrait img6 = new Portrait(
                screen.assetManager.get("authors/author_6.png", Texture.class),
                screen.game.t("authors.img_6_title"),
                style,
                imgWidth
        );

        rows.add(img1).padBottom(10).expandX().row();

        rows.add().padBottom(30).row();

        for (String sentence : screen.game.t("authors.content1").split("\n")) {

            Label label = new Label(sentence, style);
            label.setWrap(true);
            label.setAlignment(Align.center);

            rows.add(label).padBottom(10).center().width(widthLimit).row();
        }

        Label link1 = new Label(screen.game.t("authors.link1"), linkStyle);
        link1.setWrap(true);
        link1.setAlignment(Align.center);
        link1.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.net.openURI("https://www.youtube.com/channel/UC_GO6yOVtXVBIgfztM-FzBQ");
            }
        });

        rows.add(link1).center().width(widthLimit).row();
        rows.add(new Underline()).height(3).padBottom(10).center().width(link1.getWidth()).row();

        for (String sentence : screen.game.t("authors.content2").split("\n")) {

            Label label = new Label(sentence, style);
            label.setWrap(true);
            label.setAlignment(Align.center);

            rows.add(label).padBottom(10).center().width(widthLimit).row();
        }


        rows.add().padBottom(30).row();

        rows.add(img2).padBottom(10).expandX().row();

        rows.add().padBottom(30).row();



        for (String sentence : screen.game.t("authors.content3").split("\n")) {

            Label label = new Label(sentence, style);
            label.setWrap(true);
            label.setAlignment(Align.center);

            rows.add(label).padBottom(10).center().width(widthLimit).row();
        }



        rows.add().padBottom(30).row();

        rows.add(img3).padBottom(10).expandX().row();

        rows.add(img4).padBottom(10).expandX().row();

        rows.add().padBottom(30).row();



        for (String sentence : screen.game.t("authors.content4").split("\n")) {

            Label label = new Label(sentence, style);
            label.setWrap(true);
            label.setAlignment(Align.center);

            rows.add(label).padBottom(10).center().width(widthLimit).row();
        }


        rows.add().padBottom(30).row();

        rows.add(img5).padBottom(10).expandX().row();

        rows.add().padBottom(30).row();



        for (String sentence : screen.game.t("authors.content5").split("\n")) {

            Label label = new Label(sentence, style);
            label.setWrap(true);
            label.setAlignment(Align.center);

            rows.add(label).padBottom(10).center().width(widthLimit).row();
        }

        rows.add().padBottom(30).row();


        for (String sentence : screen.game.t("authors.content6").split("\n")) {

            Label label = new Label(sentence, style);
            label.setWrap(true);
            label.setAlignment(Align.center);

            rows.add(label).padBottom(10).center().width(widthLimit).row();
        }

        Label link2 = new Label(screen.game.t("authors.link2"), linkStyle);
        link2.setWrap(true);
        link2.setAlignment(Align.center);
        link2.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.net.openURI("https://www.youtube.com/channel/UCNXSfJ-9LJmNTrCu39UUxVw");
            }
        });

        rows.add(link2).center().width(widthLimit).row();
        rows.add(new Underline()).height(3).padBottom(10).center().width(link2.getWidth()).row();

        for (String sentence : screen.game.t("authors.content7").split("\n")) {

            Label label = new Label(sentence, style);
            label.setWrap(true);
            label.setAlignment(Align.center);

            rows.add(label).padBottom(10).center().width(widthLimit).row();
        }

        rows.add().padBottom(30).row();

        for (String sentence : screen.game.t("authors.content8").split("\n")) {

            Label label = new Label(sentence, style);
            label.setWrap(true);
            label.setAlignment(Align.center);

            rows.add(label).padBottom(10).center().width(widthLimit).row();
        }

        rows.add().padBottom(30).row();

        for (String sentence : screen.game.t("authors.content9").split("\n")) {

            Label label = new Label(sentence, style);
            label.setWrap(true);
            label.setAlignment(Align.center);

            rows.add(label).padBottom(10).center().width(widthLimit).row();
        }

        Label link3 = new Label(screen.game.t("authors.link3"), linkStyle);
        link3.setWrap(true);
        link3.setAlignment(Align.center);
        link3.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.net.openURI("https://t.me/workalone000");
            }
        });

        rows.add(link3).center().width(widthLimit).row();
        rows.add(new Underline()).height(3).padBottom(10).center().width(link3.getWidth()).row();

        for (String sentence : screen.game.t("authors.content10").split("\n")) {

            Label label = new Label(sentence, style);
            label.setWrap(true);
            label.setAlignment(Align.center);

            rows.add(label).padBottom(10).center().width(widthLimit).row();
        }

        rows.add().padBottom(30).row();

        for (String sentence : screen.game.t("authors.content11").split("\n")) {

            Label label = new Label(sentence, style);
            label.setWrap(true);
            label.setAlignment(Align.center);

            rows.add(label).padBottom(10).center().width(widthLimit).row();
        }

        rows.add().padBottom(30).row();

        rows.add(img6).padBottom(10).expandX().row();

        rows.add().padBottom(30).row();

        for (String sentence : screen.game.t("authors.content12").split("\n")) {

            Label label = new Label(sentence, style);
            label.setWrap(true);
            label.setAlignment(Align.center);

            rows.add(label).padBottom(10).center().width(widthLimit).row();
        }

        return rows;
    }

}