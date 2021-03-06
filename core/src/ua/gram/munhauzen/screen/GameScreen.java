package ua.gram.munhauzen.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Timer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Stack;

import ua.gram.munhauzen.GameLayerInterface;
import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.Chapter;
import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.entity.Image;
import ua.gram.munhauzen.entity.Inventory;
import ua.gram.munhauzen.entity.PurchaseState;
import ua.gram.munhauzen.entity.Save;
import ua.gram.munhauzen.entity.Story;
import ua.gram.munhauzen.interaction.ServantsInteraction;
import ua.gram.munhauzen.interaction.servants.fragment.ServantsFireImageFragment;
import ua.gram.munhauzen.interfaces.DownloadSuccessFailureListener;
import ua.gram.munhauzen.repository.ChapterRepository;
import ua.gram.munhauzen.repository.InventoryRepository;
import ua.gram.munhauzen.screen.game.fragment.AchievementFragment;
import ua.gram.munhauzen.screen.game.fragment.ControlsFragment;
import ua.gram.munhauzen.screen.game.fragment.GameProgressBarFragment;
import ua.gram.munhauzen.screen.game.fragment.GameScenarioFragment;
import ua.gram.munhauzen.screen.game.fragment.ImageFragment;
import ua.gram.munhauzen.screen.game.fragment.PurchaseFragment;
import ua.gram.munhauzen.screen.game.fragment.VictoryFragment;
import ua.gram.munhauzen.screen.game.listener.StageInputListener;
import ua.gram.munhauzen.screen.game.ui.GameLayers;
import ua.gram.munhauzen.service.ExpansionImageService;
import ua.gram.munhauzen.service.GameAudioService;
import ua.gram.munhauzen.service.InteractionService;
import ua.gram.munhauzen.service.StoryManager;
import ua.gram.munhauzen.ui.MunhauzenStage;
import ua.gram.munhauzen.utils.DateUtils;
import ua.gram.munhauzen.utils.ExpansionAssetManager;
import ua.gram.munhauzen.utils.Files;
import ua.gram.munhauzen.utils.InternalAssetManager;
import ua.gram.munhauzen.utils.Log;

public class GameScreen extends MunhauzenScreen {

    public MunhauzenStage ui;
    public GameLayers gameLayers;
    public InternalAssetManager internalAssetManager;
    public ExpansionAssetManager assetManager;
    public StoryManager storyManager;
    public GameScenarioFragment scenarioFragment;
    public GameProgressBarFragment progressBarFragment;
    public ImageFragment imageFragment;
    public ControlsFragment controlsFragment;
    public GameAudioService audioService;
    public ExpansionImageService imageService;
    public InteractionService interactionService;
    private Texture background;
    private boolean isLoaded, isBackPressed;
    public StageInputListener stageInputListener;
    public VictoryFragment victoryFragment;
    public PurchaseFragment purchaseFragment;
    public AchievementFragment achievementFragment;
    Timer.Task persistTask, internetTask;

    public GameScreen(MunhauzenGame game) {
        super(game);
    }

    @Override
    public GameLayerInterface getLayers() {
        return gameLayers;
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
        game.gameState.activeSave.story = story;
    }

    @Override
    public void show() {

        Log.i(tag, "show");

        game.camera.zoom = 1;

        if (game.backgroundSfxService != null) {
            game.backgroundSfxService.stop();
        }

        GameState.isEndingReached = false;

        isBackPressed = false;
        isLoaded = false;

        background = game.internalAssetManager.get("p0.jpg", Texture.class);

        internalAssetManager = new InternalAssetManager();
        assetManager = new ExpansionAssetManager(game);
        progressBarFragment = new GameProgressBarFragment(this);

        audioService = new GameAudioService(this);
        imageService = new ExpansionImageService(this);
        interactionService = new InteractionService(this);

        ui = new MunhauzenStage(game);
        storyManager = new StoryManager(this, game.gameState);
        stageInputListener = new StageInputListener(this);
        gameLayers = new GameLayers(this);

        destroyBanners();

        isLoaded = false;
        GameState.unpause(tag);

        game.databaseManager.loadExternal(game.gameState);

        internalAssetManager.load("GameScreen/t_putty.png", Texture.class);
        internalAssetManager.load("ui/playbar_pause.png", Texture.class);
        internalAssetManager.load("ui/playbar_play.png", Texture.class);

        internalAssetManager.load("ui/playbar_rewind_backward.png", Texture.class);
        internalAssetManager.load("ui/playbar_rewind_forward.png", Texture.class);
        internalAssetManager.load("ui/playbar_skip_backward.png", Texture.class);
        internalAssetManager.load("ui/playbar_skip_forward.png", Texture.class);

        internalAssetManager.load("ui/elements_player_fond_1.png", Texture.class);
        internalAssetManager.load("ui/elements_player_fond_2.png", Texture.class);
        internalAssetManager.load("ui/elements_player_fond_3.png", Texture.class);
        internalAssetManager.load("ui/player_progress_bar_progress.9.jpg", Texture.class);
        internalAssetManager.load("ui/player_progress_bar_knob.png", Texture.class);

        persistTask = new Timer.Task() {
            @Override
            public void run() {
                try {

                    if (game.databaseManager != null && game.gameState != null) {
                        game.databaseManager.persistSync(game.gameState);
                    }

                } catch (Throwable ignore) {
                }
            }
        };

        internetTask = new Timer.Task() {
            @Override
            public void run() {
                try {
                    Story story = getStory();

                    if (story != null && story.currentScenario != null && story.currentInteraction == null) {
                        if (game.isOnlineMode()) {
                            Chapter chapter = ChapterRepository.find(game.gameState, story.currentScenario.scenario.chapter);

                            if (game.canUseIntenetForChapter(chapter.name)) {

                                if (!MunhauzenGame.internetListenter.hasInternet()) {
                                    if (noInternetFragment == null) {
                                        Log.i(tag, "No internet checker => openNoInternetBanner");
                                        openNoInternetBanner(new Runnable() {
                                            @Override
                                            public void run() {
                                                destroyBanners();
                                                navigateTo(new GameScreen(game));
                                            }
                                        });
                                    }
                                }
                            }
                        }
                    }
                } catch (Throwable ignore) {
                }
            }
        };
    }

    private void onResourcesLoaded() {

        Log.i(tag, "onResourcesLoaded");

        try {

            isLoaded = true;

            ui.addActor(gameLayers);

            progressBarFragment.create();

            controlsFragment = new ControlsFragment(this);
            controlsFragment.create();

            imageFragment = new ImageFragment(this);
            imageFragment.create();

            gameLayers.setBackgroundImageLayer(imageFragment);
            gameLayers.setProgressBarLayer(progressBarFragment);

            Gdx.input.setInputProcessor(ui);

            storyManager.resume();

            ui.addListener(stageInputListener);

            destroyBanners();
            GameState.unpause(tag);

            isChapterDownloaded();

        } catch (Throwable e) {
            Log.e(tag, e);

            onCriticalError(e);
        }
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void render(float delta) {

        try {
            Gdx.gl.glClearColor(235 / 255f, 232 / 255f, 112 / 255f, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

            if (ui == null) return;

            checkBackPressed();

            if (internalAssetManager == null) return;
            if (assetManager == null) return;

            drawBackground();

            try {
                internalAssetManager.update();
            } catch (Throwable ignore) {
            }

            try {
                assetManager.update();
            } catch (Throwable ignore) {
            }

            if (!isLoaded) {

                if (internalAssetManager.isFinished()) {
                    try {
                        onResourcesLoaded();
                    } catch (Throwable ignore) {
                    }
                }
                return;
            }

            try {
                if (persistTask != null && !persistTask.isScheduled()) {
                    Timer.instance().scheduleTask(persistTask, 1, 1);
                }
            } catch (Throwable ignore) {
            }

            try {
                if (internetTask != null && !internetTask.isScheduled()) {
                    Timer.instance().scheduleTask(internetTask, 1, 5);
                }
            } catch (Throwable ignore) {
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

            try {
                if (storyManager != null) {
                    Story story = getStory();
                    if (story != null) {

                        boolean isInteractionLocked = story.isInteractionLocked();

                        if (!isInteractionLocked && !story.isCompleted) {

                            removePurchaseFragment();

                            if (!GameState.isPaused) {
                                storyManager.update(
                                        story.progress + (delta * 1000),
                                        story.totalDuration
                                );
                            }

                            storyManager.startLoadingImages();

                            if (!GameState.isPaused) {

                                if (story.isValid()) {

                                    if (game.achievementService != null)
                                        game.achievementService.onScenarioVisited(story.currentScenario.scenario);

                                    if (story.isCompleted) {

                                        storyManager.onCompleted();

                                    } else {
                                        storyManager.startLoadingAudio();
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
                }
            } catch (Throwable ignore) {
            }

            try {
                if (victoryFragment != null) {
                    victoryFragment.update();
                }
            } catch (Throwable ignore) {
            }

            createAchievementFragment();

            try {
                if (ui != null) {
                    ui.act(delta);
                    ui.draw();
                }
            } catch (Throwable ignore) {
            }

        } catch (Throwable ignore) {
        }

    }

    public boolean isChapterPurchased() {
        try {

            if (game.params.isStandaloneProVersion) {
                return true;
            }

            Story story = getStory();

            PurchaseState state = game.gameState.purchaseState;

            boolean isPurchased = true;

            if (story != null) {

                Chapter chapter = ChapterRepository.find(game.gameState, story.currentScenario.scenario.chapter);

                isPurchased = chapter.number <= state.maxChapter;

                Log.e(tag, "isChapterPurchased " + chapter.number + "/" + state.maxChapter);
            }

            return isPurchased;

        } catch (Throwable ignore) {
        }

        return true;
    }

    public void isChapterDownloaded() {
        try {

            Story story = getStory();

            PurchaseState state = game.gameState.purchaseState;

            if (story != null) {

                if (game.isOnlineMode()) {

                    final Chapter chapter = ChapterRepository.find(game.gameState, story.currentScenario.scenario.chapter);

                    if (game.canUseIntenetForChapter(chapter.name)) {
                        //sending previous chapter
                        GameState.pause(tag);
                        MunhauzenGame.downloadExpansionInteface.downloadExpansionAndDeletePrev(chapter.name, new DownloadSuccessFailureListener() {
                            @Override
                            public void onSuccess() {
                                Log.i(tag, "Chapter " + chapter.name + " is downloaded!");
                                GameState.unpause(tag);
                            }

                            @Override
                            public void onFailure() {
                                Log.e(tag, "Chapter " + chapter.name + " is not downloaded! => openNoInternetBanner");
                                if (noInternetFragment == null) {
                                    openNoInternetBanner(new Runnable() {
                                        @Override
                                        public void run() {
                                            destroyBanners();
                                            navigateTo(new GameScreen(game));
                                        }
                                    });
                                }
                            }
                        });
                    } else {
                        Log.e(tag, "Can't download the chapter " + chapter.name + "/" + state.maxChapter);
                    }
                }
            }
        } catch (Throwable ignore) {
        }
    }


    public void createAchievementFragment() {
        try {
            if (game.gameState.achievementState.achievementsToDisplay == null) {
                game.gameState.achievementState.achievementsToDisplay = new Stack<>();
            }

            if (!game.gameState.achievementState.achievementsToDisplay.isEmpty()) {
                if (achievementFragment == null) {

                    String name = game.gameState.achievementState.achievementsToDisplay.pop();

                    Inventory inventory = InventoryRepository.find(game.gameState, name);

                    Log.i(tag, "createAchievementFragment: " + inventory.name);

                    achievementFragment = new AchievementFragment(this);
                    achievementFragment.create(inventory);

                    gameLayers.setAchievementLayer(achievementFragment);

                    achievementFragment.fadeIn();
                }
            }

        } catch (Throwable ignore) {
        }
    }

    public void removeAchievementFragment() {
        if (achievementFragment != null) {
            achievementFragment.dispose();
            gameLayers.setAchievementLayer(null);
            achievementFragment = null;
        }
    }

    public void createPurchaseFragment() {
        if (purchaseFragment == null) {
            purchaseFragment = new PurchaseFragment(this);
            purchaseFragment.create();

            gameLayers.setPurchaseLayer(purchaseFragment);

            purchaseFragment.fadeIn();
        }
    }

    public void removePurchaseFragment() {
        if (purchaseFragment != null) {
            purchaseFragment.dispose();
            gameLayers.setPurchaseLayer(null);
            purchaseFragment = null;
        }
    }

    public void checkBackPressed() {
        if (Gdx.input.isKeyPressed(Input.Keys.BACK)) {
            if (!isBackPressed) {
                isBackPressed = true;
                onBackPressed();
            }
        }
    }

    public void onBackPressed() {
        Log.i(tag, "onBackPressed");

        stopCurrentSfx();

        Story story = getStory();
        if (story != null) {
            if (story.currentInteraction != null) {
                if (story.currentInteraction.interaction instanceof ServantsInteraction) {
                    ServantsInteraction interaction = (ServantsInteraction) story.currentInteraction.interaction;

                    if (ServantsFireImageFragment.class.getSimpleName().equals(interaction.currentFragment)) {

                        Log.i(tag, "onBackPressed but SERVANTS are active");

                        interaction.openHireFragment();

                        isBackPressed = false;

                        return;
                    }
                }
            }

        }

        game.sfxService.onBackToMenuClicked();

        navigateTo(new MenuScreen(game));
    }

    private void drawBackground() {
        if (game.batch == null || background == null) return;

        game.batch.begin();
        game.batch.disableBlending();

        game.batch.draw(background,
                0, 0,
                MunhauzenGame.WORLD_WIDTH, MunhauzenGame.WORLD_HEIGHT
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

        isBackPressed = false;

        Story story = getStory();
        if (story != null) {
            if (story.currentInteraction != null && story.currentInteraction.interaction != null) {
                story.currentInteraction.interaction.dispose();
            }
        }

        if (storyManager != null) {
            storyManager.dispose();
            storyManager = null;
        }

        isLoaded = false;

        if (persistTask != null) {
            persistTask.cancel();
            persistTask = null;
        }

        if (internetTask != null) {
            internetTask.cancel();
            internetTask = null;
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

        if (purchaseFragment != null) {
            purchaseFragment.destroy();
            purchaseFragment = null;
        }

        if (achievementFragment != null) {
            achievementFragment.destroy();
            achievementFragment = null;
        }

        if (gameLayers != null) {
            gameLayers.dispose();
            gameLayers = null;
        }

        if (assetManager != null) {
            assetManager.dispose();
            assetManager = null;
        }

        if (internalAssetManager != null) {
            internalAssetManager.dispose();
            internalAssetManager = null;
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

        background = null;
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

//        Log.i(tag, "showProgressBar");
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

    public void setLastBackground(String file) {

        String[] path = file.split("/");

        String name = file;
        if (path.length > 1) {
            String[] filename = path[path.length - 1].split("\\.");
            if (filename.length > 1) {
                name = filename[0];
            }
        }

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
        Save save = game.gameState.activeSave;

        save.updatedAt = DateUtils.now();

        return save;
    }

    public void stopCurrentSfx() {
        game.stopCurrentSfx();
    }

    public void onCriticalError(Throwable e) {
        game.navigator.onCriticalError(e);
    }

    public void navigateTo(Screen screen) {
        game.navigator.navigateTo(screen);
    }

    public void onInventoryAdded(Inventory inventory) {
        game.achievementService.onInventoryAdded(inventory);
    }


    public static String getPreviousChapterName(String currentChapterName) {

        //get previous and next chapter
        String previousChapter = "";
        String nextChapter = "";
        try {

            String chapterJson = Files.getChaptersFile().readString();
            JSONArray chapters = new JSONArray(chapterJson);

            for (int m = 0; m < chapters.length(); m++) {

                JSONObject jsonObject = chapters.getJSONObject(m);
                if (currentChapterName.equals(jsonObject.getString("name"))) {
                    int currentChapNumber = jsonObject.getInt("number");

                    for (int n = 0; n < chapters.length(); n++) {
                        JSONObject jsonObject1 = chapters.getJSONObject(n);

                        if (jsonObject1.getInt("number") == currentChapNumber - 1) {
                            previousChapter = jsonObject1.getString("name");
                            System.out.println("Prevchap--->" + previousChapter);
                        } else if (jsonObject1.getInt("number") == currentChapNumber + 1) {
                            nextChapter = jsonObject1.getString("name");
                            System.out.println("NextChap---श>" + nextChapter);
                        }

                    }

                    break;
                }


            }

            return previousChapter;
        } catch (Exception e) {
            e.printStackTrace();
            return previousChapter;
        }

    }
}
