package ua.gram.munhauzen.screen.menu.fragment;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.screen.DebugScreen;
import ua.gram.munhauzen.screen.MenuScreen;
import ua.gram.munhauzen.ui.FixedImage;
import ua.gram.munhauzen.ui.Fragment;
import ua.gram.munhauzen.ui.FragmentRoot;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class LogoFragment extends Fragment {

    private final String tag = getClass().getSimpleName();
    private final MenuScreen screen;
    public FragmentRoot root;

    public LogoFragment(MenuScreen screen) {
        this.screen = screen;
    }

    public void create() {

        Log.i(tag, "create");

        FixedImage logo = new FixedImage(
                screen.assetManager.get("menu/menu_logo.png", Texture.class),
                MunhauzenGame.WORLD_WIDTH * .75f
        );
        if (!screen.game.params.isProduction()) {
            logo.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);

                    screen.game.stopAllAudio();

                    screen.navigateTo(new DebugScreen(screen.game));
                }
            });
        }

        Table table = new Table();
        table.pad(30, 10, 10, 10);
        table.setFillParent(true);
        table.add(logo).expand().align(Align.top).size(logo.width, logo.height);

        root = new FragmentRoot();
        root.setTouchable(Touchable.childrenOnly);
        root.addContainer(table);

        root.setName(tag);

        root.setVisible(false);

        Timer.instance().scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                fadeInFancy();
            }
        }, .4f);
    }

    public void fadeInFancy() {

        root.addAction(Actions.sequence(
                Actions.visible(false),
                Actions.moveBy(0, 20),
                Actions.alpha(0),
                Actions.parallel(
                        Actions.visible(true),
                        Actions.moveBy(0, -20, .3f),
                        Actions.alpha(1, .3f)
                )
        ));

    }

    public void fadeOutFancy() {

        root.addAction(Actions.sequence(
                Actions.visible(true),
                Actions.parallel(
                        Actions.moveBy(0, 20, .2f),
                        Actions.alpha(0, .2f)
                ),
                Actions.visible(false),
                Actions.moveBy(0, -20)
        ));

    }

    @Override
    public Actor getRoot() {
        return root;
    }

}
