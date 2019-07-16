package ua.gram.munhauzen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.screen.ErrorScreen;
import ua.gram.munhauzen.screen.MainMenuScreen;
import ua.gram.munhauzen.service.DatabaseManager;
import ua.gram.munhauzen.service.InventoryService;
import ua.gram.munhauzen.utils.ExceptionHandler;
import ua.gram.munhauzen.utils.ExternalFiles;
import ua.gram.munhauzen.utils.Log;

public class MunhauzenGame extends Game {

    public static int WORLD_WIDTH;
    public static int WORLD_HEIGHT;
    public static boolean PAUSED = false;
    public static final boolean DEBUG = true;
    public static final boolean DEBUG_RENDER_INFO = true;
    public static final int PROGRESS_BAR_FADE_OUT_DELAY = 5;
    public static String developmentScenario;// = "alions_a";

    private final String tag = getClass().getSimpleName();

    public final PlatformParams params;
    public DatabaseManager databaseManager;
    public GameState gameState;
    public SpriteBatch batch;
    public OrthographicCamera camera;
    public Viewport view;
    public FontProvider fontProvider;
    public ButtonBuilder buttonBuilder;
    public AssetManager assetManager;
    public InventoryService inventoryService;

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

        ExceptionHandler.create();

        ExternalFiles.updateNomedia();

        WORLD_WIDTH = Gdx.graphics.getWidth();
        WORLD_HEIGHT = Gdx.graphics.getHeight();

        databaseManager = new DatabaseManager();

        loadGameState();
        loadGlobalAssets();

        createCamera();
        createBatch();
        createViewport();

        assetManager.finishLoading();

        inventoryService = new InventoryService(gameState);
        buttonBuilder = new ButtonBuilder(this);

        setScreen(new MainMenuScreen(this));
    }

    @Override
    public void dispose() {
        if (batch != null)
            batch.dispose();

        if (fontProvider != null)
            fontProvider.dispose();

        databaseManager = null;
        inventoryService = null;

        ExceptionHandler.dispose();
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
        }
    }

    private void loadGlobalAssets() {
        fontProvider = new FontProvider();
        fontProvider.load();

        assetManager = new AssetManager();
        assetManager.load("p0.jpg", Texture.class);
        assetManager.load("ui/b_primary_sm_enabled.png", Texture.class);
        assetManager.load("ui/b_primary_sm_disabled.png", Texture.class);
        assetManager.load("ui/b_danger_sm_enabled.png", Texture.class);
        assetManager.load("ui/b_danger_sm_disabled.png", Texture.class);
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
