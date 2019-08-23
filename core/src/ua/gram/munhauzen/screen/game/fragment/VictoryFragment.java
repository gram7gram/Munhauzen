package ua.gram.munhauzen.screen.game.fragment;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;

import ua.gram.munhauzen.FontProvider;
import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.screen.MenuScreen;
import ua.gram.munhauzen.screen.game.ui.BackgroundImage;
import ua.gram.munhauzen.ui.Fragment;
import ua.gram.munhauzen.ui.FragmentRoot;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class VictoryFragment extends Fragment {

    private final String tag = getClass().getSimpleName();
    private final GameScreen screen;
    public FragmentRoot root;

    final String IMAGE = "images/p62_fin_0.jpg";

    BackgroundImage backgroundImage;
    Image curtain;
    Label title;

    public VictoryFragment(GameScreen gameScreen) {
        this.screen = gameScreen;
    }

    public void create() {

        Log.i(tag, "create");

        screen.assetManager.load(IMAGE, Texture.class);

        screen.assetManager.finishLoading();

        screen.game.fontProvider.loadHd();

        title = new Label("The end", new Label.LabelStyle(
                screen.game.fontProvider.getFont(FontProvider.FleischmannGotich, FontProvider.hd),
                Color.BLACK
        ));
        title.setWrap(false);
        title.setAlignment(Align.center);
        title.setVisible(false);

        Pixmap px = new Pixmap(1, 1, Pixmap.Format.RGB888);
        px.setColor(Color.BLACK);
        px.fill();

        curtain = new Image(new Texture(px));
        curtain.setVisible(false);

        backgroundImage = new BackgroundImage(screen);

        Container<Label> titleContainer = new Container<>(title);
        titleContainer.pad(10);
        titleContainer.align(Align.center);

        root = new FragmentRoot();
        root.addContainer(backgroundImage);
        root.addContainer(titleContainer);
        root.addContainer(new Container<>(curtain));

        root.setName(tag);
        root.setVisible(false);

        backgroundImage.setBackgroundDrawable(new SpriteDrawable(new Sprite(
                screen.assetManager.get(IMAGE, Texture.class)
        )));
    }

    public void fadeIn() {

        root.setVisible(true);

        root.clearActions();
        root.addAction(
                Actions.sequence(
                        Actions.alpha(0),
                        Actions.alpha(1, .2f),
                        Actions.delay(.5f),
                        Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                fadeInTitle();
                            }
                        }),
                        Actions.delay(3),
                        Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                scaleBackground();
                            }
                        })

                )
        );
    }

    public void fadeInTitle() {

        title.setVisible(true);

        title.clearActions();
        title.addAction(
                Actions.sequence(
                        Actions.alpha(0),
                        Actions.alpha(1, 1f)
                )
        );
    }

    public void scaleBackground() {

        backgroundImage.setVisible(true);
        backgroundImage.setOrigin(
                backgroundImage.backgroundWidth * .5f,
                backgroundImage.backgroundHeight * .85f
        );

        backgroundImage.clearActions();
        backgroundImage.addAction(
                Actions.sequence(
                        Actions.scaleBy(1.6f, 1.6f, 2.4f),
                        Actions.parallel(
                                Actions.scaleBy(.4f, .4f, .6f),
                                Actions.run(new Runnable() {
                                    @Override
                                    public void run() {
                                        fadeInCurtain();
                                    }
                                })
                        )
                )
        );
    }

    public void fadeInCurtain() {

        curtain.setVisible(true);

        curtain.clearActions();
        curtain.addAction(
                Actions.sequence(
                        Actions.alpha(0),
                        Actions.alpha(1, .6f),
                        Actions.delay(2),
                        Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                try {

                                    screen.game.gameState.menuState.forceShowThankYouBanner = true;
                                    screen.game.gameState.menuState.isContinueEnabled = false;
                                    screen.getActiveSave().reset();

                                    screen.navigateTo(new MenuScreen(screen.game));

                                } catch (Throwable e) {
                                    Log.e(tag, e);
                                }
                            }
                        })
                )
        );
    }

    @Override
    public Actor getRoot() {
        return root;
    }

    public void update() {
        curtain.setBounds(0, 0, MunhauzenGame.WORLD_WIDTH, MunhauzenGame.WORLD_HEIGHT);
    }
}
