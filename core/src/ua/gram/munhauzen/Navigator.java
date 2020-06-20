package ua.gram.munhauzen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Timer;

import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.screen.LegalScreen;
import ua.gram.munhauzen.screen.LoadingScreen;
import ua.gram.munhauzen.screen.LogoScreen;
import ua.gram.munhauzen.screen.MenuScreen;
import ua.gram.munhauzen.screen.PurchaseScreen;
import ua.gram.munhauzen.service.ExpansionDownloadManager;
import ua.gram.munhauzen.utils.Log;

public class Navigator {

    final String tag = getClass().getSimpleName();
    final MunhauzenGame game;

    protected Navigator(MunhauzenGame game) {
        this.game = game;
    }

    public void openCurrentScreen() {
        game.setScreen(new LogoScreen(game));
    }

    public void onCriticalError(Throwable e) {
        Timer.instance().stop();

        try {
            game.getScreen().dispose();
        } catch (Throwable ignore) {
        }

        game.onCriticalError(e);
    }

    public void navigateTo(Screen screen) {

        Screen screenBefore = game.getScreen();

        String newScreen = screen.getClass().getSimpleName();

        Log.i(tag, "navigateTo "
                + (screenBefore != null ? screenBefore.getClass().getSimpleName() : "---")
                + " => " + newScreen);

        try {
            Gdx.input.setInputProcessor(null);

            try {
                game.syncState();
            } catch (Throwable ignore) {
            }

            try {
                if (game.backgroundSfxService != null)
                    game.backgroundSfxService.stop();
            } catch (Throwable ignore) {
            }

            game.setScreen(screen);

            if (screenBefore != null)
                screenBefore.dispose();

        } catch (Throwable e) {
            Log.e(tag, e);

            game.onCriticalError(e);
        }
    }

    public void closeApp() {

        Log.i(tag, "closeApp");

        GameState.clearTimer(tag);

        Gdx.input.setInputProcessor(null);

        try {
            if (game.params.iap != null)
                game.params.iap.dispose();
        } catch (Throwable e) {
            Log.e(tag, e);
        }

        try {
            game.databaseManager.persistSync(game.gameState);
        } catch (Throwable ignore) {
        }

        Gdx.app.exit();
    }

    public void openNextPage() {

        try {

            if (game.gameState == null) {
                throw new GdxRuntimeException("No game state was loaded");
            }

            boolean isLegalViewed = false;
            boolean hasPurchases = false;

            ExpansionDownloadManager downloadManager = new ExpansionDownloadManager(game, null);

            boolean needUpdates = downloadManager.shouldFetchExpansion();

            if (game.gameState.purchaseState != null) {
                hasPurchases = game.gameState.purchaseState.isVersionSelected;
            }

            if (game.gameState.preferences != null) {
                isLegalViewed = game.gameState.preferences.isLegalViewed;
            }

            if (game.gameState.menuState != null) {
                game.gameState.menuState.isFirstMenuAfterGameStart = true;
            }

            if (!isLegalViewed) {
                navigateTo(new LegalScreen(game));
            } else if (!hasPurchases) {
                navigateTo(new PurchaseScreen(game));
            } else if (needUpdates) {
                navigateTo(new LoadingScreen(game));
            } else {
                navigateTo(new MenuScreen(game));
            }

        } catch (Throwable e) {
            Log.e(tag, e);

            onCriticalError(e);
        }
    }
}
