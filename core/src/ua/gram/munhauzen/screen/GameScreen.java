package ua.gram.munhauzen.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.GdxRuntimeException;

import java.util.ArrayList;

import ua.gram.munhauzen.FontProvider;
import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.MunhauzenStage;
import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.entity.Image;
import ua.gram.munhauzen.entity.Save;
import ua.gram.munhauzen.entity.Story;
import ua.gram.munhauzen.entity.StoryAudio;
import ua.gram.munhauzen.entity.StoryImage;
import ua.gram.munhauzen.entity.StoryScenario;
import ua.gram.munhauzen.screen.game.fragment.ControlsFragment;
import ua.gram.munhauzen.screen.game.fragment.ImageFragment;
import ua.gram.munhauzen.screen.game.fragment.ProgressBarFragment;
import ua.gram.munhauzen.screen.game.fragment.ScenarioFragment;
import ua.gram.munhauzen.screen.game.fragment.VictoryFragment;
import ua.gram.munhauzen.screen.game.listener.StageInputListener;
import ua.gram.munhauzen.screen.game.ui.GameLayers;
import ua.gram.munhauzen.service.ExpansionImageService;
import ua.gram.munhauzen.service.GameAudioService;
import ua.gram.munhauzen.service.InteractionService;
import ua.gram.munhauzen.service.StoryManager;
import ua.gram.munhauzen.utils.ExpansionAssetManager;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class GameScreen implements Screen {

    private final String tag = getClass().getSimpleName();
    public final MunhauzenGame game;
    public MunhauzenStage ui;
    public GameLayers gameLayers;
    public AssetManager assetManager;
    public StoryManager storyManager;
    public ScenarioFragment scenarioFragment;
    public ProgressBarFragment progressBarFragment;
    public ImageFragment imageFragment;
    public ControlsFragment controlsFragment;
    public GameAudioService audioService;
    public ExpansionImageService imageService;
    public InteractionService interactionService;
    private Texture background;
    private boolean isLoaded;
    public StageInputListener stageInputListener;
    public VictoryFragment victoryFragment;

    public GameScreen(MunhauzenGame game) {
        this.game = game;
    }

    public Story getStory() {

        if (game.gameState == null) {
            throw new GdxRuntimeException("Game state was not loaded");
        }

        if (game.gameState.history == null) {
            throw new GdxRuntimeException("History was not loaded");
        }

        if (game.gameState.activeSave == null) {
            throw new GdxRuntimeException("Save was not loaded");
        }

        return game.gameState.activeSave.story;
    }

    public void setStory(Story story) {

        if (game.gameState == null) {
            throw new GdxRuntimeException("Game state was not loaded");
        }

        if (game.gameState.history == null) {
            throw new GdxRuntimeException("History was not loaded");
        }

        if (game.gameState.activeSave == null) {
            throw new GdxRuntimeException("Save was not loaded");
        }

        game.gameState.activeSave.story = story;
    }

    @Override
    public void show() {

        Log.i(tag, "show");

        isLoaded = false;

        background = game.assetManager.get("p0.jpg", Texture.class);

        assetManager = new ExpansionAssetManager();
        progressBarFragment = new ProgressBarFragment(this);

        audioService = new GameAudioService(this);
        imageService = new ExpansionImageService(this);
        interactionService = new InteractionService(this);

        ui = new MunhauzenStage(game);
        storyManager = new StoryManager(this, game.gameState);
        stageInputListener = new StageInputListener(this);

        isLoaded = false;
        GameState.unpause(tag);

        game.databaseManager.loadExternal(game.gameState);

        assetManager.load("GameScreen/t_putty.png", Texture.class);
        assetManager.load("ui/playbar_pause.png", Texture.class);
        assetManager.load("ui/playbar_play.png", Texture.class);

        assetManager.load("ui/playbar_rewind_backward.png", Texture.class);
        assetManager.load("ui/playbar_rewind_backward_off.png", Texture.class);

        assetManager.load("ui/playbar_rewind_forward.png", Texture.class);
        assetManager.load("ui/playbar_rewind_forward_off.png", Texture.class);

        assetManager.load("ui/playbar_skip_backward.png", Texture.class);
        assetManager.load("ui/playbar_skip_backward_off.png", Texture.class);

        assetManager.load("ui/playbar_skip_forward.png", Texture.class);
        assetManager.load("ui/playbar_skip_forward_off.png", Texture.class);

        assetManager.load("ui/elements_player_fond_1.png", Texture.class);
        assetManager.load("ui/elements_player_fond_2.png", Texture.class);
        assetManager.load("ui/elements_player_fond_3.png", Texture.class);
        assetManager.load("ui/player_progress_bar_progress.9.jpg", Texture.class);
        assetManager.load("ui/player_progress_bar_knob.png", Texture.class);
    }

    private void onResourcesLoaded() {

        Log.i(tag, "onResourcesLoaded");

        isLoaded = true;

        gameLayers = new GameLayers(this);

        ui.addActor(gameLayers);

        progressBarFragment.create();

        controlsFragment = new ControlsFragment(this);
        controlsFragment.create();

        imageFragment = new ImageFragment(this);
        imageFragment.create();

        gameLayers.setBackgroundImageLayer(imageFragment);
        gameLayers.setProgressBarLayer(progressBarFragment);
        gameLayers.setControlsLayer(controlsFragment);

        Gdx.input.setInputProcessor(ui);

        storyManager.resume();

        ui.addListener(stageInputListener);
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        if (assetManager == null) return;

        drawBackground();

        assetManager.update();

        if (!isLoaded) {

            if (assetManager.isFinished()) {
                onResourcesLoaded();
            }
            return;
        }

        if (imageService != null) {
            imageService.update();
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

                        game.achievementService.onScenarioVisited(story.currentScenario.scenario);

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

        if (victoryFragment != null) {
            victoryFragment.update();
        }

        if (ui != null) {
            ui.act(delta);
        }

        if (ui != null) {
            ui.draw();
        }

        if (MunhauzenGame.DEBUG_RENDER_INFO)
            drawDebugInfo();

    }

    private void drawDebugInfo() {

        Story story = getStory();
        if (story == null) return;

        int fontSize = FontProvider.p;
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
                    for (StoryAudio audio : scenarioOption.scenario.audio) {
                        strings.add("--audio:" + audio.audio
                                + "" + (audio.isLocked ? " locked" : "")
                                + "" + (audio.isActive ? " active" : "")
                        );
                    }
                    for (StoryImage image : scenarioOption.scenario.images) {
                        strings.add("--image:" + image.image
                                + "" + (image.isLocked ? " locked" : "")
                                + "" + (image.isActive ? " active" : "")
                        );
                    }
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
        if (game.batch == null || background == null) return;

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

        Story story = getStory();
        if (story != null) {
            if (story.currentInteraction != null) {
                story.currentInteraction.interaction.dispose();
            }
        }

        if (storyManager != null) {
            storyManager.dispose();
            storyManager = null;
        }

        isLoaded = false;

        if (assetManager != null) {
            assetManager.dispose();
            assetManager = null;
        }

        if (ui != null) {
            ui.dispose();
            ui = null;
        }

        if (audioService != null) {
            audioService.dispose();
            audioService = null;
        }

        if (imageService != null) {
            imageService.dispose();
            imageService = null;
        }

        if (progressBarFragment != null) {
            progressBarFragment.dispose();
            progressBarFragment = null;
        }

        if (scenarioFragment != null) {
            scenarioFragment.dispose();
            scenarioFragment = null;
        }

        if (controlsFragment != null) {
            controlsFragment.dispose();
            controlsFragment = null;
        }

        if (victoryFragment != null) {
            victoryFragment.destroy();
            victoryFragment = null;
        }

        if (gameLayers != null) {
            gameLayers.dispose();
            gameLayers = null;
        }

        background = null;

        setStory(null);
    }

    public void restoreProgressBarIfDestroyed() {

        Story story = getStory();
        if (story == null) return;

        if (story.isInteraction()) return;

        Log.i(tag, "restoreProgressBarIfDestroyed");

        showProgressBar();
    }

    public void showProgressBar() {

        if (progressBarFragment == null) return;

        Log.i(tag, "showProgressBar");
        try {

            if (!progressBarFragment.isFadeIn) {
                progressBarFragment.fadeIn();
            }
        } catch (Throwable e) {
            Log.e(tag, e);
        }

    }

    public void hideProgressBar() {

        if (progressBarFragment == null) return;

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
            imageFragment.backgroundBottomImage.setVisible(false);
            imageFragment.backgroundTopImage.setVisible(false);
        }
    }

    public void showImageFragment() {

        if (imageFragment != null) {
            imageFragment.backgroundBottomImage.setVisible(true);
            imageFragment.backgroundTopImage.setVisible(true);
        }
    }

    public void hideAndDestroyScenarioFragment() {
        if (gameLayers != null) {
            gameLayers.setStoryDecisionsLayer(null);
        }
        scenarioFragment = null;
    }

    public void onCriticalError(Throwable e) {
        game.onCriticalError(e);
        dispose();
    }

    public void setLastBackground(String file) {

        String[] path = file.split("/");

        String name = path[path.length - 1].split(".")[0];

        Image image = new Image();
        image.name = name;
        image.file = file;

        setLastBackground(image);
    }

    public void setLastBackground(Image image) {

        getActiveSave().lastImage = image;

        game.achievementService.onImageViewed(image);
    }

    public Image getLastBackground() {
        return getActiveSave().lastImage;
    }

    public Save getActiveSave() {
        return game.gameState.activeSave;
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
