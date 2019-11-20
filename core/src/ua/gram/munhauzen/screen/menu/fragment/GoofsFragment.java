package ua.gram.munhauzen.screen.menu.fragment;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import ua.gram.munhauzen.screen.FailsScreen;
import ua.gram.munhauzen.screen.MenuScreen;
import ua.gram.munhauzen.screen.menu.ui.GoofsBanner;
import ua.gram.munhauzen.ui.FragmentRoot;
import ua.gram.munhauzen.utils.Log;

public class GoofsFragment extends MenuFragment {

    FragmentRoot root;
    public boolean isFadeIn;
    public boolean isFadeOut;

    public GoofsFragment(MenuScreen screen) {
        super(screen);
    }

    public void create() {
        super.create();

        screen.assetManager.load("ui/banner_fond_0.png", Texture.class);

        switch (screen.game.params.locale) {
            case "ru":
                screen.assetManager.load("authors/author_3_2.png", Texture.class);
                break;
            case "en":
                screen.assetManager.load("authors/author_3_1.png", Texture.class);
                break;
        }

        screen.assetManager.finishLoading();

        GoofsBanner banner = new GoofsBanner(this);

        Container c = new Container();
        c.setTouchable(Touchable.enabled);

        root = new FragmentRoot();
        root.addContainer(c);
        root.addContainer(banner);

        c.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                //ignore

            }
        });

        root.setVisible(false);
    }

    public void update() {

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

        screen.stopCurrentSfx();
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

    public void onOkClicked() {
        try {

            root.setTouchable(Touchable.disabled);

            screen.navigateTo(new FailsScreen(screen.game));

        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }
}
