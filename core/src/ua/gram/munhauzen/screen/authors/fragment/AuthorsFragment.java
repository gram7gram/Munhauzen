package ua.gram.munhauzen.screen.authors.fragment;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;

import ua.gram.munhauzen.FontProvider;
import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.screen.AuthorsScreen;
import ua.gram.munhauzen.ui.Fragment;
import ua.gram.munhauzen.ui.FragmentRoot;
import ua.gram.munhauzen.ui.VerticalScrollPane;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public abstract class AuthorsFragment extends Fragment {

    public final String tag = getClass().getSimpleName();
    public final AuthorsScreen screen;
    public FragmentRoot root;
    Image top, bottom;
    Table back;

    public AuthorsFragment(AuthorsScreen screen) {
        this.screen = screen;
    }

    protected abstract Table createBody();

    @Override
    public Actor getRoot() {
        return root;
    }

    public void create() {

        Log.i(tag, "create");

        top = new Image();
        bottom = new Image();

        Table body = createBody();
        Table footer1 = createRateRow();
        Table footer2 = createShareRow();
        Table footer3 = createVersionRow();

        Table content = new Table();
        content.pad(75, 10, 10, 10);
        content.align(Align.top);
        content.add(createHeader()).top().expandX().row();
        content.add(body).top().row();
        content.add(footer1).top().row();
        content.add(footer2).top().row();
        content.add(footer3).top().row();

        body.layout();
        footer1.layout();
        footer2.layout();
        footer3.layout();

        Texture middleTexture = screen.assetManager.get("ui/gv_paper_2.png", Texture.class);

        float middleWidth = MunhauzenGame.WORLD_WIDTH * .9f;
        float middleScale = 1f * middleWidth / middleTexture.getWidth();
        int middleHeight = (int) (1f * middleTexture.getHeight() * middleScale);

        back = new Table();
        back.pad(10, 0, 80, 0);
        back.align(Align.top);
        back.add(top).top().expandX().row();

        float bodyHeight = body.getPrefHeight() + footer1.getPrefHeight() + footer2.getPrefHeight() + footer3.getPrefHeight();

        int repeats = Math.max(1, (int) Math.ceil(bodyHeight / middleHeight));
        for (int i = 0; i < repeats; i++) {
            back.add(new Image(middleTexture))
                    .top()
                    .width(middleWidth)
                    .height(middleHeight)
                    .row();
        }

        back.add(bottom).top().expandX().row();

        Stack stack = new Stack();
        stack.add(back);
        stack.add(content);

        VerticalScrollPane scroll = new VerticalScrollPane(stack);

        root = new FragmentRoot();
        root.addContainer(scroll);

        setTopBackground(screen.assetManager.get("ui/gv_paper_1.png", Texture.class));
        setBottomBackground(screen.assetManager.get("ui/gv_paper_3.png", Texture.class));
    }

    public void setTopBackground(Texture texture) {

        top.setDrawable(new SpriteDrawable(new Sprite(texture)));

        float width = MunhauzenGame.WORLD_WIDTH * .9f;
        float scale = 1f * width / top.getDrawable().getMinWidth();
        int height = (int) (1f * top.getDrawable().getMinHeight() * scale);

        back.getCell(top)
                .width(width)
                .height(height);
    }

    public void setBottomBackground(Texture texture) {

        bottom.setDrawable(new SpriteDrawable(new Sprite(texture)));

        float width = MunhauzenGame.WORLD_WIDTH * .9f;
        float scale = 1f * width / bottom.getDrawable().getMinWidth();
        int height = (int) (1f * bottom.getDrawable().getMinHeight() * scale);

        back.getCell(bottom)
                .width(width)
                .height(height);
    }

    public void fadeIn() {
        root.clearActions();

        root.setVisible(true);
        root.addAction(Actions.sequence(
                Actions.alpha(0),
                Actions.moveBy(0, 20),
                Actions.parallel(
                        Actions.alpha(1, .3f),
                        Actions.moveBy(0, -20, .3f)
                )
        ));
    }

    public void fadeOut(Runnable task) {

        root.clearActions();

        root.addAction(Actions.sequence(
                Actions.parallel(
                        Actions.alpha(0, .3f),
                        Actions.moveBy(0, 20, .3f)
                ),
                Actions.visible(false),
                Actions.run(task)
        ));
    }

    public void update() {

    }

    private Table createShareRow() {

        Table rows = new Table();
        rows.align(Align.top);
        rows.pad(10, 100, 10, 100);

        Image icon = new Image(
                screen.assetManager.get("menu/b_share_2.png", Texture.class)
        );

        float widthLimit = MunhauzenGame.WORLD_WIDTH - 20 - rows.getPadLeft() - rows.getPadRight();

        float width = MunhauzenGame.WORLD_WIDTH * .3f;
        float scale = 1f * width / icon.getDrawable().getMinWidth();
        float height = icon.getDrawable().getMinHeight() * scale;

        Label label = new Label(screen.game.t("authors.share_title"), new Label.LabelStyle(
                screen.game.fontProvider.getFont(FontProvider.h3),
                Color.BLACK
        ));
        label.setWrap(true);
        label.setAlignment(Align.center);

        rows.add(label).width(widthLimit - width - 5).center();
        rows.add(icon).pad(5).width(width).height(height).center().row();


        rows.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                try {

                    screen.openShareBanner();

                } catch (Throwable e) {
                    Log.e(tag, e);

                    screen.onCriticalError(e);
                }

            }
        });
        return rows;
    }

    private Table createRateRow() {

        Table rows = new Table();
        rows.align(Align.top);
        rows.pad(10, 100, 10, 100);

        Image icon = new Image(
                screen.assetManager.get("menu/b_rate_2.png", Texture.class)
        );

        float widthLimit = MunhauzenGame.WORLD_WIDTH - 20 - rows.getPadLeft() - rows.getPadRight();

        float width = MunhauzenGame.WORLD_WIDTH * .3f;
        float scale = 1f * width / icon.getDrawable().getMinWidth();
        float height = icon.getDrawable().getMinHeight() * scale;

        Label label = new Label(screen.game.t("authors.rate_title"), new Label.LabelStyle(
                screen.game.fontProvider.getFont(FontProvider.h3),
                Color.BLACK
        ));
        label.setWrap(true);
        label.setAlignment(Align.center);

        rows.add(icon).pad(5).width(width).height(height).center();
        rows.add(label).width(widthLimit - width - 5).center().row();

        rows.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                try {

                    screen.openRateBanner();

                } catch (Throwable e) {
                    Log.e(tag, e);

                    screen.onCriticalError(e);
                }

            }
        });

        return rows;
    }

    private Table createVersionRow() {

        Table rows = new Table();
        rows.align(Align.top);
        rows.pad(10, 100, 10, 100);

        Image icon = new Image(
                screen.assetManager.get(screen.game.gameState.purchaseState.isPro
                        ? "menu/b_full_version_2.png"
                        : "menu/b_demo_version_2.png", Texture.class)
        );

        float widthLimit = MunhauzenGame.WORLD_WIDTH - 20 - rows.getPadLeft() - rows.getPadRight();

        float width = MunhauzenGame.WORLD_WIDTH * .3f;
        float scale = 1f * width / icon.getDrawable().getMinWidth();
        float height = icon.getDrawable().getMinHeight() * scale;

        String text = screen.game.gameState.purchaseState.isPro
                ? screen.game.t("authors.pro_title")
                : screen.game.t("authors.demo_title");

        Label label = new Label(text, new Label.LabelStyle(
                screen.game.fontProvider.getFont(FontProvider.h3),
                Color.BLACK
        ));
        label.setWrap(true);
        label.setAlignment(Align.center);

        rows.add(icon).pad(5).width(width).height(height).center();
        rows.add(label).width(widthLimit - width - 5).center().row();

        rows.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                try {

                    screen.openVersionBanner();

                } catch (Throwable e) {
                    Log.e(tag, e);

                    screen.onCriticalError(e);
                }

            }
        });

        return rows;
    }

    private Actor createHeader() {

        Label title = new Label(screen.game.t("authors.title"), new Label.LabelStyle(
                screen.game.fontProvider.getFont(FontProvider.h1),
                Color.BLACK
        ));

        Table content = new Table();
        content.add(title).padBottom(5).expandX().row();

        Container<Table> container = new Container<>(content);
        container.pad(10);
        container.align(Align.bottom);

        return container;
    }
}