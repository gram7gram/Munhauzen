package ua.gram.munhauzen.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;

import java.util.Collections;
import java.util.Comparator;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.Chapter;
import ua.gram.munhauzen.screen.chapters.fragment.BannerFragment;
import ua.gram.munhauzen.screen.chapters.fragment.ChaptersFragment;
import ua.gram.munhauzen.screen.chapters.fragment.ControlsFragment;
import ua.gram.munhauzen.screen.chapters.ui.Layers;
import ua.gram.munhauzen.service.AudioService;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class ChaptersScreen extends AbstractScreen {

    public AudioService audioService;
    public Layers layers;
    public ChaptersFragment fragment;
    public ControlsFragment controlsFragment;
    public BannerFragment banner;

    public ChaptersScreen(MunhauzenGame game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();

        background = game.internalAssetManager.get("p1.jpg", Texture.class);

        audioService = new AudioService(game);

        layers = new Layers();

        ui.addActor(layers);

        assetManager.load("ui/b_sound_on.png", Texture.class);
        assetManager.load("ui/b_sound_off.png", Texture.class);
        assetManager.load("menu/b_menu.png", Texture.class);
        assetManager.load("ui/gv_paper_1.png", Texture.class);
        assetManager.load("ui/gv_paper_2.png", Texture.class);
        assetManager.load("ui/gv_paper_3.png", Texture.class);
        assetManager.load("saves/sv_baron.png", Texture.class);
        assetManager.load("gallery/b_closed_0.png", Texture.class);
        assetManager.load("ui/banner_fond_0.png", Texture.class);
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
            Collections.sort(game.gameState.chapterRegistry, new Comparator<Chapter>() {
                @Override
                public int compare(Chapter a, Chapter b) {
                    return Integer.compare(a.number, b.number);
                }
            });

            for (Chapter chapter : game.gameState.chapterRegistry) {
                if (game.gameState.history.visitedChapters.contains(chapter.name)) {
                    assetManager.load(chapter.icon, Texture.class);
                }
            }

            assetManager.finishLoading();

            controlsFragment = new ControlsFragment(this);
            controlsFragment.create();

            layers.setControlsLayer(controlsFragment);

            fragment = new ChaptersFragment(this);
            fragment.create();

            layers.setContentLayer(fragment);

            fragment.fadeIn();
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

        if (fragment != null) {
            fragment.update();
        }
    }

    @Override
    public void dispose() {
        super.dispose();

        destroyBanners();

        if (fragment != null) {
            fragment.destroy();
            fragment = null;
        }

        if (controlsFragment != null) {
            controlsFragment.destroy();
            controlsFragment = null;
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

    public void destroyBanners() {
        if (banner != null) {
            banner.destroy();
            banner = null;
        }
    }

    public void openChapterBanner(Chapter chapter) {
        try {

            destroyBanners();

            banner = new BannerFragment(this);
            banner.create(chapter);

            layers.setBannerLayer(banner);

            banner.fadeIn();

        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }
}
