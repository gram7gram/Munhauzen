package ua.gram.munhauzen.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.screen.authors.fragment.AuthorsFragment;
import ua.gram.munhauzen.screen.authors.fragment.ControlsFragment;
import ua.gram.munhauzen.screen.authors.fragment.DemoFragment;
import ua.gram.munhauzen.screen.authors.fragment.EnAuthorsFragment;
import ua.gram.munhauzen.screen.authors.fragment.ProFragment;
import ua.gram.munhauzen.screen.authors.fragment.RateFragment;
import ua.gram.munhauzen.screen.authors.fragment.RuAuthorsFragment;
import ua.gram.munhauzen.screen.authors.fragment.ShareFragment;
import ua.gram.munhauzen.screen.authors.ui.AuthorsLayers;
import ua.gram.munhauzen.service.AudioService;
import ua.gram.munhauzen.ui.AdultGateFragment;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class AuthorsScreen extends AbstractScreen {

    public AudioService audioService;
    public AuthorsLayers layers;
    public AuthorsFragment authorsFragment;
    public ControlsFragment controlsFragment;
    public RateFragment rateFragment;
    public ShareFragment shareFragment;
    public DemoFragment demoFragment;
    public ProFragment proFragment;
    public AdultGateFragment adultGateFragment;

    public AuthorsScreen(MunhauzenGame game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();

        background = game.internalAssetManager.get("p1.jpg", Texture.class);

        audioService = new AudioService(game);

        assetManager.load("menu/b_menu.png", Texture.class);
        assetManager.load("ui/b_sound_on.png", Texture.class);
        assetManager.load("ui/b_sound_off.png", Texture.class);
        assetManager.load("ui/gv_paper_1.png", Texture.class);
        assetManager.load("ui/gv_paper_2.png", Texture.class);
        assetManager.load("ui/gv_paper_3.png", Texture.class);
        assetManager.load("menu/b_rate_2.png", Texture.class);
        assetManager.load("menu/b_share_2.png", Texture.class);

        assetManager.load("authors/author_1.png", Texture.class);
        assetManager.load("authors/author_2.png", Texture.class);
        assetManager.load("authors/author_5.png", Texture.class);
        assetManager.load("authors/author_6.png", Texture.class);
        assetManager.load("authors/Author_7.png", Texture.class);

        switch (game.params.locale) {
            case "ru":
                assetManager.load("authors/author_3_2.png", Texture.class);
                assetManager.load("authors/author_4_2.png", Texture.class);
                break;
            case "en":
                assetManager.load("authors/author_3_1.png", Texture.class);
                assetManager.load("authors/author_4_1.png", Texture.class);
                break;
        }

        assetManager.load(game.gameState.purchaseState.isPro
                ? "menu/b_full_version_2.png"
                : "menu/b_demo_version_2.png", Texture.class);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        game.stopAllAudio();

        game.sfxService.onBackToMenuClicked();

        navigateTo(new MenuScreen(game));
    }

    @Override
    public void onResourcesLoaded() {
        super.onResourcesLoaded();

        try {

            layers = new AuthorsLayers();

            ui.addActor(layers);

            controlsFragment = new ControlsFragment(this);
            controlsFragment.create();

            layers.setControlsLayer(controlsFragment);

            controlsFragment.fadeIn();

            switch (game.params.locale) {
                case "ru":
                    authorsFragment = new RuAuthorsFragment(this);
                    break;
                case "en":
                    authorsFragment = new EnAuthorsFragment(this);
                    break;
            }

            authorsFragment.create();

            layers.setContentLayer(authorsFragment);

            authorsFragment.fadeIn();
        } catch (Throwable e) {
            Log.e(tag, e);

            onCriticalError(e);
        }
    }

    @Override
    public void fillBackgroundColor() {
        Gdx.gl.glClearColor(137 / 255f, 60 / 255f, 54 / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
    }

    @Override
    public void renderAfterLoaded(float delta) {

        if (authorsFragment != null) {
            authorsFragment.update();
        }

        if (controlsFragment != null) {
            controlsFragment.update();
        }
    }

    public void destroyBanners() {
        if (rateFragment != null) {
            rateFragment.destroy();
            rateFragment = null;
        }
        if (shareFragment != null) {
            shareFragment.destroy();
            shareFragment = null;
        }
        if (demoFragment != null) {
            demoFragment.destroy();
            demoFragment = null;
        }
        if (proFragment != null) {
            proFragment.destroy();
            proFragment = null;
        }

        if (adultGateFragment != null) {
            adultGateFragment.destroy();
            adultGateFragment = null;
        }
    }

    public void openRateBanner() {
        try {

            if (layers == null) return;

            rateFragment = new RateFragment(this);
            rateFragment.create();

            layers.setBannerLayer(rateFragment);

            rateFragment.fadeIn();
        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }

    public void openVersionBanner() {
        try {

            if (layers == null) return;

            if (game.gameState.purchaseState.isPro) {
                proFragment = new ProFragment(this);
                proFragment.create();

                layers.setBannerLayer(proFragment);

                proFragment.fadeIn();
            } else {
                demoFragment = new DemoFragment(this);
                demoFragment.create();

                layers.setBannerLayer(demoFragment);

                demoFragment.fadeIn();
            }
        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }

    public void openShareBanner() {
        try {

            if (layers == null) return;

            shareFragment = new ShareFragment(this);
            shareFragment.create();

            layers.setBannerLayer(shareFragment);

            shareFragment.fadeIn();
        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }

    @Override
    public void dispose() {
        super.dispose();

        destroyBanners();

        if (audioService != null) {
            audioService.dispose();
            audioService = null;
        }

        if (layers != null) {
            layers.dispose();
            layers = null;
        }

        if (authorsFragment != null) {
            authorsFragment.destroy();
            authorsFragment = null;
        }

        if (controlsFragment != null) {
            controlsFragment.destroy();
            controlsFragment = null;
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
}
