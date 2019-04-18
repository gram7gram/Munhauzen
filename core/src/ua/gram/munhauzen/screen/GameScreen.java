package ua.gram.munhauzen.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.MunhauzenStage;
import ua.gram.munhauzen.entity.Decision;
import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.entity.Option;
import ua.gram.munhauzen.entity.OptionAudio;
import ua.gram.munhauzen.entity.OptionImage;
import ua.gram.munhauzen.entity.OptionRepository;
import ua.gram.munhauzen.entity.Scenario;
import ua.gram.munhauzen.service.ScenarioManager;
import ua.gram.munhauzen.utils.DateUtils;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class GameScreen implements Screen {

    private final String tag = getClass().getSimpleName();
    private final MunhauzenGame game;
    private Texture background;
    private MunhauzenStage ui;
    public AssetManager assetManager;
    private ScenarioManager scenarioManager;
    private ProgressBar bar;
    private Image backgroundTop;
    private Stack uiLayers;

    public GameScreen(MunhauzenGame game) {
        this.game = game;
    }

    @Override
    public void show() {

        Log.i(tag, "show");

        ui = new MunhauzenStage(game);
        scenarioManager = new ScenarioManager(this, game.gameState);

        assetManager = new AssetManager();

        assetManager.load("ui/player_progress_bar.9.png", Texture.class);
        assetManager.load("ui/player_progress_bar_progress.9.jpg", Texture.class);
        assetManager.load("ui/player_progress_bar_knob.png", Texture.class);
        assetManager.load("GameScreen/b_bookmenu.png", Texture.class);
        assetManager.load("GameScreen/b_booksound_on.png", Texture.class);
        assetManager.load("GameScreen/b_booksound_off.png", Texture.class);
        assetManager.load("GameScreen/b_star_game.png", Texture.class);
        assetManager.load("GameScreen/b_tulip_1.png", Texture.class);

        assetManager.finishLoading();

        background = game.assetManager.get("a0.jpg", Texture.class);

        Stack barContainer = prepareProgressBar();

        Table actionsContainer = prepareTitleActions();

        Table scenarioContainer = new Table();
        scenarioContainer.setFillParent(true);
        scenarioContainer.add(actionsContainer).align(Align.topRight).expandX().row();
        scenarioContainer.add(barContainer).align(Align.bottom).fillX().expand().row();

        backgroundTop = new Image();

        Container<Image> backgroundTopContainer = new Container<>(backgroundTop);
        backgroundTopContainer.setFillParent(true);

        uiLayers = new Stack();
        uiLayers.setFillParent(true);

        setBackgroundImageLayer(backgroundTopContainer);

        setScenarioUILayer(scenarioContainer);

        ui.addActor(uiLayers);

        Gdx.input.setInputProcessor(ui);

        scenarioManager.resumeScenario();
    }

    public void setBackgroundImageLayer(Actor actor) {
        uiLayers.addActorAt(0, actor);
    }

    public void setScenarioOptionsLayer(Actor actor) {
        uiLayers.addActorAt(1, actor);
    }

    public void setScenarioUILayer(Actor actor) {
        uiLayers.addActorAt(2, actor);
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        assetManager.update();

        drawBackground();

        Scenario scenario = game.gameState.history.activeSave.scenario;

        if (!scenario.isCompleted) {

            scenarioManager.updateScenario(
                    scenario.progress + (delta * 1000),
                    scenario.totalDuration
            );

            if (scenario.isCompleted) {

                scenarioManager.onScenarioCompleted();

            } else {
                OptionImage image = scenario.currentOption.currentImage;
                prepareImage(image);

                OptionAudio audio = scenario.currentOption.currentAudio;
                prepareAudio(audio);

                if (image.next != null) {
                    prepareImage((OptionImage) image.next);
                }

                if (audio.next != null) {
                    prepareAudio((OptionAudio) audio.next);
                }
            }
        }

        bar.setRange(0, scenario.totalDuration);
        bar.setValue(scenario.progress);

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

    private void prepareAudio(OptionAudio item) {
        if (item.isPrepared) return;

        String resource = "audio/" + item.id + ".ogg";

        if (!item.isPreparing) {

            if (!assetManager.isLoaded(resource, Music.class)) {
                item.isPreparing = true;
                item.prepareStartedAt = new Date();

                assetManager.load(resource, Music.class);
            }

        } else {

            if (assetManager.isLoaded(resource, Music.class)) {

                item.isPreparing = false;
                item.isPrepared = true;
                item.prepareCompletedAt = new Date();
                item.player = assetManager.get(resource, Music.class);

                onAudioPrepared(item);
            }
        }
    }

    private void prepareImage(OptionImage item) {
        if (item.isPrepared) return;

        String resource = "images/" + item.id + ".jpg";

        if (!item.isPreparing) {

            if (!assetManager.isLoaded(resource, Texture.class)) {
                item.isPreparing = true;
                item.prepareStartedAt = new Date();

                assetManager.load(resource, Texture.class);
            }

        } else {

            if (assetManager.isLoaded(resource, Texture.class)) {

                item.isPreparing = false;
                item.isPrepared = true;
                item.prepareCompletedAt = new Date();

                item.image = assetManager.get(resource, Texture.class);

                onImagePrepared(item);
            }
        }
    }

    private void onAudioPrepared(OptionAudio item) {

        Log.i(tag, "onAudioPrepared " + item.id
                + " in " + DateUtils.getDateDiff(item.prepareCompletedAt, item.prepareStartedAt, TimeUnit.MILLISECONDS) + "ms");

        Scenario scenario = game.gameState.history.activeSave.scenario;

        for (OptionAudio audio : scenario.currentOption.option.audio) {
            if (audio != item) {
                if (audio.player != null) {
                    audio.player.stop();
                }
            }
        }

        float delta = Math.max(0, (item.progress - item.startsAt) / 1000);

        item.player.setPosition(delta);
        item.player.setVolume(GameState.isMute ? 0 : 1);
        item.player.play();
    }

    private void onImagePrepared(OptionImage item) {

        Log.i(tag, "onImagePrepared " + item.id
                + " in " + DateUtils.getDateDiff(item.prepareCompletedAt, item.prepareStartedAt, TimeUnit.MILLISECONDS) + "ms");

        backgroundTop.setDrawable(new SpriteDrawable(new Sprite(item.image)));

        float scale = 1f * MunhauzenGame.WORLD_WIDTH / item.image.getWidth();

        backgroundTop.setWidth(MunhauzenGame.WORLD_WIDTH);
        backgroundTop.setHeight(item.image.getHeight() * scale);
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

        if (assetManager != null) {
            assetManager.dispose();
        }
        if (ui != null) {
            ui.dispose();
        }
    }

    private Stack prepareProgressBar() {
        ProgressBar.ProgressBarStyle barStyle = new ProgressBar.ProgressBarStyle();
        barStyle.background = new NinePatchDrawable(new NinePatch(
                assetManager.get("ui/player_progress_bar_progress.9.jpg", Texture.class),
                10, 10, 0, 0
        ));
        barStyle.knob = new SpriteDrawable(new Sprite(
                assetManager.get("ui/player_progress_bar_knob.png", Texture.class)));
        bar = new ProgressBar(0, 100, 1, false, barStyle);

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

        return barContainer;
    }

    private Table prepareTitleActions() {
        ImageButton soundButton = new ImageButton(new SpriteDrawable(new Sprite(
                assetManager.get("GameScreen/b_booksound_off.png", Texture.class))));

        ImageButton menuButton = new ImageButton(new SpriteDrawable(new Sprite(
                assetManager.get("GameScreen/b_bookmenu.png", Texture.class))));

        Table actionsContainer = new Table();
        actionsContainer.add(soundButton).width(150).height(300);
        actionsContainer.add(menuButton).width(150).height(300);

        return actionsContainer;
    }

    public Actor prepareScenarioOptions(ArrayList<Decision> decisions) {
        final Stack stack = new Stack();
        stack.setFillParent(true);

        Table table = new Table();
        table.setFillParent(true);

        for (final Decision decision : decisions) {

            Option option = OptionRepository.find(game.gameState, decision.option);

            Button button = game.buttonBuilder.primary(option.text, new Runnable() {
                @Override
                public void run() {
                    uiLayers.removeActor(stack);

                    scenarioManager.createScenario(decision.option);
                }
            });

            table.add(button).width(600).height(50).pad(10).row();
        }

        ScrollPane scrollPane = new ScrollPane(table);
        scrollPane.setScrollingDisabled(true, false);

        Image decorLeft = new Image(assetManager.get("GameScreen/b_tulip_1.png", Texture.class));
        Image decorRight = new Image(assetManager.get("GameScreen/b_tulip_1.png", Texture.class));
        Image decorTop = new Image(assetManager.get("GameScreen/b_star_game.png", Texture.class));

        Table decoration = new Table();
        decoration.add(decorLeft).left().expandX();
        decoration.add(decorTop).top().expandX();
        decoration.add(decorRight).right().expandX();

        stack.addActorAt(0, decoration);
        stack.addActorAt(1, scrollPane);

        return stack;
    }
}
