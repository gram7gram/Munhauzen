package ua.gram.munhauzen.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import ua.gram.munhauzen.ButtonBuilder;
import ua.gram.munhauzen.FontProvider;
import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.ui.MunhauzenStage;
import ua.gram.munhauzen.ui.PrimaryButton;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class LegalScreen implements Screen {

    private final String tag = getClass().getSimpleName();
    private final MunhauzenGame game;
    private Texture background;
    private MunhauzenStage ui;

    public LegalScreen(MunhauzenGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        ui = new MunhauzenStage(game);

        game.loadGameState();

        background = game.internalAssetManager.get("p0.jpg", Texture.class);

        float minWidth = MunhauzenGame.WORLD_WIDTH * .9f;

        Label.LabelStyle style = new Label.LabelStyle(
                game.fontProvider.getFont(FontProvider.h4),
                Color.BLACK
        );

        Label title = new Label(game.t("legal.title"), new Label.LabelStyle(
                game.fontProvider.getFont(FontProvider.h2),
                Color.BLACK
        ));
        title.setWrap(true);
        title.setAlignment(Align.center);

        Table rows = new Table();
        for (String sentence : game.t("legal.content").split("\n")) {
            Label label = new Label(sentence, style);
            label.setAlignment(Align.center);
            label.setWrap(true);

            rows.add(label).width(minWidth)
                    .padBottom(10)
                    .row();
        }

        PrimaryButton btn = game.buttonBuilder.primaryRose(game.t("legal.btn"), new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                try {

                    game.gameState.menuState.isLegalViewed = true;

                    navigateTo(new LoadingScreen(game));

                } catch (Throwable e) {
                    Log.e(tag, e);

                    onCriticalError(e);
                }
            }
        });

        Table root = new Table();
        root.setFillParent(true);

        root.add(title)
                .width(minWidth)
                .center()
                .padBottom(20)
                .row();
        root.add(rows)
                .width(minWidth)
                .center()
                .padBottom(20)
                .row();
        root.add(btn)
                .width(ButtonBuilder.BTN_PRIMARY_WIDTH)
                .height(ButtonBuilder.BTN_PRIMARY_HEIGHT)
                .center()
                .row();

        root.setVisible(false);
        root.addAction(
                Actions.sequence(
                        Actions.alpha(0),
                        Actions.delay(.3f),
                        Actions.visible(true),
                        Actions.alpha(1, .4f)
                )
        );

        ui.addActor(root);

        Gdx.input.setInputProcessor(ui);
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
