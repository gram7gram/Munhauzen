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

        game.stopCurrentSfx();

        Gdx.input.setOnscreenKeyboardVisible(false);

        Fragment currentBanner = getLayers().getBannerLayer();
        if (currentBanner != null) {
            currentBanner.destroy();
            getLayers().setBannerLayer(null);
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

    public void openNoInternetBanner(Runnable action) {
        try {

            destroyBanners();

            noInternetFragment = new NoInternetFragment(this);
            noInternetFragment.create(action);

            getLayers().setBannerLayer(noInternetFragment);

            noInternetFragment.fadeIn();

        } catch (Throwable e) {
            Log.e(tag, e);

            onCriticalError(e);
        }
    }

    public void openChapterDownloadBanner(Runnable action) {
        try {

            destroyBanners();

            chapterDownloadFragment = new ChapterDownloadFragment(this);
            chapterDownloadFragment.create(action);

            getLayers().setBannerLayer(chapterDownloadFragment);

            chapterDownloadFragment.fadeIn();

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
