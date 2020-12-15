package ua.gram.munhauzen.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

import ua.gram.munhauzen.GameLayerInterface;
import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.ui.ChapterDownloadFragment;
import ua.gram.munhauzen.ui.Fragment;
import ua.gram.munhauzen.ui.GameModeFragment;
import ua.gram.munhauzen.ui.Gift6Fragment;
import ua.gram.munhauzen.ui.NoInternetFragment;
import ua.gram.munhauzen.ui.NoMemoryFragment;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public abstract class MunhauzenScreen implements Screen {

    protected final String tag = getClass().getSimpleName();
    public final MunhauzenGame game;
    public NoMemoryFragment noMemoryFragment;
    public NoInternetFragment noInternetFragment;
    public GameModeFragment gameModeFragment;
    public ChapterDownloadFragment chapterDownloadFragment;
    public Gift6Fragment gift6Fragment;

    public MunhauzenScreen(MunhauzenGame game) {
        this.game = game;
    }

    public abstract GameLayerInterface getLayers();

    public void onCriticalError(Throwable e) {
        game.navigator.onCriticalError(e);
    }

    public void navigateTo(Screen screen) {
        game.navigator.navigateTo(screen);
    }

    public void openAdultGateBanner(Runnable task) {
        Log.i(tag, "openAdultGateBanner");
    }

    public void openGameModeBanner(Runnable action, Runnable soundAction) {
        try {

            destroyBanners();

            gameModeFragment = new GameModeFragment(this);
            gameModeFragment.create(action, soundAction);

            getLayers().setBannerLayer(gameModeFragment);

            gameModeFragment.fadeIn();

        } catch (Throwable e) {
            Log.e(tag, e);

            onCriticalError(e);
        }
    }

    public void destroyBanners() {
        Log.i(tag, "destroyBanners");

        try {

            game.stopCurrentSfx();

            Gdx.input.setOnscreenKeyboardVisible(false);

            GameLayerInterface layer = getLayers();
            if (layer != null) {
                Fragment currentBanner = layer.getBannerLayer();
                if (currentBanner != null) {
                    currentBanner.destroy();
                    layer.setBannerLayer(null);
                }
            }

            if (noMemoryFragment != null) {
                noMemoryFragment.destroy();
                noMemoryFragment = null;
            }
            if (noInternetFragment != null) {
                noInternetFragment.destroy();
                noInternetFragment = null;
            }
            if (gameModeFragment != null) {
                gameModeFragment.destroy();
                gameModeFragment = null;
            }
            if (chapterDownloadFragment != null) {
                chapterDownloadFragment.destroy();
                chapterDownloadFragment = null;
            }
            if (gift6Fragment != null) {
                gift6Fragment.destroy();
                gift6Fragment = null;
            }
        } catch (Throwable ignore) {
        }

    }

    public void openNoMemoryBanner(Runnable action) {
        try {

            destroyBanners();

            noMemoryFragment = new NoMemoryFragment(this);
            noMemoryFragment.create(action);

            getLayers().setBannerLayer(noMemoryFragment);

            noMemoryFragment.fadeIn();

        } catch (Throwable e) {
            Log.e(tag, e);

            onCriticalError(e);
        }
    }

    public void openNoInternetBanner(final Runnable action) {
        try {

            if (noInternetFragment != null) return;

            destroyBanners();

            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    try {
                        noInternetFragment = new NoInternetFragment(MunhauzenScreen.this);
                        noInternetFragment.create(action);

                        getLayers().setBannerLayer(noInternetFragment);

                        noInternetFragment.fadeIn();

                    } catch (Throwable e) {
                        Log.e(tag, e);

                        onCriticalError(e);
                    }
                }
            });

        } catch (Throwable e) {
            Log.e(tag, e);

            onCriticalError(e);
        }
    }

    public void openChapterDownloadBanner(final Runnable action) {
        try {

            if (chapterDownloadFragment != null) return;

            destroyBanners();

            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    try {
                        chapterDownloadFragment = new ChapterDownloadFragment(MunhauzenScreen.this);
                        chapterDownloadFragment.create(action);

                        getLayers().setBannerLayer(chapterDownloadFragment);

                        chapterDownloadFragment.fadeIn();

                    } catch (Throwable e) {
                        Log.e(tag, e);

                        onCriticalError(e);
                    }
                }
            });

        } catch (Throwable e) {
            Log.e(tag, e);

            onCriticalError(e);
        }
    }

    public void openGift6Banner(Runnable action) {
        try {

            destroyBanners();

            gift6Fragment = new Gift6Fragment(this);
            gift6Fragment.create(action);

            getLayers().setBannerLayer(gift6Fragment);

            gift6Fragment.fadeIn();

        } catch (Throwable e) {
            Log.e(tag, e);

            onCriticalError(e);
        }
    }
}
