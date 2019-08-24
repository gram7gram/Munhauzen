package ua.gram.munhauzen.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Timer;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.AudioFail;
import ua.gram.munhauzen.entity.MenuState;
import ua.gram.munhauzen.screen.menu.fragment.ControlsFragment;
import ua.gram.munhauzen.screen.menu.fragment.DemoBanner;
import ua.gram.munhauzen.screen.menu.fragment.ExitDialog;
import ua.gram.munhauzen.screen.menu.fragment.GreetingBanner;
import ua.gram.munhauzen.screen.menu.fragment.ImageFragment;
import ua.gram.munhauzen.screen.menu.fragment.ProBanner;
import ua.gram.munhauzen.screen.menu.fragment.RateBanner;
import ua.gram.munhauzen.screen.menu.fragment.ShareBanner;
import ua.gram.munhauzen.screen.menu.listenter.MenuStageListener;
import ua.gram.munhauzen.screen.menu.ui.MenuLayers;
import ua.gram.munhauzen.service.AudioService;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class MenuScreen extends AbstractScreen {

    public MenuLayers layers;
    public ImageFragment imageFragment;
    public ControlsFragment controlsFragment;
    public ShareBanner shareBanner;
    public GreetingBanner greetingBanner;
    public RateBanner rateBanner;
    public DemoBanner demoBanner;
    public ProBanner proBanner;
    public ExitDialog exitDialog;
    public AudioService audioService;
    public boolean isButtonClicked;

    public MenuScreen(MunhauzenGame game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();

        audioService = new AudioService(game);

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

        assetManager.load("menu/share_sheet_1x2.png", Texture.class);
        assetManager.load("menu/rate_sheet_1x2.png", Texture.class);
        assetManager.load("menu/full_version_sheet_1x2.png", Texture.class);
        assetManager.load("menu/demo_version_sheet_1x4.png", Texture.class);
    }

    @Override
    protected void onResourcesLoaded() {
        super.onResourcesLoaded();

        for (AudioFail fail : game.gameState.audioFailRegistry) {
            if (fail.isFailOpenedOnStart) {
                game.gameState.history.openedFails.add(fail.name);
            }
        }

        layers = new MenuLayers();

        ui.addActor(layers);

        controlsFragment = new ControlsFragment(this);
        controlsFragment.create();

        layers.setControlsLayer(controlsFragment);

        imageFragment = new ImageFragment(this);
        imageFragment.create();

        layers.setBackgroundLayer(imageFragment);

        ui.addListener(new MenuStageListener(this));

        Gdx.input.setInputProcessor(ui);

        MenuState menuState = game.gameState.menuState;

        int openCount = menuState.openCount;

        if (menuState.forceShowThankYouBanner) {
            menuState.forceShowThankYouBanner = false;

            Timer.instance().scheduleTask(new Timer.Task() {
                @Override
                public void run() {
                    openVersionBanner();
                }
            }, 1);

        } else {

            boolean isGreetingViewed = menuState.isGreetingViewed;
            boolean isShareViewed = menuState.isShareViewed;

            boolean isBannerActive = layers.bannerLayer != null;
            boolean canOpenGreeting = !isBannerActive && !isGreetingViewed;
            boolean canOpenShare = !isBannerActive && !isShareViewed && openCount % 5 == 0;
            boolean canOpenVersion = !isBannerActive && openCount % 7 == 0;

            if (canOpenGreeting) {
                Timer.instance().scheduleTask(new Timer.Task() {
                    @Override
                    public void run() {
                        openGreetingBanner();
                    }
                }, 2);
            } else if (canOpenShare) {
                Timer.instance().scheduleTask(new Timer.Task() {
                    @Override
                    public void run() {
                        openShareBanner();
                    }
                }, 2);
            } else if (canOpenVersion) {
                Timer.instance().scheduleTask(new Timer.Task() {
                    @Override
                    public void run() {
                        openVersionBanner();
                    }
                }, 2);
            }
        }

        ++openCount;

        if (openCount > 7) {
            openCount = 0;
        }

        menuState.openCount = openCount;

    }

    private void openGreetingBanner() {
        try {

            if (layers.bannerLayer != null) return;
            if (isButtonClicked) return;

            greetingBanner = new GreetingBanner(MenuScreen.this);
            greetingBanner.create();

            layers.setBannerLayer(greetingBanner);

            greetingBanner.fadeIn();

            game.gameState.menuState.isGreetingViewed = true;
        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }

    private void openShareBanner() {

        try {

            if (layers.bannerLayer != null) return;
            if (isButtonClicked) return;

            shareBanner = new ShareBanner(MenuScreen.this);
            shareBanner.create();

            layers.setBannerLayer(shareBanner);

            shareBanner.fadeIn();

        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }

    private void openVersionBanner() {
        try {

            if (layers.bannerLayer != null) return;
            if (isButtonClicked) return;

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        try {

            exitDialog = new ExitDialog(this);
            exitDialog.create();

            layers.setBannerLayer(exitDialog);

            exitDialog.fadeIn();

        } catch (Throwable e) {
            Log.e(tag, e);
        }

    }

    @Override
    public void renderAfterLoaded(float delta) {

        if (greetingBanner != null) {
            greetingBanner.update();
        }

        if (shareBanner != null) {
            shareBanner.update();
        }

        if (rateBanner != null) {
            rateBanner.update();
        }

        if (exitDialog != null) {
            exitDialog.update();
        }
    }

    @Override
    public void dispose() {
        super.dispose();

        audioService.dispose();

        layers.dispose();

        if (exitDialog != null) {
            exitDialog.destroy();
            exitDialog = null;
        }

        if (greetingBanner != null) {
            greetingBanner.destroy();
            greetingBanner = null;
        }

        if (proBanner != null) {
            proBanner.destroy();
            proBanner = null;
        }

        if (demoBanner != null) {
            demoBanner.destroy();
            demoBanner = null;
        }

        if (shareBanner != null) {
            shareBanner.destroy();
            shareBanner = null;
        }

        if (rateBanner != null) {
            rateBanner.destroy();
            rateBanner = null;
        }
    }
}
