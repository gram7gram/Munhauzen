package ua.gram.munhauzen.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import ua.gram.munhauzen.FontProvider;
import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.MunhauzenStage;
import ua.gram.munhauzen.ui.PrimaryButton;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class ErrorScreen implements Screen {

    private final MunhauzenGame game;
    private MunhauzenStage ui;
    final Throwable e;

    public ErrorScreen(MunhauzenGame game, Throwable e) {
        this.game = game;
        this.e = e;
    }

    @Override
    public void show() {
        ui = new MunhauzenStage(game);

        String cause = e.getClass().getSimpleName(), msg = e.getMessage(), desc = "Причина:\n";

        for (StackTraceElement trace : e.getStackTrace()) {
            desc += trace.toString() + "\n";
        }

        Pixmap pm1 = new Pixmap(1, 1, Pixmap.Format.RGB565);
        pm1.setColor(Color.SCARLET);
        pm1.fill();

        Label causeLbl = new Label(cause, new Label.LabelStyle(
                game.fontProvider.getFont(FontProvider.DroidSansMono, FontProvider.p),
                Color.BLACK
        ));
        causeLbl.setWrap(true);

        Label title = new Label(msg, new Label.LabelStyle(
                game.fontProvider.getFont(FontProvider.DroidSansMono, FontProvider.h3),
                Color.BLACK
        ));
        title.setWrap(true);

        Label description = new Label(desc, new Label.LabelStyle(
                game.fontProvider.getFont(FontProvider.DroidSansMono, FontProvider.p),
                Color.WHITE
        ));
        description.setWrap(true);

        Table tableTop = new Table();
        tableTop.pad(10);
        tableTop.add(causeLbl).width(MunhauzenGame.WORLD_WIDTH - 20).padBottom(5).row();
        tableTop.add(title).width(MunhauzenGame.WORLD_WIDTH - 20);
        tableTop.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture(pm1))));

        Table tableBottom = new Table();
        tableBottom.pad(10);
        tableBottom.add(description).width(MunhauzenGame.WORLD_WIDTH - 20);

        PrimaryButton button = game.buttonBuilder.primary("To menu", new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                game.setScreen(new MainMenuScreen(game));
                dispose();
            }
        });

        Table group = new Table();
        group.setFillParent(true);
        group.add(tableTop).top().expandX().row();
        group.add(tableBottom).top().expand().row();
        group.add(button).center()
                .width(MunhauzenGame.WORLD_WIDTH / 3f)
                .height(MunhauzenGame.WORLD_HEIGHT / 15f)
                .expand().row();

        ui.addActor(group);

        Gdx.input.setInputProcessor(ui);
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(Color.DARK_GRAY.r, Color.DARK_GRAY.g, Color.DARK_GRAY.b, Color.DARK_GRAY.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

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
