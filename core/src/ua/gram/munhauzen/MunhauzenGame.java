package ua.gram.munhauzen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.screen.MainMenuScreen;
import ua.gram.munhauzen.service.DatabaseManager;
import ua.gram.munhauzen.service.InventoryService;
import ua.gram.munhauzen.utils.ExceptionHandler;
import ua.gram.munhauzen.utils.Log;

public class MunhauzenGame extends Game {

    public static int WORLD_WIDTH;
    public static int WORLD_HEIGHT;
    public static boolean PAUSED = false;
    public static final boolean DEBUG = false;
    public static final boolean DEBUG_ZOOM = false;
    public static final boolean DEBUG_RENDER_INFO = true;
    public static final boolean DEBUG_OVERWRITE_DURATION = true;
    public static final int PROGRESS_BAR_FADE_OUT_DELAY = DEBUG ? 1 : 5;
    public static String developmentScenario;// = "a2_2_0";

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
    private Throwable currentError;

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

            handleInput();

            drawError();

        } catch (Throwable e) {
            Log.e(tag, e);

            currentError = e;

            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    currentError = null;
                }
            }, 10);
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
        assetManager.load("ui/b_primary_enabled.9.png", Texture.class);
        assetManager.load("ui/b_primary_disabled.png", Texture.class);
    }

    private void createCamera() {
        if (camera != null) return;

        camera = new OrthographicCamera();
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

        float ratio = 1f * WORLD_WIDTH / WORLD_HEIGHT;

        view = new ExtendViewport(WORLD_WIDTH * ratio, WORLD_HEIGHT, camera);
        view.apply();
    }


    private void handleInput() {

        if (DEBUG_ZOOM) {
            boolean hasChanged = false;

            if (Gdx.input.isKeyPressed(Input.Keys.VOLUME_DOWN)) {
                Log.i(tag, "zoom out");
                camera.zoom += 0.1;
                hasChanged = true;
            }

            if (Gdx.input.isKeyPressed(Input.Keys.VOLUME_UP)) {
                Log.i(tag, "zoom in");
                camera.zoom -= 0.1;
                hasChanged = true;
            }

            if (!hasChanged) return;

            camera.update();
        }

    }

    private void drawError() {

        if (currentError == null) return;

        int fontSize = FontProvider.h4;
        BitmapFont font = fontProvider.getFont(FontProvider.BuxtonSketch, fontSize);
        if (font != null) {

            font.setColor(Color.RED);

            ArrayList<String> strings = new ArrayList<>();
            strings.add("error: " + currentError.getMessage());

            int offset = fontSize + 1;
            int row = -1;

            batch.begin();
            for (String string : strings) {
                font.draw(batch, string, 10, 10 + fontSize + (++row) * offset);
            }
            batch.end();
        }
    }

    @Override
    public void setScreen(Screen screen) {
        super.setScreen(screen);
        currentError = null;
    }

}
