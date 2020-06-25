package ua.gram.munhauzen.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.GdxRuntimeException;

import ua.gram.munhauzen.ButtonBuilder;
import ua.gram.munhauzen.FontProvider;
import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.ui.FragmentRoot;
import ua.gram.munhauzen.ui.MunhauzenStage;
import ua.gram.munhauzen.ui.PrimaryButton;
import ua.gram.munhauzen.utils.ColorUtils;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class ErrorScreen extends MunhauzenScreen {

    private MunhauzenStage ui;
    final Throwable e;
    Container<Table> titleContainer;
    ScrollPane errorContainer;

    public ErrorScreen(MunhauzenGame game, Throwable e) {
        super(game);
        this.e = e;
    }

    @Override
    public void show() {
        ui = new MunhauzenStage(game);

        createErrorContainer();

        createTitleContainer();

        FragmentRoot root = new FragmentRoot();
        root.addContainer(titleContainer);
        root.addContainer(errorContainer);

        ui.addActor(root);

        showTitle();

        Gdx.input.setInputProcessor(ui);
    }

    private void showTitle() {
        titleContainer.setVisible(true);
        errorContainer.setVisible(false);
    }

    private void showError() {
        titleContainer.setVisible(false);
        errorContainer.setVisible(true);
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(Color.DARK_GRAY.r, Color.DARK_GRAY.g, Color.DARK_GRAY.b, Color.DARK_GRAY.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        if (ui != null) {
            ui.act(delta);
            ui.draw();
        }
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

    private void createTitleContainer() {

        Table table = new Table();
        float width = MunhauzenGame.WORLD_WIDTH - 20;

        Pixmap pm1 = new Pixmap(1, 1, Pixmap.Format.RGB565);
        pm1.setColor(ColorUtils.darkRed);
        pm1.fill();

        PrimaryButton button = game.buttonBuilder.primary(game.t("error_screen.to_menu"), new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                game.setScreen(new MenuScreen(game));
                dispose();
            }
        });

        PrimaryButton errorBtn = game.buttonBuilder.primary(game.t("error_screen.show_error"), new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                showError();
            }
        });

        Label title = new Label(game.t("error_screen.title"), new Label.LabelStyle(
                game.fontProvider.getFont(FontProvider.BuxtonSketch, FontProvider.h2),
                Color.BLACK
        ));
        title.setAlignment(Align.center);
        title.setWrap(true);

        table.add(title).width(width).padBottom(20).row();

        for (String value : game.t("error_screen.subtitle").split("\n")) {
            Label subtitle = new Label(value, new Label.LabelStyle(
                    game.fontProvider.getFont(FontProvider.BuxtonSketch, FontProvider.h5),
                    Color.BLACK
            ));
            subtitle.setAlignment(Align.center);
            subtitle.setWrap(true);

            table.add(subtitle).width(width).padBottom(20).row();
        }

        table.add(button).padBottom(20).expandX()
                .width(ButtonBuilder.BTN_PRIMARY_WIDTH)
                .height(ButtonBuilder.BTN_PRIMARY_HEIGHT)
                .row();
        table.add(errorBtn).padBottom(20).expandX()
                .width(ButtonBuilder.BTN_PRIMARY_WIDTH * 1.5f)
                .height(ButtonBuilder.BTN_PRIMARY_HEIGHT)
                .row();

        titleContainer = new Container<>(table);
        titleContainer.pad(10);
        titleContainer.align(Align.center);
        titleContainer.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture(pm1))));
    }

    private void createErrorContainer() {
        String cause = e.getClass().getSimpleName(),
                msg = e.getMessage(),
                desc = game.t("error_screen.reason") + ":\n";

        if (msg != null) {
            msg = msg.replace(GdxRuntimeException.class.getCanonicalName() + ": ", "");
        }

        for (StackTraceElement trace : e.getStackTrace()) {
            desc += trace.toString() + "\n";
        }

        Pixmap pm1 = new Pixmap(1, 1, Pixmap.Format.RGB565);
        pm1.setColor(ColorUtils.darkRed);
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

        PrimaryButton button = game.buttonBuilder.primary(game.t("error_screen.to_menu"), new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                game.setScreen(new MenuScreen(game));
                dispose();
            }
        });

        float width = MunhauzenGame.WORLD_WIDTH - 20;

        Table tableTop = new Table();
        tableTop.pad(10);
        tableTop.add(causeLbl).width(width).padBottom(5).row();
        tableTop.add(title).width(width);
        tableTop.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture(pm1))));

        Table tableCenter = new Table();
        tableCenter.pad(10);
        tableCenter.add(description).width(width);

        Table tableBottom = new Table();
        tableBottom.pad(10);
        tableBottom.add(button)
                .center().expand()
                .width(ButtonBuilder.BTN_PRIMARY_WIDTH)
                .height(ButtonBuilder.BTN_PRIMARY_HEIGHT)
                .row();

        Table group = new Table();
        group.add(tableTop).top().expandX().row();
        group.add(tableCenter).top().expand().row();
        group.add(tableBottom).top().expand().row();

        errorContainer = new ScrollPane(group);
        errorContainer.setFillParent(true);
    }
}
