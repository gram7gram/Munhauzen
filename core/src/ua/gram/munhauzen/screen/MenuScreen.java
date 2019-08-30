package ua.gram.munhauzen.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Timer;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.AudioFail;
import ua.gram.munhauzen.entity.Image;
import ua.gram.munhauzen.entity.MenuState;
import ua.gram.munhauzen.screen.menu.fragment.ControlsFragment;
import ua.gram.munhauzen.screen.menu.fragment.DemoFragment;
import ua.gram.munhauzen.screen.menu.fragment.ExitDialog;
import ua.gram.munhauzen.screen.menu.fragment.GreetingFragment;
import ua.gram.munhauzen.screen.menu.fragment.ImageFragment;
import ua.gram.munhauzen.screen.menu.fragment.ProFragment;
import ua.gram.munhauzen.screen.menu.fragment.RateFragment;
import ua.gram.munhauzen.screen.menu.fragment.ShareFragment;
import ua.gram.munhauzen.screen.menu.fragment.ThankYouFragment;
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
    public ShareFragment shareFragment;
    public GreetingFragment greetingFragment;
    public RateFragment rateFragment;
    public DemoFragment demoFragment;
    public ProFragment proFragment;
    public ExitDialog exitDialog;
    public ThankYouFragment thankYouFragment;
    public AudioService audioService;
    public boolean isButtonClicked, isZoomStarted;

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

        for (Image image : game.gameState.getGalleryImages()) {

            boolean isOpened = game.gameState.history.viewedImages.contains(image.name);
            boolean isViewed = game.gameState.galleryState.visitedImages.contains(image.name);
            if (isOpened && !isViewed) {
                game.gameState.galleryState.hasUpdates = true;
                break;
            }
        }

        for (AudioFail fail : game.gameState.audioFailRegistry) {

            boolean isOpened = game.gameState.history.openedFails.contains(fail.name);
            boolean isViewed = game.gameState.failsState.listenedAudio.contains(fail.name);
            if (isOpened && !isViewed) {
                game.gameState.failsState.hasUpdates = true;
                break;
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

        openBannerIfNeeded();
    }

    private void openBannerIfNeeded() {
        MenuState menuState = game.gameState.menuState;

        int openCount = menuState.openCount;

        if (menuState.showThankYouBanner) {
            menuState.showThankYouBanner = false;

            Timer.instance().scheduleTask(new Timer.Task() {
                @Override
                public void run() {
                    openThankYouBanner();
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
            } else {
                if (menuState.isFirstVisit) {
                    menuState.isFirstVisit = false;

                    game.sfxService.onFirstVisitToMenu();
                }
            }
        }

        ++openCount;

        if (openCount > 7) {
            openCount = 0;
        }

        menuState.openCount = openCount;
    }

    private void openThankYouBanner() {
        try {

            thankYouFragment = new ThankYouFragment(MenuScreen.this);
            thankYouFragment.create();

            layers.setBannerLayer(thankYouFragment);

            thankYouFragment.fadeIn();
        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }

    private void openGreetingBanner() {
        try {

            if (layers.bannerLayer != null) return;
            if (isButtonClicked) return;

            greetingFragment = new GreetingFragment(MenuScreen.this);
            greetingFragment.create();

            layers.setBannerLayer(greetingFragment);

            greetingFragment.fadeIn();

            game.gameState.menuState.isGreetingViewed = true;
        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }

    private void openShareBanner() {

        try {

            if (layers.bannerLayer != null) return;
            if (isButtonClicked) return;

            shareFragment = new ShareFragment(MenuScreen.this);
            shareFragment.create();

            layers.setBannerLayer(shareFragment);

            shareFragment.fadeIn();

        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }

    private void openVersionBanner() {
        try {

            if (layers.bannerLayer != null) return;
            if (isButtonClicked) return;

            if (game.params.isPro) {
                proFragment = new ProFragment(MenuScreen.this);
                proFragment.create();

                layers.setBannerLayer(proFragment);

                proFragment.fadeIn();
            } else {
                demoFragment = new DemoFragment(MenuScreen.this);
                demoFragment.create();

                layers.setBannerLayer(demoFragment);

                demoFragment.fadeIn();
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

        if (greetingFragment != null) {
            greetingFragment.update();
        }

        if (shareFragment != null) {
            shareFragment.update();
        }

        if (rateFragment != null) {
            rateFragment.update();
        }

        if (controlsFragment != null) {
            controlsFragment.update();
        }

        if (exitDialog != null) {
            exitDialog.update();
        }

        if (isZoomStarted) {
            game.camera.zoom -= .4f * delta;
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

        if (greetingFragment != null) {
            greetingFragment.destroy();
            greetingFragment = null;
        }

        if (proFragment != null) {
            proFragment.destroy();
            proFragment = null;
        }

        if (demoFragment != null) {
            demoFragment.destroy();
            demoFragment = null;
        }

        if (shareFragment != null) {
            shareFragment.destroy();
            shareFragment = null;
        }

        if (rateFragment != null) {
            rateFragment.destroy();
            rateFragment = null;
        }
    }

    public void scaleAndNavigateTo(final Screen screen) {

        controlsFragment.fadeOutFancy();

        isZoomStarted = true;

        Timer.instance().scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                imageFragment.fadeOut();
            }
        }, 1.5f);

        Timer.instance().scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                isZoomStarted = false;

                game.camera.zoom = 1;
                game.camera.update();
            }
        }, 2);

        Timer.instance().scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                navigateTo(screen);
            }
        }, 2.1f);
    }
}
