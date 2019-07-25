package ua.gram.munhauzen.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.MunhauzenStage;
import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.screen.menu.fragment.ControlsFragment;
import ua.gram.munhauzen.screen.menu.fragment.ImageFragment;
import ua.gram.munhauzen.ui.MenuLayers;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class MenuScreen implements Screen {

    private final String tag = getClass().getSimpleName();
    public final MunhauzenGame game;
    public MunhauzenStage ui;
    public MenuLayers layers;
    public final AssetManager assetManager;
    public ImageFragment imageFragment;
    public ControlsFragment controlsFragment;
    private Texture background;
    private boolean isLoaded;

    public MenuScreen(MunhauzenGame game) {
        this.game = game;
        assetManager = new AssetManager();
    }

    @Override
    public void show() {

        Log.i(tag, "show");

        ui = new MunhauzenStage(game);

        isLoaded = false;
        GameState.unpause();

        assetManager.load("menu/icon_crown_sheet_1x9.png", Texture.class);
        assetManager.load("menu/icon_helm_sheet_1x5.png", Texture.class);
        assetManager.load("menu/icon_lion_sheet_1x8.png", Texture.class);
        assetManager.load("menu/icon_rose_sheet_1x6.png", Texture.class);
        assetManager.load("menu/icon_shield_sheet_1x8.png", Texture.class);
        assetManager.load("menu/mmv_btn.png", Texture.class);
        assetManager.load("menu/mmv_fond_1.jpg", Texture.class);
    }

    private void onResourcesLoaded() {

        Log.i(tag, "onResourcesLoaded");

        isLoaded = true;

        background = game.assetManager.get("p0.jpg", Texture.class);

        layers = new MenuLayers(this);

        ui.addActor(layers);

        controlsFragment = new ControlsFragment(this);
        controlsFragment.create();

        imageFragment = new ImageFragment(this);
        imageFragment.create();

        layers.setBackgroundLayer(imageFragment);
        //layers.setControlsLayer(controlsFragment);

        Gdx.input.setInputProcessor(ui);
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        assetManager.update();

        if (!isLoaded) {

            if (assetManager.isFinished()) {
                onResourcesLoaded();
            }
            return;
        }

        drawBackground();

        if (ui != null) {
            ui.act(delta);
            ui.draw();
        }
    }

    private void drawBackground() {
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

        isLoaded = false;

        assetManager.dispose();

        if (ui != null) {
            ui.dispose();
            ui = null;
        }

        layers.dispose();

        background = null;
    }

    public void onCriticalError(Throwable e) {
        game.onCriticalError(e);
        dispose();
    }
}
