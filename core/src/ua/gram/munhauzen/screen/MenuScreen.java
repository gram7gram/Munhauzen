package ua.gram.munhauzen.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Timer;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.MunhauzenStage;
import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.screen.menu.fragment.ControlsFragment;
import ua.gram.munhauzen.screen.menu.fragment.DemoBanner;
import ua.gram.munhauzen.screen.menu.fragment.GreetingBanner;
import ua.gram.munhauzen.screen.menu.fragment.ImageFragment;
import ua.gram.munhauzen.screen.menu.fragment.ProBanner;
import ua.gram.munhauzen.screen.menu.fragment.RateBanner;
import ua.gram.munhauzen.screen.menu.fragment.ShareBanner;
import ua.gram.munhauzen.service.AudioService;
import ua.gram.munhauzen.ui.MenuLayers;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class MenuScreen implements Screen {

    private final String tag = getClass().getSimpleName();
    public final MunhauzenGame game;
    public MunhauzenStage ui;
    public MenuLayers layers;
    public final AssetManager assetManager;
    public ImageFragment imageFragment;
    public ControlsFragment controlsFragment;
    public ShareBanner shareBanner;
    private Texture background;
    private boolean isLoaded;
    public GreetingBanner greetingBanner;
    public RateBanner rateBanner;
    public DemoBanner demoBanner;
    public ProBanner proBanner;
    public AudioService audioService;

    public MenuScreen(MunhauzenGame game) {
        this.game = game;
        assetManager = new AssetManager();
    }

    @Override
    public void show() {

        Log.i(tag, "show");

        audioService = new AudioService(game);
        ui = new MunhauzenStage(game);

        isLoaded = false;
        GameState.unpause();

        assetManager.load("menu/icon_crown_sheet_1x9.png", Texture.class);
        assetManager.load("menu/icon_helm_sheet_1x5.png", Texture.class);
        assetManager.load("menu/icon_lion_sheet_1x8.png", Texture.class);
        assetManager.load("menu/icon_rose_sheet_1x6.png", Texture.class);
        assetManager.load("menu/icon_shield_sheet_1x8.png", Texture.class);
        assetManager.load("menu/mmv_btn.png", Texture.class);
        assetManager.load("menu/mmv_fond_1.jpg", Texture.class);
        assetManager.load("menu/b_share.png", Texture.class);
        assetManager.load("menu/b_share_disabled.png", Texture.class);
        assetManager.load("menu/b_rate.png", Texture.class);
        assetManager.load("menu/b_rate_disabled.png", Texture.class);
        assetManager.load("menu/b_demo.png", Texture.class);
        assetManager.load("menu/b_demo_disabled.png", Texture.class);
        assetManager.load("menu/b_menu.png", Texture.class);
        assetManager.load("menu/b_exit_on.png", Texture.class);
        assetManager.load("menu/menu_logo.png", Texture.class);
        assetManager.load("menu/b_lock.png", Texture.class);
        assetManager.load("menu/b_full_version.png", Texture.class);
    }

    private void onResourcesLoaded() {

        Log.i(tag, "onResourcesLoaded");

        isLoaded = true;

        background = game.assetManager.get("p0.jpg", Texture.class);

        layers = new MenuLayers(this);

        ui.addActor(layers);

        controlsFragment = new ControlsFragment(this);
        controlsFragment.create();

        layers.setControlsLayer(controlsFragment);

        imageFragment = new ImageFragment(this);
        imageFragment.create();

        layers.setBackgroundLayer(imageFragment);

        ui.addListener(new ActorGestureListener() {
            @Override
            public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
                super.touchDown(event, x, y, pointer, button);

                Log.i(tag, "ui clicked");

                try {

                    if (controlsFragment != null) {
                        controlsFragment.scheduleFadeOut();
                    }

                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }
        });

        Gdx.input.setInputProcessor(ui);

        int openCount = game.preferences.getInteger(tag + ":menuOpenCount");

        boolean isGreetingViewed = game.preferences.getBoolean(tag + ":isGreetingViewed");
        boolean isShareViewed = game.preferences.getBoolean(tag + ":isShareViewed");

        boolean canOpenGreeting = !isGreetingViewed;
        boolean canOpenShare = !isShareViewed && openCount % 5 == 0;
        boolean canOpenVersion = openCount % 7 == 0;

        if (canOpenGreeting) {
            Timer.instance().scheduleTask(new Timer.Task() {
                @Override
                public void run() {

                    try {
                        greetingBanner = new GreetingBanner(MenuScreen.this);
                        greetingBanner.create();

                        layers.setBannerLayer(greetingBanner);

                        greetingBanner.fadeIn();

                        Timer.instance().postTask(new Timer.Task() {
                            @Override
                            public void run() {
                                game.preferences.putBoolean(tag + ":isGreetingViewed", true).flush();
                            }
                        });
                    } catch (Throwable e) {
                        Log.e(tag, e);
                    }
                }
            }, 2);
        } else if (canOpenShare) {
            Timer.instance().scheduleTask(new Timer.Task() {
                @Override
                public void run() {

                    try {
                        greetingBanner = new GreetingBanner(MenuScreen.this);
                        greetingBanner.create();

                        layers.setBannerLayer(greetingBanner);

                        greetingBanner.fadeIn();

                    } catch (Throwable e) {
                        Log.e(tag, e);
                    }
                }
            }, 2);
        } else if (canOpenVersion) {
            Timer.instance().scheduleTask(new Timer.Task() {
                @Override
                public void run() {

                    try {
                        if (game.params.isPro) {
                            proBanner = new ProBanner(MenuScreen.this);
                            proBanner.create();

                            layers.setBannerLayer(proBanner);

                            proBanner.fadeIn();
                        } else {
                            demoBanner = new DemoBanner(MenuScreen.this);
                            demoBanner.create();

                            layers.setBannerLayer(demoBanner);

                            demoBanner.fadeIn();
                        }

                    } catch (Throwable e) {
                        Log.e(tag, e);
                    }
                }
            }, 2);
        }

        if (openCount % 7 == 0) {
            openCount = 0;
        }

        final int openCountToSave = openCount + 1;

        Timer.instance().postTask(new Timer.Task() {
            @Override
            public void run() {
                game.preferences.putInteger(tag + ":menuOpenCount", openCountToSave).flush();
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

        if (greetingBanner != null) {
            greetingBanner.update();
        }

        if (shareBanner != null) {
            shareBanner.update();
        }

        if (rateBanner != null) {
            rateBanner.update();
        }

        if (ui != null) {
            ui.act(delta);
            ui.draw();
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

        Timer.instance().clear();

        Log.i(tag, "dispose");

        isLoaded = false;

        assetManager.dispose();

        audioService.dispose();

        if (ui != null) {
            ui.dispose();
            ui = null;
        }

        layers.dispose();

        background = null;
    }

    public void onCriticalError(Throwable e) {
        game.onCriticalError(e);
        dispose();
    }
}
