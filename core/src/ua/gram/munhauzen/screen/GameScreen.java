package ua.gram.munhauzen.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.MunhauzenStage;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class GameScreen implements Screen {

    private final MunhauzenGame game;
    private Texture background;
    private MunhauzenStage ui;
    private AssetManager assetManager;

    public GameScreen(MunhauzenGame game) {
        this.game = game;
    }

    @Override
    public void show() {

        Gdx.app.log("GameScreen", "show");
        ui = new MunhauzenStage(game);

        assetManager = new AssetManager();

        assetManager.load("ui/player_progress_bar.9.png", Texture.class);
        assetManager.load("ui/player_progress_bar_progress.9.jpg", Texture.class);
        assetManager.load("ui/player_progress_bar_knob.png", Texture.class);
        assetManager.load("GameScreen/b_bookmenu.png", Texture.class);
        assetManager.load("GameScreen/b_booksound_on.png", Texture.class);
        assetManager.load("GameScreen/b_booksound_off.png", Texture.class);

        assetManager.finishLoading();

        background = game.assetManager.get("a0.jpg", Texture.class);

        ProgressBar.ProgressBarStyle barStyle = new ProgressBar.ProgressBarStyle();
        barStyle.background = new NinePatchDrawable(new NinePatch(
                assetManager.get("ui/player_progress_bar_progress.9.jpg", Texture.class),
                10, 10, 0, 0
        ));
        barStyle.knob = new SpriteDrawable(new Sprite(assetManager.get("ui/player_progress_bar_knob.png", Texture.class)));
        ProgressBar bar = new ProgressBar(0, 100, 1, false, barStyle);

        Image barBackground = new Image(new NinePatchDrawable(new NinePatch(
                assetManager.get("ui/player_progress_bar.9.png", Texture.class),
                130, 1000 - 270, 0, 0
        )));

        Table barTable = new Table();
        barTable.pad(40, 100, 40, 100);
        barTable.add().expandX().height(100).row();
        barTable.add(bar).fillX().expandX().height(100).row();

        barBackground.setWidth(barTable.getWidth());
        barBackground.setHeight(barTable.getHeight());

        Stack barContainer = new Stack();
        barContainer.addActor(barBackground);
        barContainer.addActor(barTable);

        ImageButton soundButton = new ImageButton(new SpriteDrawable(new Sprite(
                assetManager.get("GameScreen/b_booksound_off.png", Texture.class))));

        ImageButton menuButton = new ImageButton(new SpriteDrawable(new Sprite(
                assetManager.get("GameScreen/b_bookmenu.png", Texture.class))));

        Table actionsContainer = new Table();
        actionsContainer.add(soundButton).width(150).height(300);
        actionsContainer.add(menuButton).width(150).height(300);

        Table container = new Table();
        container.setFillParent(true);
        container.add(actionsContainer).align(Align.topRight).expandX().row();
        container.add(barContainer).align(Align.bottom).fillX().expand().row();

        ui.addActor(container);

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

        game.batch.begin();
        game.batch.disableBlending();

        game.batch.draw(background,
                0, 0, //position
                MunhauzenGame.WORLD_WIDTH, MunhauzenGame.WORLD_HEIGHT //width
        );

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
        if (assetManager != null) {
            assetManager.dispose();
        }
        if (ui != null) {
            ui.dispose();
        }
    }
}
