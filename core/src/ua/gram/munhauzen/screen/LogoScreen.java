package ua.gram.munhauzen.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

import ua.gram.munhauzen.FontProvider;
import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.MunhauzenStage;
import ua.gram.munhauzen.expansion.response.ExpansionResponse;
import ua.gram.munhauzen.utils.ExternalFiles;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class LogoScreen implements Screen {

    private final String tag = getClass().getSimpleName();
    private final MunhauzenGame game;
    private Texture background;
    private MunhauzenStage ui;
    boolean canRedirectToLoading;

    public LogoScreen(MunhauzenGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        ui = new MunhauzenStage(game);

        restoreFromFile();

        background = game.assetManager.get("p0.jpg", Texture.class);

        Image logo = new Image(new Texture("logo_500.png"));

        Label title = new Label("FingerTips and Co\nPresents", new Label.LabelStyle(
                game.fontProvider.getFont(FontProvider.h2),
                Color.BLACK
        ));
        title.setWrap(true);
        title.setAlignment(Align.center);

        Table root = new Table();
        root.setFillParent(true);
        root.pad(10);
        root.add(logo).size(MunhauzenGame.WORLD_WIDTH * .5f).pad(10).row();
        root.add(title).width(MunhauzenGame.WORLD_WIDTH * .7f).row();

        root.setVisible(false);
        root.addAction(
                Actions.sequence(
                        Actions.moveBy(0, 100),
                        Actions.alpha(0),
                        Actions.visible(true),
                        Actions.parallel(
                                Actions.moveBy(0, -100, .4f),
                                Actions.alpha(1, .4f)
                        ),
                        Actions.delay(.6f),
                        Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                onComplete();
                            }
                        })
                )
        );

        ui.addActor(root);
    }

    private void restoreFromFile() {
        try {
            FileHandle file = ExternalFiles.getExpansionInfoFile(game.params);
            if (file.exists()) {

                Json json = new Json(JsonWriter.OutputType.json);
                json.setIgnoreUnknownFields(true);

                ExpansionResponse result = json.fromJson(ExpansionResponse.class, file.read());

                if (result != null && result.version == game.params.versionCode) {
                    canRedirectToLoading = !result.isCompleted;
                }
            }
        } catch (Throwable e) {
            Log.e(tag, e);
        }

    }

    private void onComplete() {

        if (canRedirectToLoading) {
            game.setScreen(new LoadingScreen(game));
        } else {
            game.setScreen(new DebugScreen(game));
        }

        dispose();
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        if (background == null) return;

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
