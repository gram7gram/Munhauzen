package ua.gram.munhauzen.screen.menu.fragment;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import ua.gram.munhauzen.screen.MenuScreen;
import ua.gram.munhauzen.screen.menu.ui.ExitBanner;
import ua.gram.munhauzen.ui.FragmentRoot;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class ExitFragment extends MenuFragment {

    private final String tag = getClass().getSimpleName();
    public FragmentRoot root;

    public ExitFragment(MenuScreen screen) {
        super(screen);
    }

    @Override
    public Actor getRoot() {
        return root;
    }

    public void create() {
        super.create();

        screen.assetManager.load("ui/banner_fond_3.png", Texture.class);

        screen.assetManager.finishLoading();

        Log.i(tag, "create");

        ExitBanner banner = new ExitBanner(this);
        banner.create();

        Container<?> container = new Container<>();
        container.setTouchable(Touchable.enabled);

        container.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                if (event.isHandled()) return;

                Log.i(tag, "root clicked");

                fadeOut(new Runnable() {
                    @Override
                    public void run() {
                        screen.destroyBanners();
                    }
                });
            }
        });

        root = new FragmentRoot();
        root.addContainer(container);
        root.addContainer(banner);

        root.setVisible(false);
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

        screen.game.stopCurrentSfx();

        screen.game.currentSfx = screen.game.sfxService.onExitClicked();
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
}