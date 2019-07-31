package ua.gram.munhauzen.screen.menu.fragment;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
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
import ua.gram.munhauzen.ui.FitImage;
import ua.gram.munhauzen.ui.Fragment;
import ua.gram.munhauzen.ui.FragmentRoot;
import ua.gram.munhauzen.ui.PrimaryButton;
import ua.gram.munhauzen.utils.Log;

public class RateBanner extends Fragment {

    final MenuScreen screen;
    FragmentRoot root;
    Table content;
    public boolean isFadeIn;
    public boolean isFadeOut;
    PrimaryButton btn;

    public RateBanner(MenuScreen screen) {
        this.screen = screen;
    }

    public void create() {

        screen.assetManager.load("menu/banner_fond_1.png", Texture.class);
        screen.assetManager.load("menu/b_rate_2.png", Texture.class);

        screen.assetManager.finishLoading();

        String[] sentences = {
                "Please, rate out application or leave a positive review",
        };

        content = new Table();

        Label.LabelStyle style = new Label.LabelStyle(
                screen.game.fontProvider.getFont(FontProvider.h4),
                Color.BLACK
        );

        for (String sentence : sentences) {
            Label label = new Label(sentence, style);
            label.setAlignment(Align.center);
            label.setWrap(true);

            content.add(label)
                    .width(MunhauzenGame.WORLD_WIDTH / 3f)
                    .padBottom(10).growX().row();
        }

        btn = screen.game.buttonBuilder.primary("Rate", new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                screen.game.params.appStore.openRateUrl();

                fadeOut(new Runnable() {
                    @Override
                    public void run() {
                        destroy();
                        screen.rateBanner = null;
                    }
                });
            }
        });

        content.add(btn)
                .width(MunhauzenGame.WORLD_WIDTH / 3f)
                .height(MunhauzenGame.WORLD_HEIGHT / 12f)
                .expandX().row();

        FitImage img = new FitImage(
                screen.assetManager.get("menu/b_rate_2.png", Texture.class)
        );

        Table columns = new Table();
        columns.pad(50, 100, 50, 100);
        columns.add(content).center().expandX();
        columns.add(img).width(MunhauzenGame.WORLD_WIDTH * .3f).center().expandX();

        columns.setBackground(new SpriteDrawable(new Sprite(
                screen.assetManager.get("menu/banner_fond_1.png", Texture.class)
        )));

        Container<Table> container = new Container<>(columns);
        container.pad(MunhauzenGame.WORLD_WIDTH * .05f);
        container.setTouchable(Touchable.enabled);

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
                        screen.rateBanner = null;
                    }
                });
            }
        });

        root.setVisible(false);
    }

    public void update() {
        btn.setDisabled(screen.game.params.appStore == null);
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
