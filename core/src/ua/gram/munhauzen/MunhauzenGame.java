package ua.gram.munhauzen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;

import ua.gram.munhauzen.entity.Device;
import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.entity.StoryAudio;
import ua.gram.munhauzen.screen.ErrorScreen;
import ua.gram.munhauzen.service.AchievementService;
import ua.gram.munhauzen.service.BackgroundSfxService;
import ua.gram.munhauzen.service.DatabaseManager;
import ua.gram.munhauzen.service.InventoryService;
import ua.gram.munhauzen.service.SfxService;
import ua.gram.munhauzen.ui.GameViewport;
import ua.gram.munhauzen.utils.ErrorMonitoring;
import ua.gram.munhauzen.utils.ExpansionAssetManager;
import ua.gram.munhauzen.utils.ExternalFiles;
import ua.gram.munhauzen.utils.InternalAssetManager;
import ua.gram.munhauzen.utils.Log;

public class MunhauzenGame extends Game {

    public static int WORLD_WIDTH;
    public static int WORLD_HEIGHT;
    public static boolean PAUSED = false;

    public static final boolean DEBUG_UI = false;
    public static final boolean CAN_REMOVE_PREVIOUS_EXPANSION = false;
    public static final boolean CAN_SKIP_EXPANSION_VALIDATION = true;
    public static final int PROGRESS_BAR_FADE_OUT_DELAY = 5;

    public static String developmentScenario;

    // WAUWAU GENERAL HARE PICTURE SERVANTS TIMER
    // continue HORN BALLOONS CHAPTER
    // DATE LIONS SLAP PUZZLE SWAMP
    public static String developmentInteraction;

    private final String tag = getClass().getSimpleName();

    public final PlatformParams params;
    public DatabaseManager databaseManager;
    public GameState gameState;
    public SpriteBatch batch;
    public OrthographicCamera camera;
    public Viewport view;
    public FontProvider fontProvider;
    public ButtonBuilder buttonBuilder;
    public InternalAssetManager internalAssetManager;
    public ExpansionAssetManager expansionAssetManager;
    public InventoryService inventoryService;
    public AchievementService achievementService;
    public SfxService sfxService;
    public BackgroundSfxService backgroundSfxService;
    public StoryAudio currentSfx;
    public Navigator navigator;

    public MunhauzenGame(PlatformParams params) {
        this.params = params;
    }

    public String t(String key) {
        return params.translator.t(key);
    }

    public static void pauseGame() {
        PAUSED = true;
    }

    public static void resumeGame() {
        PAUSED = false;
    }

    @Override
    public void create() {
        Log.i(tag, "create");

        try {

            Gdx.input.setCatchKey(Input.Keys.BACK, true);

            ExternalFiles.updateNomedia(params);

            WORLD_WIDTH = Gdx.graphics.getWidth();
            WORLD_HEIGHT = Gdx.graphics.getHeight();

            updateDpi();

            ErrorMonitoring.createInstance(this);

            sfxService = new SfxService(this);
            backgroundSfxService = new BackgroundSfxService(this);
            databaseManager = new DatabaseManager(this);
            navigator = new Navigator(this);

            loadGameState();
            loadGlobalAssets();

            createCamera();
            createBatch();
            createViewport();

            internalAssetManager.finishLoading();

            inventoryService = new InventoryService(gameState);
            buttonBuilder = new ButtonBuilder(this);
            achievementService = new AchievementService(this);

            navigator.openCurrentScreen();


        } catch (Throwable e) {
            Log.e(tag, e);

            onCriticalError(e);
        }
    }

    @Override
    public void render() {

        try {
            super.render();

            if (sfxService != null) {
                sfxService.update();
            }

            if (backgroundSfxService != null) {
                backgroundSfxService.update();
            }

        } catch (Throwable e) {
            Log.e(tag, e);

            onCriticalError(e);
        }
    }

    @Override
    public void dispose() {
        super.dispose();

        try {

            Log.e(tag, "dispose");

            GameState.clearTimer(tag);

            if (batch != null) {
                batch.dispose();
                batch = null;
            }

            if (fontProvider != null) {
                fontProvider.dispose();
                fontProvider = null;
            }

            if (databaseManager != null) {
                databaseManager.persistSync(gameState);
                databaseManager = null;
            }

            if (expansionAssetManager != null) {
                expansionAssetManager.dispose();
                expansionAssetManager = null;
            }

            if (internalAssetManager != null) {
                internalAssetManager.dispose();
                internalAssetManager = null;
            }

            if (sfxService != null) {
                sfxService.dispose();
                sfxService = null;
            }

            if (backgroundSfxService != null) {
                backgroundSfxService.dispose();
                backgroundSfxService = null;
            }

            navigator = null;
            view = null;
            camera = null;
            buttonBuilder = null;
            achievementService = null;
            inventoryService = null;

            gameState = null;

            ErrorMonitoring.destroy();

        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }

    @Override
    public void resize(int width, int height) {
        view.update(width, height);
    }

    public void loadGameState() {
        gameState = new GameState();

        try {
            databaseManager.loadExternal(gameState);
        } catch (Throwable e) {
            Log.e(tag, e);

            onCriticalError(e);
        }
    }

    private void loadGlobalAssets() {
        fontProvider = new FontProvider(this);
        fontProvider.load();

        expansionAssetManager = new ExpansionAssetManager(this);
        internalAssetManager = new InternalAssetManager();
        internalAssetManager.load("p0.jpg", Texture.class);
        internalAssetManager.load("p1.jpg", Texture.class);
        internalAssetManager.load("ui/b_primary_sm_enabled.png", Texture.class);
        internalAssetManager.load("ui/b_primary_sm_disabled.png", Texture.class);
        internalAssetManager.load("ui/b_danger_sm_enabled.png", Texture.class);
        internalAssetManager.load("ui/b_danger_sm_disabled.png", Texture.class);
        internalAssetManager.load("ui/btn_rose_enabled.png", Texture.class);
        internalAssetManager.load("ui/btn_rose_disabled.png", Texture.class);

        sfxService.load();
    }

    private void createCamera() {
        if (camera != null) return;

        Log.i(tag, "camera " + WORLD_WIDTH + "x" + WORLD_HEIGHT);

        camera = new OrthographicCamera(WORLD_WIDTH, WORLD_HEIGHT);
        camera.position.set(WORLD_WIDTH / 2f, WORLD_HEIGHT / 2f, 0);
        camera.zoom = 1;
        camera.update();
    }

    private void createBatch() {
        if (batch != null) return;

        batch = new SpriteBatch();
    }

    private void createViewport() {
        if (view != null) return;

        view = new GameViewport(this);
        view.apply();
    }

    public void onCriticalError(Throwable e) {

        try {
            if (databaseManager != null && gameState != null) {
                databaseManager.persistSync(gameState);
            }
        } catch (Throwable ignore) {
        }

        if (ErrorMonitoring.instance() != null)
            ErrorMonitoring.instance().capture(e);

        setScreen(new ErrorScreen(this, e));
    }

    public void stopCurrentSfx() {
        if (currentSfx != null) {
            sfxService.dispose(currentSfx);
            sfxService.disposeInternal(currentSfx);
            currentSfx = null;
        }

        sfxService.dispose();
    }

    public void updateDpi() {
        if (params.device.type == Device.Type.ios) {
            updateIphoneDpi();
        } else {
            updateAndroidDpi();
        }
    }

    public void updateIphoneDpi() {
        if (WORLD_WIDTH >= 1000 || WORLD_HEIGHT >= 1700) {
            params.dpi = "hdpi";
        } else {
            params.dpi = "mdpi";
            params.scaleFactor = 1;
        }
    }

    public void updateAndroidDpi() {

        if (WORLD_WIDTH >= 1600 || WORLD_HEIGHT >= 2000) {
            params.dpi = "hdpi";

            if (params.scaleFactor == 1) {
                params.scaleFactor = 1.5f;
            }
        } else {
            params.dpi = "mdpi";
            params.scaleFactor = 1;
        }
    }
}
