package ua.gram.munhauzen.screen.chapters.fragment;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;

import ua.gram.munhauzen.FontProvider;
import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.Chapter;
import ua.gram.munhauzen.screen.ChaptersScreen;
import ua.gram.munhauzen.screen.chapters.ui.ChapterRow;
import ua.gram.munhauzen.ui.Fragment;
import ua.gram.munhauzen.ui.FragmentRoot;
import ua.gram.munhauzen.ui.VerticalScrollPane;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class ChaptersFragment extends Fragment {

    private final String tag = getClass().getSimpleName();
    public final ChaptersScreen screen;
    public FragmentRoot root;
    Image top, bottom;
    Table back;
    VerticalScrollPane scroll;

    public ChaptersFragment(ChaptersScreen screen) {
        this.screen = screen;
    }

    @Override
    public Actor getRoot() {
        return root;
    }

    public void create() {

        Log.i(tag, "create");

        top = new Image();
        bottom = new Image();

        Table body = createBody();

        Table content = new Table();
        content.pad(75, 10, 10, 10);
        content.align(Align.top);
        content.add(createHeader()).top().expandX().row();
        content.add(body).top().expand().fill().row();

        body.layout();

        Texture middleTexture = screen.assetManager.get("ui/gv_paper_2.png", Texture.class);

        float middleWidth = MunhauzenGame.WORLD_WIDTH * .9f;
        float middleScale = 1f * middleWidth / middleTexture.getWidth();
        int middleHeight = (int) (1f * middleTexture.getHeight() * middleScale);

        back = new Table();
        back.pad(10, 0, 80, 0);
        back.align(Align.top);
        back.add(top).top().expandX().row();

        int repeats = Math.max(1, (int) Math.ceil(body.getPrefHeight() / middleHeight));
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

        scroll = new VerticalScrollPane(stack);

        root = new FragmentRoot();
        root.addContainer(scroll);
        root.setVisible(false);

        setTopBackground(screen.assetManager.get("ui/gv_paper_1.png", Texture.class));
        setBottomBackground(screen.assetManager.get("ui/gv_paper_3.png", Texture.class));
    }

    public void update() {
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

    private Table createBody() {

        Table rows = new Table();
        rows.align(Align.top);
        if (!screen.game.params.isTablet) {
            rows.pad(10, 100, 10, 130);
        } else {
            rows.pad(10, 180, 10, 180);
        }

        float width = MunhauzenGame.WORLD_WIDTH - 20 - rows.getPadLeft() - rows.getPadRight();

        for (Chapter chapter : screen.game.gameState.chapterRegistry) {
            rows.add(new ChapterRow(screen, chapter, width))
                    .padBottom(5)
                    .row();
        }

        return rows;
    }

    private Actor createHeader() {

        Image icon = new Image(screen.assetManager.get("saves/sv_baron.png", Texture.class));

        Label title = new Label(screen.game.t("chapter_screen.title"), new Label.LabelStyle(
                screen.game.fontProvider.getFont(FontProvider.h1),
                Color.BLACK
        ));

        Table content = new Table();
        content.add(title).padBottom(5).expandX().row();
        content.add(icon)
                .size(MunhauzenGame.WORLD_WIDTH * .25f)
                .expandX().row();

        Container<Table> container = new Container<>(content);
        container.pad(10);
        container.align(Align.bottom);

        return container;
    }
}