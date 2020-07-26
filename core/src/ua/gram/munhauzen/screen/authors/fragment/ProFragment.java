package ua.gram.munhauzen.screen.authors.fragment;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import ua.gram.munhauzen.screen.AuthorsScreen;
import ua.gram.munhauzen.screen.authors.ui.ProBanner;
import ua.gram.munhauzen.ui.Fragment;
import ua.gram.munhauzen.ui.FragmentRoot;
import ua.gram.munhauzen.utils.Log;

public class ProFragment extends Fragment {

    public final AuthorsScreen screen;
    FragmentRoot root;
    public boolean isFadeIn;
    public boolean isFadeOut;

    public ProFragment(AuthorsScreen screen) {
        this.screen = screen;
    }

    public void create() {

        screen.assetManager.load("ui/banner_fond_1.png", Texture.class);
        screen.assetManager.load("menu/b_full_version_2.png", Texture.class);

        screen.assetManager.finishLoading();

        ProBanner banner = new ProBanner(this);
        banner.create();

        Pixmap px = new Pixmap(1, 1, Pixmap.Format.RGBA4444);
        px.setColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, .3f);
        px.fill();

        Container<?> c = new Container<>();
        c.setTouchable(Touchable.enabled);
//        c.setBackground(new SpriteDrawable(new Sprite(new Texture(px))));

        root = new FragmentRoot();
        root.addContainer(c);
        root.addContainer(banner);

        c.addListener(new ClickListener() {
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

        root.setVisible(false);
    }

    public void onOkClicked() {
        try {

            root.setTouchable(Touchable.disabled);

            fadeOut(new Runnable() {
                @Override
                public void run() {
                    screen.openAdultGateBanner(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                screen.game.params.appStore.openRateUrl();
                            } catch (Throwable e) {
                                Log.e(tag, e);
                            }
                        }
                    });
                }
            });

        } catch (Throwable e) {
            Log.e(tag, e);
        }
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

        screen.game.currentSfx = screen.game.sfxService.onProBannerShown();
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
}
