package ua.gram.munhauzen.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.Timer;

import java.util.ArrayList;

import ua.gram.munhauzen.FontProvider;
import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.MunhauzenStage;
import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.entity.Story;
import ua.gram.munhauzen.entity.StoryAudio;
import ua.gram.munhauzen.entity.StoryImage;
import ua.gram.munhauzen.entity.StoryScenario;
import ua.gram.munhauzen.fragment.GameControlsFragment;
import ua.gram.munhauzen.fragment.ImageFragment;
import ua.gram.munhauzen.fragment.ProgressBarFragment;
import ua.gram.munhauzen.fragment.ScenarioFragment;
import ua.gram.munhauzen.service.AudioService;
import ua.gram.munhauzen.service.ImageService;
import ua.gram.munhauzen.service.InventoryService;
import ua.gram.munhauzen.service.StoryManager;
import ua.gram.munhauzen.ui.GameLayers;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class GameScreen implements Screen {

    private final String tag = getClass().getSimpleName();
    public final MunhauzenGame game;
    public MunhauzenStage ui;
    public GameLayers gameLayers;
    public final AssetManager assetManager;
    public StoryManager storyManager;
    public ScenarioFragment scenarioFragment;
    public ProgressBarFragment progressBarFragment;
    public ImageFragment imageFragment;
    public GameControlsFragment gameControlsFragment;
    public AudioService audioService;
    public ImageService imageService;
    public InventoryService inventoryService;
    public InteractionService interactionService;
    private Timer.Task saveTask;
    private Texture background;
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
        inventoryService = new InventoryService(game.gameState);
        interactionService = new InteractionService(this);

        ui = new MunhauzenStage(game);
        storyManager = new StoryManager(this, game.gameState);

        isLoaded = false;
        GameState.isPaused = false;

        assetManager.load("GameScreen/t_putty.png", Texture.class);

        Story story = game.gameState.history.activeSave.story;
        if (story != null && story.isValid()) {
            storyManager.startLoadingResources();
        }
    }

    private void onResourcesLoaded() {

        Log.i(tag, "onResourcesLoaded");

        isLoaded = true;

        background = game.assetManager.get("a0.jpg", Texture.class);

        gameLayers = new GameLayers(this);
        gameLayers.setFillParent(true);

        ui.addActor(gameLayers);

        progressBarFragment = new ProgressBarFragment(this);
        progressBarFragment.create();

        gameControlsFragment = new GameControlsFragment(this);
        gameControlsFragment.create();

        imageFragment = new ImageFragment(this);
        imageFragment.create();

        gameLayers.setBackgroundImageLayer(imageFragment);
        gameLayers.setProgressBarLayer(progressBarFragment);
        gameLayers.setControlsLayer(gameControlsFragment);

        Gdx.input.setInputProcessor(ui);

        storyManager.resume();

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

        ui.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                try {
                    Log.i(tag, "ui clicked");

                    if (progressBarFragment == null) return;

                    if (progressBarFragment.getRoot() == null) {
                        progressBarFragment.destroy();
                        progressBarFragment = null;
                        return;
                    }

                    progressBarFragment.fadeIn();

                    if (scenarioFragment == null) {
                        progressBarFragment.scheduleFadeOut();
                    }
                } catch (Throwable e) {
                    Log.e(tag, e);
                }

            }
        });
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

        interactionService.update();

        Story story = getStory();

        boolean isInteractionLocked = story.currentInteraction != null && story.currentInteraction.isLocked;

        if (!isInteractionLocked && !story.isCompleted) {

            if (!GameState.isPaused) {
                storyManager.update(
                        story.progress + (delta * 1000),
                        story.totalDuration
                );

                if (story.isCompleted) {

                    storyManager.onCompleted();

                } else {
                    storyManager.startLoadingResources();
                }
            }
        }

        if (progressBarFragment != null)
            progressBarFragment.update();

        if (audioService != null) {
            audioService.updateVolume();

            audioService.updateMusicState();
        }

        if (imageService != null) {
            imageService.update();
        }

        ui.act(delta);
        ui.draw();

        if (MunhauzenGame.DEBUG)
            drawDebugInfo();
    }

    private void drawDebugInfo() {

        Story story = getStory();

        int fontSize = FontProvider.h4;
        BitmapFont font = game.fontProvider.getFont(FontProvider.BuxtonSketch, fontSize);
        if (font != null) {

            font.setColor(Color.RED);


            ArrayList<String> strings = new ArrayList<>();
            strings.add("story:" + story.id);
            strings.add("progress:" + story.totalDuration + "/" + ((int) story.progress));

            if (story.currentInteraction != null) {
                strings.add("interaction:" + story.currentInteraction.name + "" + (story.currentInteraction.isLocked ? " lock" : ""));
            }

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


            int offset = fontSize + 1;
            int row = -1;

            game.batch.begin();
            for (String string : strings) {
                font.draw(game.batch, string, 10, MunhauzenGame.WORLD_HEIGHT - 10 - (++row) * offset);
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

        if (progressBarFragment != null) {
            progressBarFragment.dispose();
            progressBarFragment = null;
        }

        if (scenarioFragment != null) {
            scenarioFragment.dispose();
            scenarioFragment = null;
        }

        if (gameControlsFragment != null) {
            gameControlsFragment.dispose();
            gameControlsFragment = null;
        }

        gameLayers = null;
    }
}
