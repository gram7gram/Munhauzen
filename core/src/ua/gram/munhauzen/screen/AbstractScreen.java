package ua.gram.munhauzen.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Timer;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.MunhauzenStage;
import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.utils.ExpansionAssetManager;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public abstract class AbstractScreen implements Screen {

    protected final String tag = getClass().getSimpleName();
    public final MunhauzenGame game;
    public MunhauzenStage ui;
    public AssetManager assetManager;
    protected Texture background;
    private boolean isLoaded, isDisposed;
    Timer.Task persistTask;

    public AbstractScreen(MunhauzenGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        Log.i(tag, "show");

        isDisposed = false;
        isLoaded = false;

        assetManager = new ExpansionAssetManager();

        background = game.assetManager.get("p0.jpg", Texture.class);

        ui = new MunhauzenStage(game);

        GameState.unpause(tag);

        Gdx.input.setInputProcessor(ui);

        game.databaseManager.loadExternal(game.gameState);

        persistTask = new Timer.Task() {
            @Override
            public void run() {
                try {

                    if (game.databaseManager != null && game.gameState != null) {
                        game.databaseManager.persist(game.gameState);
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

        if (!persistTask.isScheduled()) {
            Timer.instance().scheduleTask(persistTask, 5, 5);
        }
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        if (isDisposed) return;

        if (assetManager == null) return;

        drawBackground();

        assetManager.update();

        if (!isLoaded) {

            if (assetManager.isFinished()) {
                onResourcesLoaded();
            }
            return;
        }

        checkInput();

        renderAfterLoaded(delta);

        if (ui != null) {
            ui.act(delta);
            ui.draw();
        }
    }

    public void checkInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.BACK)) {
            onBackPressed();
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
                0, 0, //position
                MunhauzenGame.WORLD_WIDTH, MunhauzenGame.WORLD_HEIGHT //width
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

        GameState.clearTimer();

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
        game.onCriticalError(e);
        dispose();
    }

    public void navigateTo(Screen screen) {

        Log.i(tag, "navigateTo " + getClass().getSimpleName() + " => " + screen.getClass().getSimpleName());

        try {
            Gdx.input.setInputProcessor(null);

            game.databaseManager.persist(game.gameState);

            game.setScreen(screen);
            dispose();
        } catch (Throwable e) {
            Log.e(tag, e);

            game.onCriticalError(e);
        }
    }
}
