package ua.gram.munhauzen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.screen.AuthorsScreen;
import ua.gram.munhauzen.screen.FailsScreen;
import ua.gram.munhauzen.screen.GalleryScreen;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.screen.LogoScreen;
import ua.gram.munhauzen.screen.MenuScreen;
import ua.gram.munhauzen.screen.PaintingScreen;
import ua.gram.munhauzen.screen.SavesScreen;
import ua.gram.munhauzen.utils.Log;

public class ScreenNavigator {

    final String tag = getClass().getSimpleName();
    final MunhauzenGame game;

    public boolean shouldIgnoreSavedScreenUntilAppIsClosed;

    protected ScreenNavigator(MunhauzenGame game) {
        this.game = game;
    }

    public void openCurrentScreen() {

        String currentScreen = game.gameState.preferences.currentScreen;

        if (GalleryScreen.class.getSimpleName().equals(currentScreen)
                || PaintingScreen.class.getSimpleName().equals(currentScreen)) {
            game.setScreen(new GalleryScreen(game));

        } else if (FailsScreen.class.getSimpleName().equals(currentScreen)) {
            game.setScreen(new FailsScreen(game));

        } else if (SavesScreen.class.getSimpleName().equals(currentScreen)) {
            game.setScreen(new SavesScreen(game));

        } else if (AuthorsScreen.class.getSimpleName().equals(currentScreen)) {
            game.setScreen(new AuthorsScreen(game));

        } else if (MenuScreen.class.getSimpleName().equals(currentScreen)) {
            game.setScreen(new MenuScreen(game));

        } else if (GameScreen.class.getSimpleName().equals(currentScreen)) {
            game.setScreen(new GameScreen(game));

        } else {
            game.setScreen(new LogoScreen(game));
        }
    }

    public void onCriticalError(Throwable e) {
        game.onCriticalError(e);
        game.getScreen().dispose();
    }

    public void navigateTo(Screen screen) {

        Screen screenBefore = game.getScreen();

        String newScreen = screen.getClass().getSimpleName();

        Log.i(tag, "navigateTo "
                + (screenBefore != null ? screenBefore.getClass().getSimpleName() : "---")
                + " => " + newScreen);

        try {
            Gdx.input.setInputProcessor(null);

            if (!shouldIgnoreSavedScreenUntilAppIsClosed) {
                try {
                    game.gameState.preferences.currentScreen = newScreen;
                } catch (Throwable ignore) {
                }
            }

            try {
                game.databaseManager.persistSync(game.gameState);
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
            game.gameState.preferences.currentScreen = null;
        } catch (Throwable ignore) {
        }

        try {
            game.databaseManager.persistSync(game.gameState);
        } catch (Throwable ignore) {
        }

        Gdx.app.exit();
    }

    public void forceLogoScreenOnBoot() {
        shouldIgnoreSavedScreenUntilAppIsClosed = true;
        try {
            game.gameState.preferences.currentScreen = null;
        } catch (Throwable ignore) {
        }
    }
}
