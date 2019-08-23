package ua.gram.munhauzen.screen.menu.fragment;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
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
import ua.gram.munhauzen.screen.menu.ui.ContinueButton;
import ua.gram.munhauzen.screen.menu.ui.DemoSideButton;
import ua.gram.munhauzen.screen.menu.ui.GalleryButton;
import ua.gram.munhauzen.screen.menu.ui.GoofsButton;
import ua.gram.munhauzen.screen.menu.ui.MenuButton;
import ua.gram.munhauzen.screen.menu.ui.ProSideButton;
import ua.gram.munhauzen.screen.menu.ui.RateSideButton;
import ua.gram.munhauzen.screen.menu.ui.SavesButton;
import ua.gram.munhauzen.screen.menu.ui.ShareSideButton;
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
    public MenuButton startButton, continueButton, savesButton, galleryButton, goofsButton;
    Table btnTable, sideTable, logoTable;
    Container<Table> sideContainer, exitContainer, titleContainer;
    Timer.Task fadeTask;
    public boolean isFadeIn, isFadeOut, isVisible;
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
        goofsButton = new GoofsButton(screen);

        logo = new FitImage();

        btnTable = new Table();

        if (screen.game.gameState.menuState.isContinueEnabled) {
            btnTable.add(continueButton).row();
        }

        btnTable.add(startButton).row();
        btnTable.add(savesButton).row();
        btnTable.add(galleryButton).row();
        btnTable.add(goofsButton).row();

        ImageButton exitBtn = getExitBtn();

        ShareSideButton shareBtnAnimation = new ShareSideButton(screen);
        shareBtnAnimation.start();

        RateSideButton rateBtnAnimation = new RateSideButton(screen);
        rateBtnAnimation.start();

        DemoSideButton demoBtnAnimation = new DemoSideButton(screen);
        demoBtnAnimation.start();

        ProSideButton proBtnAnimation = new ProSideButton(screen);
        proBtnAnimation.start();

        float iconSize = MunhauzenGame.WORLD_WIDTH * .18f;
        float iconSize2 = iconSize * 1.5f;

        sideTable = new Table();
        sideTable.add(rateBtnAnimation)
                .size(iconSize)
                .pad(10)
                .row();

        sideTable.add(shareBtnAnimation)
                .size(iconSize)
                .pad(10)
                .row();

        sideTable.add(demoBtnAnimation)
                .size(iconSize)
                .pad(10)
                .row();

        sideTable.add(proBtnAnimation)
                .size(iconSize)
                .pad(10)
                .row();

        Table exitTable = new Table();
        exitTable.add(exitBtn).expandX().right().width(iconSize2 / 2f).height(iconSize2).row();

        logoTable = new Table();
        logoTable.setTouchable(Touchable.disabled);
        logoTable.add(logo).expandX().center().row();

        sideContainer = new Container<>(sideTable);
        sideContainer.setTouchable(Touchable.childrenOnly);
        sideContainer.align(Align.bottomLeft);
        sideContainer.pad(10);

        exitContainer = new Container<>(exitTable);
        exitContainer.setTouchable(Touchable.childrenOnly);
        exitContainer.align(Align.bottomRight);
        exitContainer.pad(10);

        titleContainer = new Container<>(logoTable);
        titleContainer.setTouchable(Touchable.childrenOnly);
        titleContainer.align(Align.top);
        titleContainer.pad(10);

        VerticalGroup content = new VerticalGroup();
        content.setTouchable(Touchable.childrenOnly);
        content.addActor(titleContainer);
        content.addActor(btnTable);

        root = new FragmentRoot();
        root.setTouchable(Touchable.childrenOnly);
        root.addContainer(content);
        root.addContainer(sideContainer);
        root.addContainer(exitContainer);

        root.setName(tag);

        scheduleFadeOut();

        root.setVisible(false);
        isVisible = false;

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

        isVisible = false;
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
                        isVisible = true;
                    }
                })
        ));

    }

    public void fadeOut() {

        if (isFadeOut) return;

        isVisible = true;
        isFadeIn = false;
        isFadeOut = true;

        sideContainer.addAction(Actions.sequence(
                Actions.parallel(
                        Actions.moveBy(-20, 0, .3f),
                        Actions.alpha(0, .3f)
                ),
                Actions.moveBy(20, 0),
                Actions.visible(false)
        ));

        exitContainer.addAction(Actions.sequence(
                Actions.parallel(
                        Actions.moveBy(0, -20, .3f),
                        Actions.alpha(0, .3f)
                ),
                Actions.moveBy(0, 20),
                Actions.visible(false)
        ));

        btnTable.addAction(Actions.sequence(
                Actions.alpha(0, .3f),
                Actions.visible(false),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        isVisible = false;
                        isFadeIn = false;
                        isFadeOut = false;
                    }
                })
        ));
    }

    public void fadeIn() {

        if (isFadeIn) return;

        isVisible = false;
        isFadeIn = true;
        isFadeOut = false;

        sideContainer.addAction(Actions.sequence(
                Actions.moveBy(-20, 0),
                Actions.visible(true),
                Actions.parallel(
                        Actions.moveBy(20, 0, .3f),
                        Actions.alpha(1, .3f)
                )
        ));

        exitContainer.addAction(Actions.sequence(
                Actions.moveBy(0, -20),
                Actions.visible(true),
                Actions.parallel(
                        Actions.moveBy(0, 20, .3f),
                        Actions.alpha(1, .3f)
                )
        ));

        btnTable.addAction(Actions.sequence(
                Actions.visible(true),
                Actions.alpha(1, .3f),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        isVisible = true;
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

    private ImageButton getExitBtn() {
        Texture txt = screen.assetManager.get("menu/b_exit_on.png", Texture.class);

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

                    screen.exitDialog = new ExitDialog(screen);
                    screen.exitDialog.create();

                    screen.layers.setBannerLayer(screen.exitDialog);

                    screen.exitDialog.fadeIn();

                } catch (Throwable e) {
                    Log.e(tag, e);

                    screen.onCriticalError(e);
                }
            }
        });

        return btn;
    }
}
