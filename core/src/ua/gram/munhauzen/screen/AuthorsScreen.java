package ua.gram.munhauzen.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.screen.authors.fragment.AuthorsFragment;
import ua.gram.munhauzen.screen.authors.fragment.ControlsFragment;
import ua.gram.munhauzen.screen.authors.fragment.EnAuthorsFragment;
import ua.gram.munhauzen.screen.authors.fragment.RuAuthorsFragment;
import ua.gram.munhauzen.screen.authors.ui.AuthorsLayers;
import ua.gram.munhauzen.service.AudioService;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class AuthorsScreen extends AbstractScreen {

    public AudioService audioService;
    public AuthorsLayers layers;
    public AuthorsFragment authorsFragment;
    public ControlsFragment controlsFragment;

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
        assetManager.load("authors/author_7.png", Texture.class);

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

        assetManager.load(game.params.isPro
                ? "menu/b_full_version_2.png"
                : "menu/b_demo_version_2.png", Texture.class);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        stopCurrentSfx();

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

    @Override
    public void dispose() {
        super.dispose();

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
