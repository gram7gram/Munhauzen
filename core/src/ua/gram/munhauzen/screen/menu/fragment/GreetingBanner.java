package ua.gram.munhauzen.screen.menu.fragment;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
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
    public boolean isFadeIn;
    public boolean isFadeOut;

    public GreetingBanner(MenuScreen screen) {
        this.screen = screen;
    }

    public void create() {

        screen.assetManager.load("ui/banner_fond_1.png", Texture.class);

        screen.assetManager.finishLoading();

        String[] sentences = {
                "Приветствуем, друзья!",
                "Наша команда вложила силы и душу в даный проэкт! И да, дорогие слушатели, я не побоюсь его назвать шедевром! Да, не побоюсь!",
                "Надеемся, что наша аудиокнига внесёт в вашу жизнь много прекрасных позитивных эмоцций, и пусть на лице озарится улыбка!",
                "Слушайте и наслаждайтесь!",
        };

        Table content = new Table();
        content.pad(50, 100, 50, 100);

        Label.LabelStyle style = new Label.LabelStyle(
                screen.game.fontProvider.getFont(FontProvider.h4),
                Color.BLACK
        );

        for (String sentence : sentences) {
            Label label = new Label(sentence, style);
            label.setAlignment(Align.center);
            label.setWrap(true);

            content.add(label)
                    .width(MunhauzenGame.WORLD_WIDTH * .6f)
                    .padBottom(10).growX().row();
        }

        PrimaryButton btn = screen.game.buttonBuilder.primary("Hip, hip, hoorey!", new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                fadeOut(new Runnable() {
                    @Override
                    public void run() {
                        destroy();
                        screen.greetingBanner = null;
                    }
                });
            }
        });

        content.add(btn)
                .width(MunhauzenGame.WORLD_WIDTH * .4f)
                .height(MunhauzenGame.WORLD_HEIGHT / 12f)
                .expandX().row();

        content.setBackground(new SpriteDrawable(new Sprite(
                screen.assetManager.get("ui/banner_fond_1.png", Texture.class)
        )));

        Container<Table> container = new Container<>(content);
        container.pad(MunhauzenGame.WORLD_WIDTH * .05f);

        root = new FragmentRoot();
        root.addContainer(container);

        Pixmap px = new Pixmap(1, 1, Pixmap.Format.RGBA4444);
        px.setColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, .3f);
        px.fill();

        container.setBackground(new SpriteDrawable(new Sprite(new Texture(px))));

        container.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                if (event.isHandled()) return;

                Log.i(tag, "root clicked");

                fadeOut(new Runnable() {
                    @Override
                    public void run() {
                        destroy();
                        screen.greetingBanner = null;
                    }
                });
            }
        });

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
    public Actor getRoot() {
        return root;
    }
}
