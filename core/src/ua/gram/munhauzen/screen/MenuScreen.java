package ua.gram.munhauzen.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Timer;

import java.util.Stack;

import ua.gram.munhauzen.GameLayerInterface;
import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.AchievementState;
import ua.gram.munhauzen.entity.AudioFail;
import ua.gram.munhauzen.entity.Image;
import ua.gram.munhauzen.entity.Inventory;
import ua.gram.munhauzen.entity.MenuState;
import ua.gram.munhauzen.repository.InventoryRepository;
import ua.gram.munhauzen.screen.menu.fragment.AchievementFragment;
import ua.gram.munhauzen.screen.menu.fragment.ControlsFragment;
import ua.gram.munhauzen.screen.menu.fragment.DemoFragment;
import ua.gram.munhauzen.screen.menu.fragment.ExitFragment;
import ua.gram.munhauzen.screen.menu.fragment.GreetingFragment;
import ua.gram.munhauzen.screen.menu.fragment.ImageFragment;
import ua.gram.munhauzen.screen.menu.fragment.LogoFragment;
import ua.gram.munhauzen.screen.menu.fragment.NewAchievementFragment;
import ua.gram.munhauzen.screen.menu.fragment.ProFragment;
import ua.gram.munhauzen.screen.menu.fragment.RateFragment;
import ua.gram.munhauzen.screen.menu.fragment.ReferalFragment;
import ua.gram.munhauzen.screen.menu.fragment.ReferalThanks3Fragment;
import ua.gram.munhauzen.screen.menu.fragment.ReferalThanks7Fragment;
import ua.gram.munhauzen.screen.menu.fragment.ShareFragment;
import ua.gram.munhauzen.screen.menu.fragment.StartWarningFragment;
import ua.gram.munhauzen.screen.menu.fragment.ThankYouFragment;
import ua.gram.munhauzen.screen.menu.fragment.TutorialFragment;
import ua.gram.munhauzen.screen.menu.fragment.VideoTrailerFragment;
import ua.gram.munhauzen.screen.menu.listenter.MenuStageListener;
import ua.gram.munhauzen.screen.menu.ui.MenuLayers;
import ua.gram.munhauzen.service.AudioService;
import ua.gram.munhauzen.service.ExpansionDownloadManager;
import ua.gram.munhauzen.service.ReferralService;
import ua.gram.munhauzen.ui.AdultGateFragment;
import ua.gram.munhauzen.utils.Log;

public class MenuScreen extends AbstractScreen {

    public MenuLayers layers;
    public ImageFragment imageFragment;
    public ControlsFragment controlsFragment;
    public LogoFragment logoFragment;
    public AchievementFragment achievementFragment;
    public AudioService audioService;
    public boolean isUILocked, isZoomStarted;

    public ShareFragment shareFragment;
    public GreetingFragment greetingFragment;
    public RateFragment rateFragment;
    public ReferalFragment referalFragment;
    public DemoFragment demoFragment;
    public ProFragment proFragment;
    public ExitFragment exitFragment;
    public StartWarningFragment startWarningFragment;
    public ThankYouFragment thankYouFragment;
    public TutorialFragment tutorialFragment;
    public AdultGateFragment adultGateFragment;
    public NewAchievementFragment newAchievementFragment;
    public ReferalThanks3Fragment referalThanks3Fragment;
    public ReferalThanks7Fragment referalThanks7Fragment;
    public VideoTrailerFragment trailerFragment;

    public MenuScreen(MunhauzenGame game) {
        super(game);
    }

    @Override
    public GameLayerInterface getLayers() {
        return layers;
    }

    @Override
    public void show() {
        super.show();

        audioService = new AudioService(game);

        try {
            assetManager.load("GameScreen/t_putty.png", Texture.class);
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
            assetManager.load("menu/b_menu.png", Texture.class);
            assetManager.load("menu/b_exit_on.png", Texture.class);
            assetManager.load("menu/b_lock.png", Texture.class);

            assetManager.load("menu/b_share_an_sheet.png", Texture.class);
            assetManager.load("menu/b_rate_an_sheet.png", Texture.class);
            assetManager.load("menu/b_full_version_an_sheet.png", Texture.class);
            assetManager.load("menu/b_demo_version_an_sheet.png", Texture.class);

            assetManager.load("menu/menu_logo.png", Texture.class);
            assetManager.load("menu/progress_color.png", Texture.class);
            assetManager.load("menu/progress_black.png", Texture.class);

            assetManager.load("menu/btn_referral.png", Texture.class);
            assetManager.load("menu/InviteBar_0.png", Texture.class);
            assetManager.load("menu/InviteBar_100.png", Texture.class);

            assetManager.load("ui/btn_online.png", Texture.class);
            assetManager.load("ui/btn_offline.png", Texture.class);

            assetManager.finishLoading();

            ExpansionDownloadManager downloadManager = new ExpansionDownloadManager(game, null);

            boolean needUpdates = downloadManager.shouldFetchExpansion();

            System.out.println("Need Updates -----------------------------> " + needUpdates);

            MunhauzenGame.onExpansionDownloadComplete.setDownloadNeeded(needUpdates);

        } catch (Throwable e) {
            Log.e(tag, e);

            navigateTo(new LoadingScreen(game));
        }

    }

    @Override
    protected void onResourcesLoaded() {
        super.onResourcesLoaded();

        try {

            game.achievementService.updateTotalPoints();

            for (AudioFail fail : game.gameState.audioFailRegistry) {
                if (fail.isFailOpenedOnStart) {
                    game.achievementService.onFailOpened(fail);
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

            logoFragment = new LogoFragment(this);
            logoFragment.create();

            layers.setLogoLayer(logoFragment);

            achievementFragment = new AchievementFragment(this);
            achievementFragment.create();

            layers.setAchievementLayer(achievementFragment);

            ui.addListener(new MenuStageListener(this));

            try {
                openBannerIfNeeded();
            } catch (Throwable ignore) {
            }

        } catch (Throwable e) {
            Log.e(tag, e);

            onCriticalError(e);
        }
    }

    private void openBannerIfNeeded() {

        if (game.gameState == null) return;

        if (game.gameState.menuState == null) {
            game.gameState.menuState = new MenuState();
        }

        if (game.gameState.achievementState == null) {
            game.gameState.achievementState = new AchievementState();
        }

        MenuState menuState = game.gameState.menuState;
        AchievementState achievementState = game.gameState.achievementState;
        boolean isBannerActive = layers == null || layers.bannerLayer != null;

        if (menuState.isFirstMenuAfterGameStart) {

            menuState.isFirstMenuAfterGameStart = false;

            if (achievementState.areAllGoofsUnlocked && achievementState.areAllImagesUnlocked) {

                game.stopAllAudio();

                game.currentSfx = game.sfxService.onAllGoofsAndImagesUnlocked();

                game.backgroundSfxService.start();

                return;
            }
        }

        int openCount = menuState.openCount;

        ++openCount;

        if (openCount > 7) {
            openCount = 0;
        }

        menuState.openCount = openCount;

        boolean canPlaySfx = true;

        if (menuState.showThankYouBanner) {
            menuState.showThankYouBanner = false;

            openThankYouBanner();

            canPlaySfx = false;

        } else if (!isBannerActive) {

            boolean canOpenGreeting = !menuState.isGreetingViewed;
            boolean canOpenTutorial = !menuState.isTutorialViewed;
            boolean canOpenShare = !menuState.isShareViewed && openCount % 5 == 0;
            boolean canOpenVersion = openCount % 7 == 0;
            boolean canOpenTrailer = !menuState.isTrailerViewed && openCount % 3 == 0;

            if (canOpenGreeting) {

                canPlaySfx = false;

                Timer.instance().scheduleTask(new Timer.Task() {
                    @Override
                    public void run() {
                        openGreetingBanner();
                    }
                }, 2);
            } else if (canOpenTutorial) {

                canPlaySfx = false;

                Timer.instance().scheduleTask(new Timer.Task() {
                    @Override
                    public void run() {
                        openTutorialBanner();
                    }
                }, 2);
            } else if (canOpenShare) {

                canPlaySfx = false;

                Timer.instance().scheduleTask(new Timer.Task() {
                    @Override
                    public void run() {
                        openShareBanner();
                    }
                }, 2);
            } else if (canOpenVersion) {

                canPlaySfx = false;

                Timer.instance().scheduleTask(new Timer.Task() {
                    @Override
                    public void run() {
                        openVersionBanner();
                    }
                }, 2);
            } else if (canOpenTrailer) {

                canPlaySfx = false;

                Timer.instance().scheduleTask(new Timer.Task() {
                    @Override
                    public void run() {
                        openTrailerBanner();
                    }
                }, 2);
            } else {

                Timer.instance().scheduleTask(new Timer.Task() {
                    @Override
                    public void run() {
                        boolean isDisplayed = createNewAchievementFragment();

                        if (!isDisplayed) {
                            openReferralThankYouBanner();
                        }

                    }
                }, 2);
            }
        }

        if (canPlaySfx) {
            if (game.backgroundSfxService != null)
                game.backgroundSfxService.start();
        }
    }

    @Override
    public void destroyBanners() {
        super.destroyBanners();

        if (trailerFragment != null) {
            trailerFragment.destroy();
            trailerFragment = null;
        }
        if (shareFragment != null) {
            shareFragment.destroy();
            shareFragment = null;
        }
        if (greetingFragment != null) {
            greetingFragment.destroy();
            greetingFragment = null;
        }
        if (rateFragment != null) {
            rateFragment.destroy();
            rateFragment = null;
        }
        if (demoFragment != null) {
            demoFragment.destroy();
            demoFragment = null;
        }
        if (proFragment != null) {
            proFragment.destroy();
            proFragment = null;
        }
        if (exitFragment != null) {
            exitFragment.destroy();
            exitFragment = null;
        }
        if (startWarningFragment != null) {
            startWarningFragment.destroy();
            startWarningFragment = null;
        }
        if (thankYouFragment != null) {
            thankYouFragment.destroy();
            thankYouFragment = null;
        }
        if (tutorialFragment != null) {
            tutorialFragment.destroy();
            tutorialFragment = null;
        }
        if (adultGateFragment != null) {
            adultGateFragment.destroy();
            adultGateFragment = null;
        }
        if (referalFragment != null) {
            referalFragment.destroy();
            referalFragment = null;
        }
        if (referalThanks3Fragment != null) {
            referalThanks3Fragment.destroy();
            referalThanks3Fragment = null;
        }
        if (referalThanks7Fragment != null) {
            referalThanks7Fragment.destroy();
            referalThanks7Fragment = null;
        }

        if (!isDisposed) {
            if (game.backgroundSfxService != null) {
                if (!game.backgroundSfxService.isPlaying) {
                    game.backgroundSfxService.start();
                }
            }
        }
    }

    public boolean createNewAchievementFragment() {
        try {
            if (game.gameState.menuState.achievementsToDisplay == null) {
                game.gameState.menuState.achievementsToDisplay = new Stack<>();
            }

            if (!game.gameState.menuState.achievementsToDisplay.isEmpty()) {

                if (newAchievementFragment == null) {

                    destroyBanners();

                    String name = game.gameState.menuState.achievementsToDisplay.pop();

                    Inventory inventory = InventoryRepository.find(game.gameState, name);

                    Log.i(tag, "createAchievementFragment: " + inventory.name);

                    newAchievementFragment = new NewAchievementFragment(this);
                    newAchievementFragment.create(inventory);

                    layers.setBannerLayer(newAchievementFragment);

                    newAchievementFragment.fadeIn();

                    return true;
                }
            }

        } catch (Throwable e) {
            Log.e(tag, e);
        }

        return false;
    }

    public void openStartWarningBanner(Timer.Task task) {
        try {
            startWarningFragment = new StartWarningFragment(MenuScreen.this, task);
            startWarningFragment.create();

            layers.setBannerLayer(startWarningFragment);

            startWarningFragment.fadeIn();
        } catch (Throwable e) {
            Log.e(tag, e);
        }
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

    private void openTutorialBanner() {
        try {

            if (layers == null || layers.bannerLayer != null) return;
            if (isUILocked) return;

            tutorialFragment = new TutorialFragment(MenuScreen.this);
            tutorialFragment.create();

            layers.setBannerLayer(tutorialFragment);

            tutorialFragment.fadeIn();

            game.gameState.menuState.isTutorialViewed = true;
        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }

    private void openGreetingBanner() {
        try {

            if (layers == null || layers.bannerLayer != null) return;
            if (isUILocked) return;

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

            if (layers == null || layers.bannerLayer != null) return;
            if (isUILocked) return;

            shareFragment = new ShareFragment(MenuScreen.this);
            shareFragment.create();

            layers.setBannerLayer(shareFragment);

            shareFragment.fadeIn();

        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }

    private void openTrailerBanner() {

        try {

            if (layers == null || layers.bannerLayer != null) return;
            if (isUILocked) return;

            trailerFragment = new VideoTrailerFragment(this);
            trailerFragment.create();

            layers.setBannerLayer(trailerFragment);

            trailerFragment.fadeIn();

        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }

    private void openVersionBanner() {
        try {

            if (layers == null || layers.bannerLayer != null) return;
            if (isUILocked) return;

            if (game.gameState.purchaseState.isPro) {
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

    public void openAdultGateBanner(Runnable task) {
        super.openAdultGateBanner(task);

        try {

            destroyBanners();

            if (!game.params.isAdultGateEnabled) {
                if (task != null) {
                    task.run();
                }
                return;
            }

            adultGateFragment = new AdultGateFragment(this);
            adultGateFragment.create(task);

            layers.setBannerLayer(adultGateFragment);

            adultGateFragment.fadeIn();

        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }

    public void openRateBanner() {
        try {

            destroyBanners();

            rateFragment = new RateFragment(this);
            rateFragment.create();

            layers.setBannerLayer(rateFragment);

            rateFragment.fadeIn();

        } catch (Throwable e) {
            Log.e(tag, e);

            onCriticalError(e);
        }
    }

    public void openReferralBanner() {
        try {

            destroyBanners();

            referalFragment = new ReferalFragment(this);
            referalFragment.create();

            layers.setBannerLayer(referalFragment);

            referalFragment.fadeIn();

        } catch (Throwable e) {
            Log.e(tag, e);

            onCriticalError(e);
        }
    }

    public boolean openReferralThankYouBanner() {
        try {

            MenuState state = game.gameState.menuState;

            if (state != null && state.referralsToDisplay != null) {
                if (!state.referralsToDisplay.isEmpty()) {

                    String type = state.referralsToDisplay.pop();

                    switch (type) {
                        case ReferralService.REFERRAL_TYPE_1:

                            destroyBanners();

                            referalThanks3Fragment = new ReferalThanks3Fragment(this);
                            referalThanks3Fragment.create();

                            layers.setBannerLayer(referalThanks3Fragment);

                            referalThanks3Fragment.fadeIn();

                            break;
                        case ReferralService.REFERRAL_TYPE_2:

                            destroyBanners();

                            referalThanks7Fragment = new ReferalThanks7Fragment(this);
                            referalThanks7Fragment.create();

                            layers.setBannerLayer(referalThanks7Fragment);

                            referalThanks7Fragment.fadeIn();

                            break;

                    }


                    return true;
                }
            }

        } catch (Throwable e) {
            Log.e(tag, e);
        }

        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        try {

            if (layers == null) return;
            if (exitFragment != null) return;

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

        try {

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

            if (achievementFragment != null) {
                achievementFragment.update();
            }

            if (isZoomStarted) {
                game.camera.zoom -= .4f * delta;
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

            destroyBanners();

            if (audioService != null) {
                audioService.dispose();
                audioService = null;
            }

            if (layers != null) {
                layers.dispose();
                layers = null;
            }

        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }

    public void scaleAndNavigateTo(final Screen screen) {

        isZoomStarted = true;

        controlsFragment.fadeOutFancy();

        if (game.backgroundSfxService != null) {
            game.backgroundSfxService.fade();
        }

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
        }, 2.2f);
    }

    @Override
    public void navigateTo(Screen screen) {

        if (game.backgroundSfxService != null) {
            game.backgroundSfxService.fade();
        }

        super.navigateTo(screen);
    }

    public void lockUI() {
        Gdx.input.setInputProcessor(null);
        isUILocked = true;
    }

    public void unlockUI() {
        Gdx.input.setInputProcessor(ui);
        isUILocked = true;
    }
}
