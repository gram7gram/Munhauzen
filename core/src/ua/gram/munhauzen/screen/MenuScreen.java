package ua.gram.munhauzen.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Timer;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.AchievementState;
import ua.gram.munhauzen.entity.AudioFail;
import ua.gram.munhauzen.entity.Image;
import ua.gram.munhauzen.entity.MenuState;
import ua.gram.munhauzen.entity.StoryAudio;
import ua.gram.munhauzen.screen.menu.fragment.ControlsFragment;
import ua.gram.munhauzen.screen.menu.fragment.DemoFragment;
import ua.gram.munhauzen.screen.menu.fragment.ExitFragment;
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
    public ExitFragment exitFragment;
    public ThankYouFragment thankYouFragment;
    public AudioService audioService;
    public boolean isButtonClicked, isZoomStarted;
    public StoryAudio currentSfx;

    public MenuScreen(MunhauzenGame game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();

        audioService = new AudioService(game);

        assetManager.load("ui/b_sound_on.png", Texture.class);
        assetManager.load("ui/b_sound_off.png", Texture.class);

        assetManager.load("menu/icon_an_crown_sheet.png", Texture.class);
        assetManager.load("menu/icon_an_helmet_sheet.png", Texture.class);
        assetManager.load("menu/icon_an_lion_sheet.png", Texture.class);
        assetManager.load("menu/icon_an_rose_sheet.png", Texture.class);
        assetManager.load("menu/icon_an_shield_sheet.png", Texture.class);
        assetManager.load("menu/icon_an_cannons_sheet.png", Texture.class);

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

        assetManager.load("menu/b_share_an_sheet.png", Texture.class);
        assetManager.load("menu/b_rate_an_sheet.png", Texture.class);
        assetManager.load("menu/b_full_version_an_sheet.png", Texture.class);
        assetManager.load("menu/b_demo_version_an_sheet.png", Texture.class);
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
        AchievementState achievementState = game.gameState.achievementState;

        int openCount = menuState.openCount;

        if (menuState.showThankYouBanner) {
            menuState.showThankYouBanner = false;

            openThankYouBanner();

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
            } else if (menuState.isFirstMenuAfterGameStart) {

                menuState.isFirstMenuAfterGameStart = false;

                if (achievementState.areAllGoofsUnlocked && achievementState.areAllImagesUnlocked) {

                    stopCurrentSfx();
                    currentSfx = game.sfxService.onAllGoofsAndImagesUnlocked();

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

            exitFragment = new ExitFragment(this);
            exitFragment.create();

            layers.setBannerLayer(exitFragment);

            exitFragment.fadeIn();

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

        if (exitFragment != null) {
            exitFragment.update();
        }

        if (isZoomStarted) {
            game.camera.zoom -= .4f * delta;
        }
    }

    @Override
    public void dispose() {
        super.dispose();

        if (exitFragment != null) {
            exitFragment.destroy();
            exitFragment = null;
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

        if (audioService != null) {
            audioService.dispose();
            audioService = null;
        }

        if (layers != null) {
            layers.dispose();
            layers = null;
        }
    }

    public void scaleAndNavigateTo(final Screen screen) {

        isZoomStarted = true;

        controlsFragment.fadeOutFancy();

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

    public void stopCurrentSfx() {
        if (currentSfx != null) {
            game.sfxService.dispose(currentSfx);
            currentSfx = null;
        }
    }
}
