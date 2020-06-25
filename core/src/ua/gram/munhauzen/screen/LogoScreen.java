package ua.gram.munhauzen.screen;

import com.badlogic.gdx.Gdx;
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
import ua.gram.munhauzen.ui.FragmentRoot;
import ua.gram.munhauzen.ui.MunhauzenStage;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class LogoScreen extends MunhauzenScreen {

    private Texture background;
    private MunhauzenStage ui;

    public LogoScreen(MunhauzenGame game) {
        super(game);
    }

    @Override
    public void show() {
        ui = new MunhauzenStage(game);

        game.loadGameState();

        background = game.internalAssetManager.get("p0.jpg", Texture.class);

        Image logo = new Image(new Texture("logo_500.png"));

        Label title = new Label(game.t("logo.title"), new Label.LabelStyle(
                game.fontProvider.getFont(FontProvider.h2),
                Color.BLACK
        ));
        title.setWrap(true);
        title.setAlignment(Align.center);

        Table table = new Table();
        table.setFillParent(true);
        table.pad(10);
        table.add(logo).size(MunhauzenGame.WORLD_WIDTH * .5f).pad(10).row();
        table.add(title).width(MunhauzenGame.WORLD_WIDTH * .9f).row();

        Label version = new Label("v" + game.params.versionName + " " + game.params.locale, new Label.LabelStyle(
                game.fontProvider.getFont(FontProvider.DroidSansMono, FontProvider.p),
                Color.BLACK
        ));
        version.setWrap(false);
        version.setAlignment(Align.center);

        Container<Label> versionContainer = new Container<>(version);
        versionContainer.align(Align.bottom);
        versionContainer.pad(10);

        FragmentRoot container = new FragmentRoot();
        container.addContainer(table);
        container.addContainer(versionContainer);

        ui.addActor(container);

        Gdx.input.setInputProcessor(ui);

        table.setVisible(false);
        table.addAction(
                Actions.sequence(
                        Actions.alpha(0),
                        Actions.delay(.3f),
                        Actions.visible(true),
                        Actions.alpha(1, .4f),
                        Actions.delay(2.5f),
                        Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                onComplete();
                            }
                        })
                )
        );

        game.sfxService.onLogoScreenOpened();
    }

    private void onComplete() {
        game.navigator.openNextPage();
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
