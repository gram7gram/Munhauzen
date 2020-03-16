package ua.gram.munhauzen.screen.fails.fragment;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;

import ua.gram.munhauzen.FontProvider;
import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.FailsState;
import ua.gram.munhauzen.entity.StoryAudio;
import ua.gram.munhauzen.screen.FailsScreen;
import ua.gram.munhauzen.screen.fails.entity.GalleryFail;
import ua.gram.munhauzen.screen.fails.ui.AudioRow;
import ua.gram.munhauzen.ui.Fragment;
import ua.gram.munhauzen.ui.FragmentRoot;
import ua.gram.munhauzen.ui.VerticalScrollPane;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class FailsFragment extends Fragment {

    private final String tag = getClass().getSimpleName();
    public final FailsScreen screen;
    public FragmentRoot root;
    Image top, bottom;
    Table back;
    ImageButton failSourceBtn;
    StoryAudio sfx;
    public ArrayList<AudioRow> audioRows;

    public FailsFragment(FailsScreen screen) {
        this.screen = screen;
    }

    @Override
    public Actor getRoot() {
        return root;
    }

    public void create() {

        Log.i(tag, "create");

        audioRows = new ArrayList<>();

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

        VerticalScrollPane scroll = new VerticalScrollPane(stack);

        root = new FragmentRoot();
        root.addContainer(scroll);
        root.setVisible(false);

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

    private Table createBody() {

        Table rows = new Table();
        rows.align(Align.top);
        if (!screen.game.params.isTablet) {
            rows.pad(10, 100, 10, 130);
        } else {
            rows.pad(10, 180, 10, 180);
        }

        FailsState state = screen.game.gameState.failsState;

        ArrayList<GalleryFail> fails = state.isMunhauzen ? screen.failsM : screen.failsD;
        int num = 0;

        float width = MunhauzenGame.WORLD_WIDTH - 20 - rows.getPadLeft() - rows.getPadRight();

        audioRows.clear();

        for (GalleryFail fail : fails) {

            AudioRow row = new AudioRow(screen, fail, ++num, width);

            audioRows.add(row);

            rows.add(row)
                    .padBottom(5)
                    .row();
        }

        return rows;
    }

    private Actor createHeader() {

        failSourceBtn = getFailSourceBtn();

        Texture txt = screen.assetManager.get("fails/fv_switch_m.png", Texture.class);

        Label title = new Label(screen.game.t("fails.title"), new Label.LabelStyle(
                screen.game.fontProvider.getFont(FontProvider.h1),
                Color.BLACK
        ));

        float width = MunhauzenGame.WORLD_WIDTH * .25f;
        float scale = 1f * width / txt.getWidth();
        float height = 1f * txt.getHeight() * scale;

        Table content = new Table();
        content.add(title).padBottom(5).expandX().row();
        content.add(failSourceBtn)
                .size(width, height)
                .expandX().row();

        Container<Table> container = new Container<>(content);
        container.pad(10);
        container.align(Align.bottom);

        return container;
    }

    private ImageButton getFailSourceBtn() {

        final FailsState state = screen.game.gameState.failsState;

        Texture txt = state.isMunhauzen
                ? screen.assetManager.get("fails/fv_switch_m.png", Texture.class)
                : screen.assetManager.get("fails/fv_switch_d.png", Texture.class);

        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.up = new SpriteDrawable(new Sprite(txt));
        style.down = new SpriteDrawable(new Sprite(txt));
        style.disabled = new SpriteDrawable(new Sprite(txt));

        ImageButton btn = new ImageButton(style);

        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                try {

                    screen.stopAll();

                    screen.game.sfxService.onAnyBtnClicked();

                    state.isMunhauzen = !state.isMunhauzen;

                    if (state.isMunhauzen) {
                        screen.game.sfxService.onGoofsSwitchClickedForMunhauzen();
                    } else {
                        screen.game.sfxService.onGoofsSwitchClickedForDaughter();
                    }

                    screen.failsFragment.destroy();

                    screen.failsFragment = new FailsFragment(screen);
                    screen.failsFragment.create();

                    screen.layers.setContentLayer(screen.failsFragment);

                    screen.failsFragment.root.setVisible(true);
                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }
        });

        return btn;
    }

    @Override
    public void dispose() {
        super.dispose();

        if (sfx != null) {
            screen.game.sfxService.dispose(sfx);
            sfx = null;
        }
    }
}