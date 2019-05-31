package ua.gram.munhauzen.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.Timer;

import java.util.ArrayList;

import ua.gram.munhauzen.FontProvider;
import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.MunhauzenStage;
import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.entity.StoryAudio;
import ua.gram.munhauzen.entity.StoryImage;
import ua.gram.munhauzen.entity.Story;
import ua.gram.munhauzen.entity.StoryScenario;
import ua.gram.munhauzen.fragment.GameControlsFragment;
import ua.gram.munhauzen.fragment.ProgressBarFragment;
import ua.gram.munhauzen.fragment.ScenarioFragment;
import ua.gram.munhauzen.service.AudioService;
import ua.gram.munhauzen.service.ImageService;
import ua.gram.munhauzen.service.StoryManager;
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
    public StoryManager scenarioManager;
    public ScenarioFragment scenarioFragment;
    public ProgressBarFragment progressBarFragment;
    public GameControlsFragment gameControlsFragment;
    public AudioService audioService;
    public ImageService imageService;

    private Timer.Task saveTask;
    private Texture background;
    public Image layer1Image, layer2Image;
    public Table layer1ImageTable, layer2ImageTable;
    public Group layer1ImageGroup, layer2ImageGroup;
    public Image layer1OverlayTop, layer1OverlayBottom, layer2OverlayTop, layer2OverlayBottom;
    private boolean isLoaded;

    public GameScreen(MunhauzenGame game) {
        this.game = game;
        assetManager = new AssetManager();
    }

    public Story getStory() {
        return game.gameState.history.activeSave.story;
    }

    public void setStory(Story story) {
        game.gameState.history.activeSave.story = story;
    }

    @Override
    public void show() {

        Log.i(tag, "show");

        audioService = new AudioService(this);
        imageService = new ImageService(this);

        ui = new MunhauzenStage(game);
        scenarioManager = new StoryManager(this, game.gameState);

        isLoaded = false;
        GameState.isPaused = false;

        assetManager.load("GameScreen/t_putty.png", Texture.class);

        Story story = game.gameState.history.activeSave.story;
        if (story != null && story.isValid()) {
            scenarioManager.startLoadingResources(story);
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

        setStoryUILayer(uiControlsLayer);

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

        layer1OverlayBottom = new Image(overlay);
        layer1OverlayTop = new Image(overlay);
        layer2OverlayBottom = new Image(overlay);
        layer2OverlayTop = new Image(overlay);

        layer1OverlayTop.setVisible(false);
        layer1OverlayBottom.setVisible(false);
        layer2OverlayTop.setVisible(false);
        layer2OverlayBottom.setVisible(false);

        layer1Image = new FitImage();
        layer2Image = new FitImage();

        layer1ImageTable = new Table();
        layer1ImageTable.setFillParent(true);
        layer1ImageTable.add(layer1Image).center().expand().fill();

        layer2ImageTable = new Table();
        layer2ImageTable.setFillParent(true);
        layer2ImageTable.add(layer2Image).center().expand().fill();

        layer1ImageGroup = new Group();
        layer1ImageGroup.addActor(layer1ImageTable);
        layer1ImageGroup.addActor(layer1OverlayTop);
        layer1ImageGroup.addActor(layer1OverlayBottom);

        layer2ImageGroup = new Group();
        layer2ImageGroup.addActor(layer2ImageTable);
        layer2ImageGroup.addActor(layer2OverlayTop);
        layer2ImageGroup.addActor(layer2OverlayBottom);

        uiImageLayer.add(layer1ImageGroup);
        uiImageLayer.add(layer2ImageGroup);

        Gdx.input.setInputProcessor(ui);

        scenarioManager.resume();

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

    public void setStoryOptionsLayer(Stack actor) {
        uiLayers.addActorAt(1, actor);
    }

    public void setStoryUILayer(Stack actor) {
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

        Story story = getStory();

        if (!story.isCompleted) {

            if (!GameState.isPaused) {
                scenarioManager.update(
                        story.progress + (delta * 1000),
                        story.totalDuration
                );
            }

            if (story.isCompleted) {

                scenarioManager.onCompleted();

            } else {
                scenarioManager.startLoadingResources(story);
            }
        }

        progressBarFragment.update();

        audioService.updateVolume();

        audioService.updateMusicState();

        imageService.update();

        ui.act(delta);
        ui.draw();

        if (MunhauzenGame.DEBUG)
            drawDebugInfo();
    }

    private void drawDebugInfo() {

        Story story = getStory();

        int fontSize = FontProvider.h3;
        BitmapFont font = game.fontProvider.getFont(FontProvider.Arnold, fontSize);
        if (font != null) {

            font.setColor(Color.RED);


            ArrayList<String> strings = new ArrayList<>();
            strings.add("story:" + story.id);
            strings.add("progress:" + story.totalDuration + "/" + ((int) story.progress));

            for (StoryScenario scenarioOption : story.scenarios) {
                strings.add("-scenario:" + scenarioOption.scenario.name + "" + (scenarioOption.isLocked ? " lock" : ""));
                strings.add("--audios");

                for (StoryAudio item : scenarioOption.scenario.audio) {
                    strings.add("---audio:" + item.audio
                            + "" + (item.isPrepared ? " +" : " -")
                            + "" + (item.isActive ? " active" : "")
                            + "" + (item.isLocked ? " lock" : ""));
                }

                strings.add("--images");
                for (StoryImage item : scenarioOption.scenario.images) {
                    strings.add("---image:" + item.image
                            + "" + (item.isPrepared ? " +" : " -")
                            + "" + (item.isActive ? " active" : "")
                            + "" + (item.isLocked ? " lock" : ""));
                }
            }


            int offset = fontSize + 2;
            int row = -1;

            game.batch.begin();
            for (String string : strings) {
                font.draw(game.batch, string.replace("_", "-"), 10, MunhauzenGame.WORLD_HEIGHT - 10 - (++row) * offset);
            }
            game.batch.end();
        }
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
