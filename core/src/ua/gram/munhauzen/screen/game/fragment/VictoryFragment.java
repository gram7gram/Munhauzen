package ua.gram.munhauzen.screen.game.fragment;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;

import ua.gram.munhauzen.FontProvider;
import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.entity.Save;
import ua.gram.munhauzen.entity.StoryAudio;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.screen.MenuScreen;
import ua.gram.munhauzen.screen.game.ui.BackgroundImage;
import ua.gram.munhauzen.screen.game.ui.VictoryGroup;
import ua.gram.munhauzen.service.StoryManager;
import ua.gram.munhauzen.ui.Fragment;
import ua.gram.munhauzen.ui.FragmentRoot;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class VictoryFragment extends Fragment {

    private final String tag = getClass().getSimpleName();
    public final GameScreen screen;
    public FragmentRoot root;

    final String IMAGE = "images/p62_fin.jpg";

    public BackgroundImage backgroundImage;
    Label title1, title2;
    StoryAudio intro;
    Container<Table> menuContainer;
    VictoryGroup victoryGroup;
    Image black;

    public VictoryFragment(GameScreen gameScreen) {
        this.screen = gameScreen;
    }

    public void create() {

        Log.i(tag, "create");

        screen.assetManager.load(IMAGE, Texture.class);
        screen.assetManager.load("menu/b_menu.png", Texture.class);

        screen.assetManager.finishLoading();

        screen.game.fontProvider.loadHd();

        title1 = new Label(screen.game.t("ending.part1"), new Label.LabelStyle(
                screen.game.fontProvider.getHdFont(FontProvider.CalligraphModern, FontProvider.hd),
                Color.WHITE
        ));
        title1.setWrap(false);
        title1.setAlignment(Align.center);
        title1.setVisible(false);

        title2 = new Label(screen.game.t("ending.part2"), new Label.LabelStyle(
                screen.game.fontProvider.getHdFont(FontProvider.CalligraphModern, FontProvider.hd),
                Color.WHITE
        ));
        title2.setWrap(false);
        title2.setAlignment(Align.center);
        title2.setVisible(false);

        Label menuLbl = new Label(screen.game.t("ending.menu_btn"), new Label.LabelStyle(
                screen.game.fontProvider.getFont(FontProvider.h4),
                Color.WHITE
        ));
        menuLbl.setWrap(false);
        menuLbl.setAlignment(Align.center);

        Pixmap px = new Pixmap(1, 1, Pixmap.Format.RGB888);
        px.setColor(Color.BLACK);
        px.fill();

        black = new Image(new Texture(px));
        black.setVisible(false);

        backgroundImage = new BackgroundImage(screen);

        victoryGroup = new VictoryGroup(this);
        victoryGroup.setVisible(false);

        HorizontalGroup titleGroup = new HorizontalGroup();
        titleGroup.addActor(title1);
        titleGroup.addActor(title2);

        Container<HorizontalGroup> titleContainer = new Container<>(titleGroup);
        titleContainer.pad(10);
        titleContainer.align(Align.center);

        ImageButton menuBtn = getMenuBtn();

        Table menuTable = new Table();
        menuTable.add(menuLbl).center().expandX().row();
        menuTable.add(menuBtn).center()
                .width(MunhauzenGame.WORLD_WIDTH * .2f)
                .height(MunhauzenGame.WORLD_WIDTH * .12f)
                .row();

        menuContainer = new Container<>(menuTable);
        menuContainer.pad(10);
        menuContainer.padBottom(MunhauzenGame.WORLD_HEIGHT * .1f);
        menuContainer.align(Align.bottom);
        menuContainer.setVisible(false);

        root = new FragmentRoot();
        root.addContainer(backgroundImage);
        root.addContainer(victoryGroup);
        root.addContainer(new Container<>(black));
        root.addContainer(titleContainer);
        root.addContainer(menuContainer);

        root.setName(tag);
        root.setVisible(false);

        backgroundImage.setBackgroundDrawable(new SpriteDrawable(new Sprite(
                screen.assetManager.get(IMAGE, Texture.class)
        )));

        playIntro();
    }

    private void playIntro() {
        try {
            intro = new StoryAudio();
            intro.audio = "sthe_end";

            screen.audioService.prepareAndPlay(intro);

        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }

    public void fadeIn() {

        root.setVisible(true);

        root.clearActions();
        root.addAction(
                Actions.sequence(
                        Actions.alpha(0),
                        Actions.alpha(1, .2f),
                        Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                scaleBackground();
                            }
                        })
                )
        );

        Timer.instance().scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                fadeInCurtain();
            }
        }, 3.45f);

        Timer.instance().scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                fadeInTitle();
            }
        }, 11);

        Timer.instance().scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                fadeInMenu();
            }
        }, 13.5f);
    }

    public void fadeInTitle() {

        title1.setVisible(true);
        title2.setVisible(false);

        title1.clearActions();
        title2.clearActions();

        title1.addAction(
                Actions.sequence(
                        Actions.alpha(0),
                        Actions.alpha(1, .5f),
                        Actions.delay(.2f),
                        Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                title2.setVisible(true);

                                title2.addAction(
                                        Actions.sequence(
                                                Actions.alpha(0),
                                                Actions.alpha(1, .5f)
                                        )
                                );
                            }
                        })
                )
        );
    }

    public void scaleBackground() {

        backgroundImage.setVisible(true);
        backgroundImage.setOrigin(
                backgroundImage.backgroundWidth * .5f,
                backgroundImage.backgroundHeight * .91f
        );

        backgroundImage.clearActions();
        backgroundImage.addAction(
                Actions.sequence(
                        Actions.scaleBy(.55f, .55f, 4.5f)
                )
        );
    }

    public void fadeInCurtain() {

        victoryGroup.setVisible(true);

        victoryGroup.victoryCircle.start();

        Timer.instance().scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                victoryGroup.victoryCircle.dispose();
                victoryGroup.remove();

                black.setVisible(true);

            }
        }, victoryGroup.victoryCircle.getTotalDuration());
    }

    public void fadeInMenu() {

        menuContainer.setVisible(true);

        menuContainer.clearActions();
        menuContainer.addAction(
                Actions.sequence(
                        Actions.alpha(0),
                        Actions.alpha(1, .3f)
                )
        );
    }

    private void onComplete() {

        GameState state = screen.game.gameState;

        try {
            state.menuState.showThankYouBanner = true;
            state.menuState.isContinueEnabled = false;

            StoryManager storyManager = new StoryManager(null, screen.game.gameState);

            Save save = new Save();

            save.story = storyManager.getDefaultStory();
            save.story.init();

            state.activeSave = save;

        } catch (Throwable e) {
            Log.e(tag, e);
        }

        try {
            screen.game.databaseManager.persistSync(state);
        } catch (Throwable e) {
            Log.e(tag, e);
        }

        try {
            screen.navigateTo(new MenuScreen(screen.game));
        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }

    @Override
    public Actor getRoot() {
        return root;
    }

    public void update() {

        black.setBounds(0, 0, MunhauzenGame.WORLD_WIDTH, MunhauzenGame.WORLD_HEIGHT);

        if (intro != null) {
            screen.audioService.updateVolume(intro);
        }
    }

    @Override
    public void dispose() {
        super.dispose();

        if (intro != null) {
            screen.audioService.stop(intro);
            intro = null;
        }
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

                screen.game.sfxService.onAnyBtnClicked();

                onComplete();
            }
        });

        return btn;
    }
}
