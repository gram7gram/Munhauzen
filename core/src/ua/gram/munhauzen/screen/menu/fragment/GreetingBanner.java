package ua.gram.munhauzen.screen.menu.fragment;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;

import ua.gram.munhauzen.FontProvider;
import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.screen.MenuScreen;
import ua.gram.munhauzen.ui.Fragment;
import ua.gram.munhauzen.ui.FragmentRoot;
import ua.gram.munhauzen.ui.PrimaryButton;
import ua.gram.munhauzen.utils.Log;

public class GreetingBanner extends Fragment {

    final MenuScreen screen;
    FragmentRoot root;
    Table content;
    public boolean isFadeIn;
    public boolean isFadeOut;

    public GreetingBanner(MenuScreen screen) {
        this.screen = screen;
    }

    public void create() {

        String[] sentences = {
                "Приветствуем, друзья!",
                "Наша команда вложила силы и душу в даный проэкт! И да, дорогие слушатели, я не побоюсь его назвать шедевром! Да, не побоюсь!",
                "Надеемся, что наша аудиокнига внесёт в вашу жизнь много прекрасных позитивных эмоцций, и пусть на лице озарится улыбка!",
                "Слушайте и наслаждайтесь!",
        };

        content = new Table();
        content.pad(50, 100, 50, 100);

        //float width = MunhauzenGame.WORLD_WIDTH * .6f;

        content.setBackground(new SpriteDrawable(new Sprite(
                screen.assetManager.get("menu/banner_fond_1.png", Texture.class)
        )));

        Label.LabelStyle style = new Label.LabelStyle(
                screen.game.fontProvider.getFont(FontProvider.h4),
                Color.BLACK
        );

        for (String sentence : sentences) {
            Label label = new Label(sentence, style);
            label.setAlignment(Align.center);
            label.setWrap(true);

            content.add(label).padBottom(10).growX().row();
        }

        PrimaryButton btn = screen.game.buttonBuilder.primary("Гип-гип Ура!", new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                fadeOut(new Runnable() {
                    @Override
                    public void run() {
                        destroy();
                    }
                });
            }
        });

        content.add(btn)
                .width(MunhauzenGame.WORLD_WIDTH / 3f)
                .height(MunhauzenGame.WORLD_HEIGHT / 12f)
                .expandX().row();

        Container<Table> container = new Container<>(content);
        container.pad(MunhauzenGame.WORLD_WIDTH * .05f);

        root = new FragmentRoot();
        root.addContainer(container);

        root.setVisible(false);
    }

    public void update() {

    }

    public void fadeIn() {

        if (!isMounted()) return;
        if (isFadeIn) return;

        isFadeOut = false;
        isFadeIn = true;

        Log.i(tag, "fadeIn");

        root.setVisible(true);

        root.clearActions();
        root.addAction(Actions.sequence(
                Actions.alpha(0),
                Actions.alpha(1, .3f),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        isFadeIn = false;
                        isFadeOut = false;
                    }
                })
        ));
    }

    public boolean canFadeOut() {
        return isMounted() && root.isVisible() && !isFadeOut;
    }

    public void fadeOut(Runnable task) {

        if (!canFadeOut()) return;

        isFadeIn = false;
        isFadeOut = true;

        Log.i(tag, "fadeOut");

        root.clearActions();
        root.addAction(Actions.sequence(
                Actions.alpha(0, .5f),
                Actions.visible(false),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        isFadeIn = false;
                        isFadeOut = false;
                    }
                }),
                Actions.run(task)
        ));
    }

    public void fadeOut() {

        if (!canFadeOut()) return;

        isFadeIn = false;
        isFadeOut = true;

        Log.i(tag, "fadeOut");

        root.clearActions();
        root.addAction(Actions.sequence(
                Actions.alpha(0, .5f),
                Actions.visible(false),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        isFadeIn = false;
                        isFadeOut = false;
                    }
                })
        ));
    }


    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    public Actor getRoot() {
        return root;
    }
}
