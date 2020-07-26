package ua.gram.munhauzen.screen.fails.fragment;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import ua.gram.munhauzen.screen.FailsScreen;
import ua.gram.munhauzen.screen.fails.ui.GoofsBanner;
import ua.gram.munhauzen.ui.Fragment;
import ua.gram.munhauzen.ui.FragmentRoot;
import ua.gram.munhauzen.utils.Log;

public class GoofsFragment extends Fragment {

    public final FailsScreen screen;
    FragmentRoot root;
    public boolean isFadeIn;
    public boolean isFadeOut;

    public GoofsFragment(FailsScreen screen) {
        this.screen = screen;
    }

    public void create() {

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
        banner.create();

        Container<?> c = new Container<>();
        c.setTouchable(Touchable.enabled);

        root = new FragmentRoot();
        root.addContainer(c);
        root.addContainer(banner);

        c.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                if (event.isHandled()) return;

                fadeOut(new Runnable() {
                    @Override
                    public void run() {
                        screen.destroyBanners();
                    }
                });

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

        screen.game.stopAllAudio();
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

            fadeOut(new Runnable() {
                @Override
                public void run() {
                    screen.destroyBanners();
                }
            });

        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }
}
