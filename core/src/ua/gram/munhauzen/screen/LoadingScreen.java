package ua.gram.munhauzen.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.Align;

import ua.gram.munhauzen.FontProvider;
import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.MunhauzenStage;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class LoadingScreen implements Screen {

    private final MunhauzenGame game;
    private Texture background;
    private MunhauzenStage ui;

    public LoadingScreen(MunhauzenGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        ui = new MunhauzenStage(game);

        Texture decor = new Texture("LoadingScreen/lv_decor_1.png");
        background = new Texture("a0.jpg");

        Label.LabelStyle h2Style = new Label.LabelStyle(
                game.fontProvider.getFont(FontProvider.BuxtonSketch, FontProvider.h2),
                Color.BLACK
        );
        Label title = new Label("Выберите режим игры", h2Style);
        title.setWrap(true);
        title.setAlignment(Align.center);

        Label.LabelStyle h5Style = new Label.LabelStyle(
                game.fontProvider.getFont(FontProvider.BuxtonSketch, FontProvider.h5),
                Color.BLACK
        );
        Label footer = new Label("Пословица ПословицаПословица ПословицаПословица Пословица", h5Style);
        footer.setWrap(true);
        footer.setAlignment(Align.center);

        Actor offlineButton = game.buttonBuilder.primary("Оффлайн", new Runnable() {
            @Override
            public void run() {

            }
        });

        Actor onlineButton = game.buttonBuilder.primary("Онлайн", new Runnable() {
            @Override
            public void run() {

            }
        });

        Group container = new VerticalGroup();
        container.addActor(offlineButton);
        container.addActor(onlineButton);

        Table rootContainer = new Table();
        rootContainer.setFillParent(true);
        rootContainer.pad(10);

        rootContainer.add(title).width(MunhauzenGame.WORLD_WIDTH * 0.75f).pad(10).align(Align.center).row();
        rootContainer.add(container).expand().row();
        rootContainer.add(footer).width(MunhauzenGame.WORLD_WIDTH * 0.75f).pad(10).align(Align.center).row();

        Image decorTop = new Image(decor);
        Image decorBottom = new Image(decor);

        decorTop.setPosition(0, MunhauzenGame.WORLD_HEIGHT - decor.getHeight());
        decorBottom.setPosition(0, 0);

        decorBottom.setWidth(MunhauzenGame.WORLD_WIDTH);
        decorTop.setWidth(MunhauzenGame.WORLD_WIDTH);

        decorTop.setOrigin(decorTop.getWidth() / 2f, decorTop.getHeight() / 2f);

        decorTop.setRotation(180);

        ui.addActor(rootContainer);
        ui.addActor(decorTop);
        ui.addActor(decorBottom);
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
        if (ui != null) {
            ui.dispose();
        }
    }
}
