package ua.gram.munhauzen.screen;

import com.badlogic.gdx.graphics.Texture;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.StoryAudio;
import ua.gram.munhauzen.screen.authors.fragment.AuthorsFragment;
import ua.gram.munhauzen.screen.authors.fragment.ControlsFragment;
import ua.gram.munhauzen.screen.authors.ui.AuthorsLayers;
import ua.gram.munhauzen.service.AudioService;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class AuthorsScreen extends AbstractScreen {

    public AudioService audioService;
    public AuthorsLayers layers;
    public AuthorsFragment authorsFragment;
    public ControlsFragment controlsFragment;
    private StoryAudio intro;

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

        assetManager.load(game.params.isPro
                ? "menu/b_full_version_2.png"
                : "menu/b_demo_version_2.png", Texture.class);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        game.sfxService.onBackToMenuClicked();

        navigateTo(new MenuScreen(game));
    }

    @Override
    public void onResourcesLoaded() {
        super.onResourcesLoaded();

        layers = new AuthorsLayers();

        ui.addActor(layers);

        controlsFragment = new ControlsFragment(this);
        controlsFragment.create();

        layers.setControlsLayer(controlsFragment);

        controlsFragment.fadeIn();

        authorsFragment = new AuthorsFragment(this);
        authorsFragment.create();

        layers.setContentLayer(authorsFragment);

        authorsFragment.fadeIn();

        playIntro();
    }

    private void playIntro() {
        try {
            intro = new StoryAudio();
            intro.audio = "sfx_authors";

            audioService.prepareAndPlay(intro);
        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }

    @Override
    public void renderAfterLoaded(float delta) {

        if (intro != null) {
            audioService.updateVolume(intro);
        }

        if (authorsFragment != null) {
            authorsFragment.update();
        }

        if (controlsFragment != null) {
            controlsFragment.update();
        }
    }

    @Override
    public void dispose() {
        super.dispose();

        if (intro != null) {
            audioService.stop(intro);
            intro = null;
        }

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
}
