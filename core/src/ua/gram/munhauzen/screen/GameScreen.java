package ua.gram.munhauzen.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.Timer;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.MunhauzenStage;
import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.entity.OptionAudio;
import ua.gram.munhauzen.entity.OptionImage;
import ua.gram.munhauzen.entity.Scenario;
import ua.gram.munhauzen.fragment.GameControlsFragment;
import ua.gram.munhauzen.fragment.ProgressBarFragment;
import ua.gram.munhauzen.fragment.ScenarioFragment;
import ua.gram.munhauzen.service.ScenarioManager;
import ua.gram.munhauzen.ui.FitImage;
import ua.gram.munhauzen.utils.DateUtils;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class GameScreen implements Screen {

    private final String tag = getClass().getSimpleName();
    public final MunhauzenGame game;
    public MunhauzenStage ui;
    public Stack uiLayers;
    public Stack uiControlsLayer;
    public Stack uiImageLayer;
    public final AssetManager assetManager;
    public ScenarioManager scenarioManager;
    public ScenarioFragment scenarioFragment;
    public ProgressBarFragment progressBarFragment;
    public GameControlsFragment gameControlsFragment;

    private Timer.Task saveTask;
    private Texture background;
    private Table currentImageTable, overlayTableTop, overlayTableBottom;
    private Image currentImage, overlayTop, overlayBottom;
    private boolean isLoaded;

    public GameScreen(MunhauzenGame game) {
        this.game = game;
        assetManager = new AssetManager();
    }

    public Scenario getScenario() {
        return game.gameState.history.activeSave.scenario;
    }

    @Override
    public void show() {

        Log.i(tag, "show");

        ui = new MunhauzenStage(game);
        scenarioManager = new ScenarioManager(this, game.gameState);

        isLoaded = false;
        GameState.isPaused = false;

        assetManager.load("GameScreen/t_putty.png", Texture.class);

        Scenario scenario = game.gameState.history.activeSave.scenario;
        if (scenario != null && scenario.isValid()) {
            scenarioManager.startLoadingResources(scenario);
        }
    }

    private void onResourcesLoaded() {

        Log.i(tag, "onResourcesLoaded");

        isLoaded = true;

        uiLayers = new Stack();
        uiLayers.setFillParent(true);

        uiControlsLayer = new Stack();
        uiControlsLayer.setFillParent(true);

        uiImageLayer = new Stack();
        uiImageLayer.setFillParent(true);

        setBackgroundImageLayer(uiImageLayer);

        setScenarioUILayer(uiControlsLayer);

        ui.addActor(uiLayers);

        background = game.assetManager.get("a0.jpg", Texture.class);
        Texture overlay = assetManager.get("GameScreen/t_putty.png", Texture.class);

        progressBarFragment = new ProgressBarFragment(this);
        Stack barContainer = progressBarFragment.create();

        gameControlsFragment = new GameControlsFragment(this);
        gameControlsFragment.create();

        Table scenarioContainer = new Table();
        scenarioContainer.setFillParent(true);
        scenarioContainer.add(barContainer).align(Align.bottom).fillX().expand().row();

        uiControlsLayer.add(scenarioContainer);

        overlayBottom = new Image(overlay);
        overlayTop = new Image(overlay);

        overlayTop.setVisible(false);
        overlayBottom.setVisible(false);

        currentImage = new FitImage();

        overlayTableTop = new Table();
        overlayTableTop.add(overlayTop).expandX().fillX();

        overlayTableBottom = new Table();
        overlayTableBottom.add(overlayBottom).expandX().fillX();

        currentImageTable = new Table();
        currentImageTable.setFillParent(true);
        currentImageTable.add(currentImage).center().expand().fill();

        uiImageLayer.add(currentImageTable);
        uiImageLayer.add(overlayTableTop);
        uiImageLayer.add(overlayTableBottom);

        Gdx.input.setInputProcessor(ui);

        scenarioManager.resumeScenario();

        saveTask = Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                Json json = new Json();
                json.setOutputType(JsonWriter.OutputType.json);

//                try {
//                    String content = json.prettyPrint(game.gameState.history);
//
//                    FileHandle history = Files.getHistoryFile();
//
//                    history.writeString(content, false, "UTF-8");

//                    Log.i(tag, "History saved to: " + history.path());
//                } catch (Throwable e) {
//                    Log.e(tag, e);
//                }
            }
        }, 5, 5);
    }

    public void setBackgroundImageLayer(Stack actor) {
        uiLayers.addActorAt(0, actor);
    }

    public void setScenarioOptionsLayer(Stack actor) {
        uiLayers.addActorAt(1, actor);
    }

    public void setScenarioUILayer(Stack actor) {
        uiLayers.addActorAt(2, actor);
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        assetManager.update();

        if (!isLoaded) {
            if (assetManager.isFinished()) {
                onResourcesLoaded();
            }
            return;
        }

        drawBackground();

        Scenario scenario = game.gameState.history.activeSave.scenario;

        if (!scenario.isCompleted) {

            if (!GameState.isPaused) {
                scenarioManager.updateScenario(
                        scenario.progress + (delta * 1000),
                        scenario.totalDuration
                );
            }

            if (scenario.isCompleted) {

                scenarioManager.onScenarioCompleted();

            } else {
                scenarioManager.startLoadingResources(scenario);
            }

            if (scenario.currentOption.currentAudio.isPrepared) {
                Music player = scenario.currentOption.currentAudio.player;
                if (player != null) {
                    if (GameState.isPaused) {
                        player.pause();
                    } else if (!player.isPlaying()) {
                        player.play();
                    }
                }
            }
        }

        progressBarFragment.update();

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

    public void prepareAudio(OptionAudio item) {
        if (item.isPrepared) return;

        String resource = item.getResource();

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

    public void prepareImage(OptionImage item) {
        if (item.isPrepared) return;

        String resource = item.getResource();

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

        if (!GameState.isPaused) {
            item.player.play();
        }
    }

    private void onImagePrepared(OptionImage item) {

        Log.i(tag, "onImagePrepared " + item.id
                + " " + item.image.getWidth() + "x" + item.image.getHeight()
                + " (" + MunhauzenGame.WORLD_WIDTH + "x" + MunhauzenGame.WORLD_HEIGHT + ")"
                + " in " + DateUtils.getDateDiff(item.prepareCompletedAt, item.prepareStartedAt, TimeUnit.MILLISECONDS) + "ms");

        currentImage.setDrawable(new SpriteDrawable(new Sprite(item.image)));

        float scale = 1f * MunhauzenGame.WORLD_WIDTH / item.image.getWidth();
        float height = 1f * item.image.getHeight() * scale;

        currentImageTable.getCell(currentImage).width(MunhauzenGame.WORLD_WIDTH).height(height);

        Log.i(tag, "currentImage " + currentImage.getWidth() + "x" + currentImage.getHeight());


        boolean isOverlayVisible = currentImage.getHeight() < MunhauzenGame.WORLD_HEIGHT;
        overlayTop.setVisible(isOverlayVisible);
        overlayBottom.setVisible(isOverlayVisible);

        if (isOverlayVisible) {

            overlayTableBottom.getCell(overlayBottom).width(MunhauzenGame.WORLD_WIDTH).height(150);
            overlayTableTop.getCell(overlayTop).width(MunhauzenGame.WORLD_WIDTH).height(150);

            overlayTop.setPosition(0, currentImage.getY() - overlayTop.getHeight() / 2f);
            overlayBottom.setPosition(0, currentImage.getY() + currentImage.getHeight() - overlayTop.getHeight() / 2f);
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

        Log.i(tag, "dispose");

        if (saveTask != null) {
            saveTask.cancel();
            saveTask = null;
        }

        isLoaded = false;

        assetManager.dispose();

        if (ui != null) {
            ui.dispose();
        }

        progressBarFragment.dispose();
        scenarioFragment.dispose();
        gameControlsFragment.dispose();
    }
}
