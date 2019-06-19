package ua.gram.munhauzen.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

import ua.gram.munhauzen.FontProvider;
import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.MunhauzenStage;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class LogoScreen implements Screen {

    private final MunhauzenGame game;
    private Texture background;
    private MunhauzenStage ui;

    public LogoScreen(MunhauzenGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        ui = new MunhauzenStage(game);

        background = game.assetManager.get("p0.jpg", Texture.class);

        Image logo = new Image(new Texture("logo_500.png"));

        Label title = new Label("FingerTips and Co\nPresents", new Label.LabelStyle(
                game.fontProvider.getFont(FontProvider.Arnold, FontProvider.h2),
                Color.BLACK
        ));
        title.setAlignment(Align.center);

        Container<Label> titleContainer = new Container<Label>(title);
        titleContainer.align(Align.center)
                .fillX()
                .pad(10);

        Container<Image> logoContainer = new Container<Image>(logo);
        logoContainer.align(Align.center)
                .fillX()
                .pad(10);

        Table rootContainer = new Table();
        rootContainer.setFillParent(true);
        rootContainer.pad(10).center();

        rootContainer.add(logoContainer).row();
        rootContainer.add(titleContainer).row();

        rootContainer.addAction(
                Actions.sequence(
                        Actions.parallel(
                                Actions.moveBy(0, 100, 0),
                                Actions.alpha(0, 0)
                        ),
                        Actions.parallel(
                                Actions.moveBy(0, -100, 0.4f),
                                Actions.alpha(1, 0.4f)
                        ),
                        Actions.delay(1),
                        Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                onComplete();
                            }
                        })
                )
        );

        ui.addActor(rootContainer);
    }

    private void onComplete() {
        game.setScreen(new LoadingScreen(game));
        dispose();
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
        game.batch.draw(background, 0, 0, MunhauzenGame.WORLD_WIDTH, MunhauzenGame.WORLD_HEIGHT);
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
