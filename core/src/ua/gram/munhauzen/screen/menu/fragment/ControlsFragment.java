package ua.gram.munhauzen.screen.menu.fragment;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.screen.MenuScreen;
import ua.gram.munhauzen.screen.menu.ui.ChronoButton;
import ua.gram.munhauzen.screen.menu.ui.ContinueButton;
import ua.gram.munhauzen.screen.menu.ui.FoolsButton;
import ua.gram.munhauzen.screen.menu.ui.GalleryButton;
import ua.gram.munhauzen.screen.menu.ui.MenuButton;
import ua.gram.munhauzen.screen.menu.ui.SavesButton;
import ua.gram.munhauzen.screen.menu.ui.StartButton;
import ua.gram.munhauzen.ui.FitImage;
import ua.gram.munhauzen.ui.Fragment;
import ua.gram.munhauzen.ui.FragmentRoot;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class ControlsFragment extends Fragment {

    private final String tag = getClass().getSimpleName();
    private final MenuScreen screen;
    public FragmentRoot root;
    public MenuButton startButton, continueButton, savesButton, galleryButton, foolsButton, chronoButton;
    Table btnTable, sideTable, logoTable;
    Container<Table> sideContainer, menuContainer, exitContainer, titleContainer;
    Timer.Task fadeTask;
    boolean isFadeIn, isFadeOut;
    Image logo;

    public ControlsFragment(MenuScreen screen) {
        this.screen = screen;
    }

    public void create() {

        Log.i(tag, "create");

        startButton = new StartButton(screen);
        continueButton = new ContinueButton(screen);
        savesButton = new SavesButton(screen);
        galleryButton = new GalleryButton(screen);
        foolsButton = new FoolsButton(screen);
        chronoButton = new ChronoButton(screen);

        logo = new FitImage();

        btnTable = new Table();
        btnTable.add(continueButton).row();
        btnTable.add(startButton).row();
        btnTable.add(savesButton).row();
        btnTable.add(galleryButton).row();
        btnTable.add(foolsButton).row();
        btnTable.add(chronoButton).row();

        ImageButton shareBtn = getShareBtn();
        ImageButton rateBtn = getRateBtn();
        ImageButton demoBtn = getDemoBtn();
        ImageButton menuBtn = getMenuBtn();
        ImageButton exitBtn = getExitBtn();

        float iconSize = MunhauzenGame.WORLD_WIDTH * .18f;
        float iconSize2 = iconSize * 1.5f;

        sideTable = new Table();
        sideTable.add(rateBtn)
                .size(iconSize)
                .padBottom(10)
                .row();
        sideTable.add(shareBtn)
                .size(iconSize)
                .padBottom(10)
                .row();
        sideTable.add(demoBtn)
                .size(iconSize)
                .padBottom(10)
                .row();

        Table menuTable = new Table();
        menuTable.add(menuBtn).expandX().left().width(iconSize2).height(iconSize2 / 2f).row();

        Table exitTable = new Table();
        exitTable.add(exitBtn).expandX().right().width(iconSize2 / 2f).height(iconSize2).row();

        logoTable = new Table();
        logoTable.add(logo).expandX().center().row();

        sideContainer = new Container<>(sideTable);
        sideContainer.align(Align.left);
        sideContainer.pad(10);

        menuContainer = new Container<>(menuTable);
        menuContainer.align(Align.bottomLeft);
        menuContainer.pad(10);

        exitContainer = new Container<>(exitTable);
        exitContainer.align(Align.bottomRight);
        exitContainer.pad(10);

        titleContainer = new Container<>(logoTable);
        titleContainer.align(Align.top);
        titleContainer.pad(10);

        VerticalGroup content = new VerticalGroup();
        content.addActor(titleContainer);
        content.addActor(btnTable);

        root = new FragmentRoot();
        root.addContainer(content);
        root.addContainer(sideContainer);
        root.addContainer(menuContainer);
        root.addContainer(exitContainer);

        root.setName(tag);

        scheduleFadeOut();

        root.setVisible(false);

        setLogoDrawable(new SpriteDrawable(new Sprite(
                screen.assetManager.get("menu/menu_logo.png", Texture.class)
        )));

        Timer.instance().scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                fadeInFancy();
            }
        }, .4f);
    }

    public void fadeInFancy() {

        if (isFadeIn) return;

        isFadeIn = true;
        isFadeOut = false;

        float delay = 0;

        titleContainer.addAction(Actions.sequence(
                Actions.visible(false),
                Actions.moveBy(0, -20),
                Actions.alpha(0),
                Actions.parallel(
                        Actions.visible(true),
                        Actions.moveBy(0, 20, .3f),
                        Actions.alpha(1, .3f)
                )
        ));

        delay += .3f;

        for (Cell cell : btnTable.getCells()) {

            cell.getActor().addAction(Actions.sequence(
                    Actions.visible(false),
                    Actions.delay(delay),
                    Actions.moveBy(0, 20),
                    Actions.alpha(0),
                    Actions.parallel(
                            Actions.visible(true),
                            Actions.moveBy(0, -20, .3f),
                            Actions.alpha(1, .3f)
                    )
            ));

            delay += .1f;
        }

        sideContainer.addAction(Actions.sequence(
                Actions.visible(false),
                Actions.moveBy(-20, 0),
                Actions.alpha(0),
                Actions.parallel(
                        Actions.visible(true),
                        Actions.moveBy(20, 0, .3f),
                        Actions.alpha(1, .3f)
                )
        ));

        menuContainer.addAction(Actions.sequence(
                Actions.visible(false),
                Actions.moveBy(0, -20),
                Actions.alpha(0),
                Actions.parallel(
                        Actions.visible(true),
                        Actions.moveBy(0, 20, .3f),
                        Actions.alpha(1, .3f)
                )
        ));

        exitContainer.addAction(Actions.sequence(
                Actions.visible(false),
                Actions.moveBy(0, -20),
                Actions.alpha(0),
                Actions.parallel(
                        Actions.visible(true),
                        Actions.moveBy(0, 20, .3f),
                        Actions.alpha(1, .3f)
                )
        ));

        root.addAction(Actions.sequence(
                Actions.visible(true),
                Actions.delay(.4f),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        isFadeIn = false;
                        isFadeOut = false;
                    }
                })
        ));

    }

    public void fadeOutFancy(Runnable task) {

    }

    public void fadeOut() {

        if (isFadeOut) return;

        isFadeIn = false;
        isFadeOut = true;

        sideContainer.addAction(Actions.sequence(
                Actions.parallel(
                        Actions.moveBy(-20, 0, .3f),
                        Actions.alpha(0, .3f)
                ),
                Actions.visible(false)
        ));

        menuContainer.addAction(Actions.sequence(
                Actions.parallel(
                        Actions.moveBy(0, -20, .3f),
                        Actions.alpha(0, .3f)
                ),
                Actions.visible(false)
        ));

        exitContainer.addAction(Actions.sequence(
                Actions.parallel(
                        Actions.moveBy(0, -20, .3f),
                        Actions.alpha(0, .3f)
                ),
                Actions.visible(false)
        ));

        titleContainer.addAction(Actions.sequence(
                Actions.parallel(
                        Actions.moveBy(0, 20, .3f),
                        Actions.alpha(0, .3f)
                ),
                Actions.visible(false)
        ));

        btnTable.addAction(Actions.sequence(
                Actions.alpha(0, .3f),
                Actions.visible(false)
        ));

        root.addAction(Actions.sequence(
                Actions.delay(.4f),
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

    public void fadeIn() {

        if (isFadeIn) return;

        isFadeIn = true;
        isFadeOut = false;

        sideContainer.addAction(Actions.sequence(
                Actions.visible(true),
                Actions.parallel(
                        Actions.moveBy(20, 0, .3f),
                        Actions.alpha(1, .3f)
                )
        ));

        menuContainer.addAction(Actions.sequence(
                Actions.visible(true),
                Actions.parallel(
                        Actions.moveBy(0, 20, .3f),
                        Actions.alpha(1, .3f)
                )
        ));

        exitContainer.addAction(Actions.sequence(
                Actions.visible(true),
                Actions.parallel(
                        Actions.moveBy(0, 20, .3f),
                        Actions.alpha(1, .3f)
                )
        ));

        titleContainer.addAction(Actions.sequence(
                Actions.visible(true),
                Actions.parallel(
                        Actions.moveBy(0, -20, .3f),
                        Actions.alpha(1, .3f)
                )
        ));

        btnTable.addAction(Actions.sequence(
                Actions.visible(true),
                Actions.alpha(1, .3f)
        ));

        root.addAction(Actions.sequence(
                Actions.visible(true),
                Actions.delay(.4f),
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

    @Override
    public void dispose() {
        super.dispose();

        cancelFadeOut();

    }

    public void scheduleFadeOut() {
        cancelFadeOut();

        fadeTask = Timer.instance().scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                fadeOut();
            }
        }, 10);
    }

    public void cancelFadeOut() {
        if (fadeTask != null) {
            fadeTask.cancel();
            fadeTask = null;
        }
    }

    public void setLogoDrawable(SpriteDrawable drawable) {

        logo.setDrawable(drawable);

        float width = MunhauzenGame.WORLD_WIDTH * .9f;
        float scale = 1f * width / drawable.getMinWidth();
        float height = 1f * drawable.getMinHeight() * scale;

        logoTable.getCell(logo)
                .width(width)
                .height(height);
    }

    private ImageButton getShareBtn() {
        Texture txt = screen.assetManager.get("menu/b_share.png", Texture.class);
        Texture txtOff = screen.assetManager.get("menu/b_share_disabled.png", Texture.class);

        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.up = new SpriteDrawable(new Sprite(txt));
        style.down = new SpriteDrawable(new Sprite(txt));
        style.disabled = new SpriteDrawable(new Sprite(txtOff));

        return new ImageButton(style);
    }

    private ImageButton getRateBtn() {
        Texture txt = screen.assetManager.get("menu/b_rate.png", Texture.class);
        Texture txtOff = screen.assetManager.get("menu/b_rate_disabled.png", Texture.class);

        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.up = new SpriteDrawable(new Sprite(txt));
        style.down = new SpriteDrawable(new Sprite(txt));
        style.disabled = new SpriteDrawable(new Sprite(txtOff));

        return new ImageButton(style);
    }

    private ImageButton getDemoBtn() {
        Texture txt = screen.assetManager.get("menu/b_demo.png", Texture.class);
        Texture txtOff = screen.assetManager.get("menu/b_demo_disabled.png", Texture.class);

        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.up = new SpriteDrawable(new Sprite(txt));
        style.down = new SpriteDrawable(new Sprite(txt));
        style.disabled = new SpriteDrawable(new Sprite(txtOff));

        return new ImageButton(style);
    }

    private ImageButton getExitBtn() {
        Texture txt = screen.assetManager.get("menu/b_exit_on.png", Texture.class);

        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.up = new SpriteDrawable(new Sprite(txt));
        style.down = new SpriteDrawable(new Sprite(txt));
        style.disabled = new SpriteDrawable(new Sprite(txt));

        return new ImageButton(style);
    }

    private ImageButton getMenuBtn() {
        Texture txt = screen.assetManager.get("menu/b_menu.png", Texture.class);

        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.up = new SpriteDrawable(new Sprite(txt));
        style.down = new SpriteDrawable(new Sprite(txt));
        style.disabled = new SpriteDrawable(new Sprite(txt));

        ImageButton btn = new ImageButton(style);

        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                cancelFadeOut();

                fadeOut();
            }
        });

        return btn;
    }
}
