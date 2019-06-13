package ua.gram.munhauzen.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.MunhauzenStage;
import ua.gram.munhauzen.fragment.mainmenu.MenuFragment;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class MainMenuScreen implements Screen {

    private final MunhauzenGame game;
    private Texture background;
    private MunhauzenStage ui;
    private MenuFragment menuFragment;

    public MainMenuScreen(MunhauzenGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        ui = new MunhauzenStage(game);

        background = game.assetManager.get("a0.jpg", Texture.class);

        menuFragment = new MenuFragment(game);
        menuFragment.create();

        ui.addActor(menuFragment.getRoot());

        Gdx.input.setInputProcessor(ui);
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        drawBackground();

        if (menuFragment != null)
            menuFragment.update();

        ui.act(delta);
        ui.draw();
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
        if (ui != null) {
            ui.dispose();
        }

        if (menuFragment != null) {
            menuFragment.dispose();
            menuFragment = null;
        }
    }
}
