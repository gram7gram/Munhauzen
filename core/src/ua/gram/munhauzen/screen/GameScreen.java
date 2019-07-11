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
import com.badlogic.gdx.utils.Timer;

import java.util.ArrayList;

import ua.gram.munhauzen.FontProvider;
import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.MunhauzenStage;
import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.entity.Story;
import ua.gram.munhauzen.entity.StoryInteraction;
import ua.gram.munhauzen.entity.StoryScenario;
import ua.gram.munhauzen.fragment.GameControlsFragment;
import ua.gram.munhauzen.fragment.ImageFragment;
import ua.gram.munhauzen.fragment.ProgressBarFragment;
import ua.gram.munhauzen.fragment.ScenarioFragment;
import ua.gram.munhauzen.interaction.ContinueInteraction;
import ua.gram.munhauzen.interaction.GeneralsInteraction;
import ua.gram.munhauzen.interaction.HareInteraction;
import ua.gram.munhauzen.interaction.TimerInteraction;
import ua.gram.munhauzen.interaction.generals.fragment.GeneralsProgressBarFragment;
import ua.gram.munhauzen.interaction.hare.fragment.HareProgressBarFragment;
import ua.gram.munhauzen.interaction.timer.fragment.TimerProgressBarFragment;
import ua.gram.munhauzen.service.AudioService;
import ua.gram.munhauzen.service.ExternalImageService;
import ua.gram.munhauzen.service.InteractionService;
import ua.gram.munhauzen.service.InternalImageService;
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
    public final ProgressBarFragment progressBarFragment;
    public ImageFragment imageFragment;
    public GameControlsFragment gameControlsFragment;
    public AudioService audioService;
    public ExternalImageService externalImageService;
    public InternalImageService internalImageService;
    public InteractionService interactionService;
    private Timer.Task saveTask;
    private Texture background;
    private boolean isLoaded;

    public GameScreen(MunhauzenGame game) {
        this.game = game;
        assetManager = new AssetManager();
        progressBarFragment = new ProgressBarFragment(this);
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
        externalImageService = new ExternalImageService(this);
        internalImageService = new InternalImageService(this);
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

        background = game.assetManager.get("p0.jpg", Texture.class);

        gameLayers = new GameLayers(this);

        ui.addActor(gameLayers);

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
//                Json json = new Json();
//                json.setOutputType(JsonWriter.OutputType.json);

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

                if (event.isHandled()) return;

                try {
                    Log.i(tag, "ui clicked");

                    StoryInteraction storyInteraction = getStory().currentInteraction;

                    if (storyInteraction != null) {
                        if (storyInteraction.interaction instanceof ContinueInteraction) {
                            ContinueInteraction continueInteraction = (ContinueInteraction) storyInteraction.interaction;

                            if (continueInteraction.root != null) {
                                if (!continueInteraction.root.isVisible()) {
                                    if (!continueInteraction.isFadeIn) {
                                        continueInteraction.fadeIn();
                                    }
                                } else {
                                    if (!continueInteraction.isFadeOut) {
                                        continueInteraction.fadeOut();
                                    }
                                }
                            }
                        }

                        if (storyInteraction.interaction instanceof HareInteraction) {
                            HareProgressBarFragment barFragment = ((HareInteraction) storyInteraction.interaction).progressBarFragment;

                            if (barFragment != null) {
                                if (!barFragment.getRoot().isVisible()) {
                                    if (!barFragment.isFadeIn) {
                                        barFragment.fadeIn();
                                        barFragment.scheduleFadeOut();
                                    }
                                } else {
                                    if (!barFragment.isFadeOut) {
                                        barFragment.fadeOut();
                                    }
                                }
                            }
                        }

                        if (storyInteraction.interaction instanceof GeneralsInteraction) {
                            GeneralsProgressBarFragment barFragment = ((GeneralsInteraction) storyInteraction.interaction).progressBarFragment;

                            if (barFragment != null) {
                                if (!barFragment.getRoot().isVisible()) {
                                    if (!barFragment.isFadeIn) {
                                        barFragment.fadeIn();
                                        barFragment.scheduleFadeOut();
                                    }
                                } else {
                                    if (!barFragment.isFadeOut) {
                                        barFragment.fadeOut();
                                    }
                                }
                            }
                        }

                        if (storyInteraction.interaction instanceof TimerInteraction) {
                            TimerProgressBarFragment barFragment = ((TimerInteraction) storyInteraction.interaction).progressBarFragment;

                            if (barFragment != null) {
                                if (!barFragment.getRoot().isVisible()) {
                                    if (!barFragment.isFadeIn) {
                                        barFragment.fadeIn();
                                        barFragment.scheduleFadeOut();
                                    }
                                } else {
                                    if (!barFragment.isFadeOut) {
                                        barFragment.fadeOut();
                                    }
                                }
                            }
                        }
                    }

                    if (storyInteraction == null || storyInteraction.interaction instanceof ContinueInteraction) {

                        if (!progressBarFragment.getRoot().isVisible()) {
                            showProgressBar();
                        } else {
                            hideProgressBar();
                        }
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

        if (externalImageService != null) {
            externalImageService.update();
        }

        if (internalImageService != null) {
            internalImageService.update();
        }

        if (imageFragment != null) {
            imageFragment.update();
        }

        if (audioService != null) {
            audioService.update();
        }

        if (interactionService != null) {
            interactionService.update();
        }

        Story story = getStory();
        if (story != null) {

            boolean isInteractionLocked = story.isInteractionLocked();

            if (!isInteractionLocked && !story.isCompleted) {

                if (!GameState.isPaused) {
                    storyManager.update(
                            story.progress + (delta * 1000),
                            story.totalDuration
                    );

                    if (story.isValid()) {
                        if (story.isCompleted) {

                            storyManager.onCompleted();

                        } else {
                            storyManager.startLoadingResources();
                        }
                    }
                }
            }

            if (progressBarFragment != null) {
                progressBarFragment.update();
            } else if (!isInteractionLocked) {
                restoreProgressBarIfDestroyed();
            }
        }

        if (audioService != null) {
            audioService.updateVolume();

            audioService.updateMusicState();
        }

        if (ui != null) {
            ui.act(delta);
            ui.draw();
        }

        if (MunhauzenGame.DEBUG_RENDER_INFO)
            drawDebugInfo();

    }

    private void drawDebugInfo() {

        Story story = getStory();

        int fontSize = FontProvider.h4;
        BitmapFont font = game.fontProvider.getFont(FontProvider.DroidSansMono, fontSize);
        if (font != null) {

            font.setColor(Color.BLUE);

            ArrayList<String> strings = new ArrayList<>();

            if (story.currentInteraction != null && !story.currentInteraction.isCompleted) {
                strings.add("interaction:" + story.currentInteraction.name + "" + (story.currentInteraction.isLocked ? " lock" : ""));
            } else {

                strings.add("duration:" + story.totalDuration + "ms");
                strings.add("progress:" + ((int) story.progress) + "ms");

                for (StoryScenario scenarioOption : story.scenarios) {
                    strings.add("-scenario:" + scenarioOption.scenario.name
                            + "" + (scenarioOption.scenario.interaction != null ? " (" + scenarioOption.scenario.interaction + ")" : "")
                            + "" + (scenarioOption.isLocked ? " lock" : "")
                    );
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
            ui = null;
        }

        if (audioService != null) {
            audioService.dispose();
            audioService = null;
        }

        if (externalImageService != null) {
            externalImageService.dispose();
            externalImageService = null;
        }

        if (internalImageService != null) {
            internalImageService.dispose();
            internalImageService = null;
        }

        progressBarFragment.dispose();

        if (scenarioFragment != null) {
            scenarioFragment.dispose();
            scenarioFragment = null;
        }

        if (gameControlsFragment != null) {
            gameControlsFragment.dispose();
            gameControlsFragment = null;
        }

        if (gameLayers != null) {
            gameLayers.dispose();
            gameLayers = null;
        }

        storyManager = null;
        audioService = null;
        externalImageService = null;
        interactionService = null;
        internalImageService = null;
        background = null;

        setStory(null);
    }

    public void restoreProgressBarIfDestroyed() {

        Log.i(tag, "restoreProgressBarIfDestroyed");

        showProgressBar();
    }

    public void showProgressBar() {

        Log.i(tag, "showProgressBar");
        try {

            if (!progressBarFragment.isFadeIn) {
                progressBarFragment.fadeIn();
                progressBarFragment.scheduleFadeOut();
            }
        } catch (Throwable e) {
            Log.e(tag, e);
        }

    }

    public void hideProgressBar() {

        Log.i(tag, "hideProgressBar");

        try {
            if (progressBarFragment.canFadeOut()) {
                progressBarFragment.fadeOut();
            }
        } catch (Throwable e) {
            Log.e(tag, e);
        }

    }

    public void hideImageFragment() {

        if (imageFragment != null) {
            imageFragment.layer1ImageGroup.setVisible(false);
            imageFragment.layer2ImageGroup.setVisible(false);
        }
    }

    public void hideAndDestroyScenarioFragment() {
        if (scenarioFragment != null) {
            scenarioFragment.fadeOut(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (scenarioFragment != null) {
                            scenarioFragment.destroy();
                            scenarioFragment = null;
                        }
                    } catch (Throwable e) {
                        Log.e(tag, e);
                    }
                }
            });
        }
    }
}
