package ua.gram.munhauzen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.screen.ErrorScreen;
import ua.gram.munhauzen.screen.LogoScreen;
import ua.gram.munhauzen.service.AchievementService;
import ua.gram.munhauzen.service.DatabaseManager;
import ua.gram.munhauzen.service.InventoryService;
import ua.gram.munhauzen.service.SfxService;
import ua.gram.munhauzen.utils.ExpansionAssetManager;
import ua.gram.munhauzen.utils.ExternalFiles;
import ua.gram.munhauzen.utils.InternalAssetManager;
import ua.gram.munhauzen.utils.Log;

public class MunhauzenGame extends Game {

    public static int WORLD_WIDTH;
    public static int WORLD_HEIGHT;
    public static boolean PAUSED = false;
    public static final boolean DEBUG = false;
    public static final boolean IS_EXPANSION_HIDDEN = false;
    public static final boolean DEBUG_RENDER_INFO = true;
    public static final boolean CAN_REMOVE_PREVIOUS_EXPANSION = true;
    public static final int PROGRESS_BAR_FADE_OUT_DELAY = 5;

    public static String developmentScenario;

    private final String tag = getClass().getSimpleName();

    public final PlatformParams params;
    public DatabaseManager databaseManager;
    public GameState gameState;
    public SpriteBatch batch;
    public OrthographicCamera camera;
    public Viewport view;
    public FontProvider fontProvider;
    public ButtonBuilder buttonBuilder;
    public InternalAssetManager assetManager;
    public ExpansionAssetManager expansionAssetManager;
    public InventoryService inventoryService;
    public Preferences preferences;
    public AchievementService achievementService;
    public SfxService sfxService;

    public MunhauzenGame(PlatformParams params) {
        this.params = params;
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

        Gdx.input.setCatchBackKey(true);

        ExternalFiles.moveExpansionIfNeeded();

        ExternalFiles.updateNomedia();

        preferences = Gdx.app.getPreferences(params.versionCode + "-prefs");

        WORLD_WIDTH = Gdx.graphics.getWidth();
        WORLD_HEIGHT = Gdx.graphics.getHeight();

        sfxService = new SfxService(this);
        databaseManager = new DatabaseManager(this);

        loadGameState();
        loadGlobalAssets();

        createCamera();
        createBatch();
        createViewport();

        assetManager.finishLoading();

        inventoryService = new InventoryService(gameState);
        buttonBuilder = new ButtonBuilder(this);
        achievementService = new AchievementService(this);

        setScreen(new LogoScreen(this));
    }

    @Override
    public void dispose() {
        super.dispose();

        try {

            Log.e(tag, "dispose");

            GameState.clearTimer();

            if (batch != null) {
                batch.dispose();
                batch = null;
            }

            if (fontProvider != null) {
                fontProvider.dispose();
                fontProvider = null;
            }

            if (databaseManager != null) {
                databaseManager.persist(gameState);
                databaseManager = null;
            }

            if (expansionAssetManager != null) {
                expansionAssetManager.dispose();
                expansionAssetManager = null;
            }

            if (assetManager != null) {
                assetManager.dispose();
                assetManager = null;
            }

            sfxService = null;
            view = null;
            camera = null;
            preferences = null;
            buttonBuilder = null;
            achievementService = null;
            inventoryService = null;

            gameState = null;

        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }

    @Override
    public void render() {

        try {
            super.render();
        } catch (Throwable e) {
            Log.e(tag, e);

            onCriticalError(e);
        }
    }

    @Override
    public void resize(int width, int height) {
        view.update(width, height);
    }

    private void loadGameState() {
        gameState = new GameState();

        try {
            databaseManager.loadExternal(gameState);
        } catch (Throwable e) {
            Log.e(tag, e);

            onCriticalError(e);
        }
    }

    private void loadGlobalAssets() {
        fontProvider = new FontProvider();
        fontProvider.load();

        expansionAssetManager = new ExpansionAssetManager();
        assetManager = new InternalAssetManager();
        assetManager.load("p0.jpg", Texture.class);
        assetManager.load("p1.jpg", Texture.class);
        assetManager.load("ui/b_primary_sm_enabled.png", Texture.class);
        assetManager.load("ui/b_primary_sm_disabled.png", Texture.class);
        assetManager.load("ui/b_danger_sm_enabled.png", Texture.class);
        assetManager.load("ui/b_danger_sm_disabled.png", Texture.class);
        assetManager.load("ui/btn_rose_enabled.png", Texture.class);
        assetManager.load("ui/btn_rose_disabled.png", Texture.class);
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

        view = new ScreenViewport(camera);
        view.apply();
    }

    public void onCriticalError(Throwable e) {
        setScreen(new ErrorScreen(this, e));
    }

}
