package ua.gram.munhauzen.screen.menu.fragment;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import ua.gram.munhauzen.screen.MenuScreen;
import ua.gram.munhauzen.screen.menu.ui.ShareBanner;
import ua.gram.munhauzen.ui.FragmentRoot;
import ua.gram.munhauzen.utils.Log;

public class ShareFragment extends MenuFragment {

    FragmentRoot root;
    public boolean isFadeIn;
    public boolean isFadeOut;

    public ShareFragment(MenuScreen screen) {
        super(screen);
    }

    public void create() {
        super.create();

        screen.assetManager.load("ui/banner_fond_1.png", Texture.class);
        screen.assetManager.load("menu/b_share_2.png", Texture.class);
        screen.assetManager.load("menu/vk_icon.jpg", Texture.class);
        screen.assetManager.load("menu/twitter_icon.jpg", Texture.class);
        screen.assetManager.load("menu/instagram_icon.jpg", Texture.class);
        screen.assetManager.load("menu/fb_icon.jpg", Texture.class);

        screen.assetManager.finishLoading();

        ShareBanner banner = new ShareBanner(this);
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

        screen.game.stopCurrentSfx();

        screen.game.currentSfx = screen.game.sfxService.onShareBannerShown();
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

    public void onBtnCLicked(final String url) {

        screen.game.gameState.menuState.isShareViewed = true;

        root.setTouchable(Touchable.disabled);

        fadeOut(new Runnable() {
            @Override
            public void run() {
                try {
                    screen.openAdultGateBanner(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Gdx.net.openURI(url);
                            } catch (Throwable e) {
                                Log.e(tag, e);
                            }
                        }
                    });

                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }
        });
    }
}
