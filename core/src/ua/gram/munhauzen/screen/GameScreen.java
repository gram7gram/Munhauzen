package ua.gram.munhauzen.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.Timer;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.MunhauzenStage;
import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.entity.Scenario;
import ua.gram.munhauzen.fragment.GameControlsFragment;
import ua.gram.munhauzen.fragment.ProgressBarFragment;
import ua.gram.munhauzen.fragment.ScenarioFragment;
import ua.gram.munhauzen.service.AudioService;
import ua.gram.munhauzen.service.ImageService;
import ua.gram.munhauzen.service.ScenarioManager;
import ua.gram.munhauzen.ui.FitImage;
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
    public AudioService audioService;
    public ImageService imageService;

    private Timer.Task saveTask;
    private Texture background;
    public Table currentImageTable;
    public Table overlayTableTop;
    public Table overlayTableBottom;
    public Image currentImage;
    public Image overlayTop;
    public Image overlayBottom;
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

        audioService = new AudioService(this);
        imageService = new ImageService(this);

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

        Scenario scenario = getScenario();

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
        }

        scenarioManager.postUpdate(scenario);

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

        audioService = null;
        imageService = null;

        progressBarFragment.dispose();
        scenarioFragment.dispose();
        gameControlsFragment.dispose();
    }
}
