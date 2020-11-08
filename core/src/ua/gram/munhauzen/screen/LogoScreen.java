package ua.gram.munhauzen.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;

import ua.gram.munhauzen.GameLayerInterface;
import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.GamePreferences;
import ua.gram.munhauzen.screen.logo.fragment.ContentFragment;
import ua.gram.munhauzen.screen.logo.ui.LogoLayers;
import ua.gram.munhauzen.ui.MunhauzenStage;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class LogoScreen extends MunhauzenScreen {

    private LogoLayers layers;
    private Texture background;
    private MunhauzenStage ui;

    public LogoScreen(MunhauzenGame game) {
        super(game);
    }

    @Override
    public GameLayerInterface getLayers() {
        return layers;
    }

    @Override
    public void show() {
        ui = new MunhauzenStage(game);
        layers = new LogoLayers();

        game.loadGameState();

        background = game.internalAssetManager.get("p0.jpg", Texture.class);

        ContentFragment container = new ContentFragment(this);
        container.create();

        layers.setContentLayer(container);

        container.fadeIn();

        ui.addActor(layers);

        Gdx.input.setInputProcessor(ui);
    }

    public void onComplete() {

        try {
            if (game.gameState.preferences == null) {
                game.gameState.preferences = new GamePreferences();
            }

            if (!game.gameState.preferences.isGameModeSelected) {
                openGameModeBanner(new Runnable() {
                    @Override
                    public void run() {
                        game.navigator.openNextPage();
                    }
                }, new Runnable() {
                    @Override
                    public void run() {
                        game.sfxService.onGameModeSelect();
                    }
                });
            } else {
                game.navigator.openNextPage();
            }
        } catch (Throwable e) {
            Log.e(tag, e);

            onCriticalError(e);
        }
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(235 / 255f, 232 / 255f, 112 / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        if (background == null) return;

        game.batch.begin();
        game.batch.disableBlending();
        game.batch.draw(background,
                0, 0,
                MunhauzenGame.WORLD_WIDTH, MunhauzenGame.WORLD_HEIGHT);
        game.batch.enableBlending();
        game.batch.end();

        ui.act(delta);
        ui.draw();
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
        if (ui != null) {
            ui.dispose();
        }
    }
}
