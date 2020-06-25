package ua.gram.munhauzen.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Timer;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.ui.MunhauzenStage;
import ua.gram.munhauzen.utils.ExpansionAssetManager;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public abstract class AbstractScreen extends MunhauzenScreen {

    public MunhauzenStage ui;
    public AssetManager assetManager;
    protected Texture background;
    private boolean isLoaded;
    protected boolean isDisposed, isBackPressed;
    Timer.Task persistTask;

    public AbstractScreen(MunhauzenGame game) {
        super(game);
    }

    @Override
    public void show() {
        Log.i(tag, "show");

        isBackPressed = false;
        isDisposed = false;
        isLoaded = false;

        assetManager = new ExpansionAssetManager(game);

        background = game.internalAssetManager.get("p0.jpg", Texture.class);

        ui = new MunhauzenStage(game);

        GameState.unpause(tag);

        Gdx.input.setInputProcessor(ui);

        game.databaseManager.loadExternal(game.gameState);

        persistTask = new Timer.Task() {
            @Override
            public void run() {
                try {

                    if (game.databaseManager != null && game.gameState != null) {
                        game.databaseManager.persistSync(game.gameState);
                    }

                } catch (Throwable e) {
                    Log.e(tag, e);
                    cancel();
                }
            }
        };
    }

    protected void onResourcesLoaded() {

        Log.i(tag, "onResourcesLoaded");

        isLoaded = true;
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    public void fillBackgroundColor() {
        Gdx.gl.glClearColor(235 / 255f, 232 / 255f, 112 / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
    }

    @Override
    public void render(float delta) {

        fillBackgroundColor();

        if (isDisposed) return;

        checkBackPressed();

        if (assetManager == null) return;

        drawBackground();

        try {
            assetManager.update();
        } catch (Throwable ignore) {
        }

        if (!isLoaded) {

            if (assetManager.isFinished()) {
                onResourcesLoaded();
            }
            return;
        }

        try {
            if (persistTask != null && !persistTask.isScheduled()) {
                Timer.instance().scheduleTask(persistTask, 1, 5);
            }
        } catch (Throwable e) {
            Log.e(tag, e);
        }

        renderAfterLoaded(delta);

        if (ui != null) {
            ui.act(delta);
            ui.draw();
        }
    }

    public void checkBackPressed() {
        if (Gdx.input.isKeyPressed(Input.Keys.BACK)) {
            if (!isBackPressed) {
                isBackPressed = true;
                onBackPressed();
            }
        }
    }

    public void onBackPressed() {
        Log.i(tag, "onBackPressed");
    }

    public abstract void renderAfterLoaded(float delta);

    private void drawBackground() {
        if (background == null) return;

        game.batch.begin();
        game.batch.disableBlending();

        game.batch.draw(background,
                0, 0,
                MunhauzenGame.WORLD_WIDTH, MunhauzenGame.WORLD_HEIGHT
        );

        game.batch.enableBlending();
        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {
        MunhauzenGame.pauseGame();
    }

    @Override
    public void resume() {
        MunhauzenGame.resumeGame();
    }

    @Override
    public void dispose() {

        Log.i(tag, "dispose");

        isBackPressed = false;
        isLoaded = false;
        isDisposed = true;

        if (assetManager != null) {
            assetManager.dispose();
            assetManager = null;
        }

        if (persistTask != null) {
            persistTask.cancel();
            persistTask = null;
        }

        ui.dispose();

        background = null;
    }

    public void onCriticalError(Throwable e) {
        game.navigator.onCriticalError(e);
    }

    public void navigateTo(Screen screen) {
        game.navigator.navigateTo(screen);
    }

}
