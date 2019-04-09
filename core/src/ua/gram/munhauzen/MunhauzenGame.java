package ua.gram.munhauzen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.service.DatabaseManager;

public class MunhauzenGame extends Game {

    public static int WORLD_WIDTH;
    public static int WORLD_HEIGHT;
    public static boolean PAUSED = false;
    public static final boolean DEBUG = true;

    public GameState gameState;
    public SpriteBatch batch;
    public OrthographicCamera camera;
    public Viewport view;
    public FontProvider fontProvider;
    public ButtonBuilder buttonBuilder;
    public AssetManager assetManager;

    public static void pauseGame() {
        PAUSED = true;
    }

    public static void resumeGame() {
        PAUSED = false;
    }

    @Override
    public void create() {
        Gdx.app.log("MunhauzenGame", "create");

        WORLD_WIDTH = Gdx.graphics.getWidth();
        WORLD_HEIGHT = Gdx.graphics.getHeight();

        loadGameState();
        loadGlobalAssets();

        createCamera();
        createBatch();
        createViewport();

        buttonBuilder = new ButtonBuilder(this);

        setScreen(new GameScreen(this));
    }

    @Override
    public void dispose() {
        if (batch != null)
            batch.dispose();

        if (fontProvider != null)
            fontProvider.dispose();

        if (buttonBuilder != null)
            buttonBuilder.dispose();
    }

    @Override
    public void resize(int width, int height) {
        view.update(width, height);
    }

    private void loadGameState() {
        gameState = new GameState();

        new DatabaseManager().load(gameState);
    }

    private void loadGlobalAssets() {
        fontProvider = new FontProvider();
        fontProvider.load();

        assetManager = new AssetManager();
        assetManager.load("a0.jpg", Texture.class);
        assetManager.load("ui/b_primary_enabled.9.png", Texture.class);

        assetManager.finishLoading();
    }

    private void createCamera() {
        if (camera != null) return;

        camera = new OrthographicCamera();
        camera.position.set(WORLD_WIDTH / 2f, WORLD_HEIGHT / 2f, 0);
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

//    private Animation<TextureRegion> texture2Animation(String file, float speed, int cols, int rows) {
//        Texture sheet = new Texture(Gdx.files.internal(file));
//
//        TextureRegion[][] tmp = TextureRegion.split(sheet,
//                sheet.getWidth() / cols,
//                sheet.getHeight() / rows);
//
//        TextureRegion[] walkFrames = new TextureRegion[cols * rows];
//        int index = 0;
//        for (int i = 0; i < rows; i++) {
//            for (int j = 0; j < cols; j++) {
//                walkFrames[index++] = tmp[i][j];
//            }
//        }
//
//        return new Animation<TextureRegion>(speed, walkFrames);
//    }
}
