package ua.gram.munhauzen.screen;

import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.AudioFail;
import ua.gram.munhauzen.entity.StoryAudio;
import ua.gram.munhauzen.screen.fails.entity.GalleryFail;
import ua.gram.munhauzen.screen.fails.fragment.ControlsFragment;
import ua.gram.munhauzen.screen.fails.fragment.FailsFragment;
import ua.gram.munhauzen.screen.fails.ui.FailsLayers;

public class FailsScreen extends AbstractScreen {

    public FailsLayers layers;
    public FailsFragment failsFragment;
    public ControlsFragment controlsFragment;
    public ArrayList<GalleryFail> fails;

    public FailsScreen(MunhauzenGame game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();

        fails = new ArrayList<>();

        background = game.assetManager.get("p1.jpg", Texture.class);

        assetManager.load("menu/b_menu.png", Texture.class);
        assetManager.load("ui/gv_paper_1.png", Texture.class);
        assetManager.load("ui/gv_paper_2.png", Texture.class);
        assetManager.load("ui/gv_paper_3.png", Texture.class);
        assetManager.load("gallery/gv_painting.png", Texture.class);
        assetManager.load("gallery/b_closed_0.png", Texture.class);
        assetManager.load("gallery/b_opened_0.png", Texture.class);

        assetManager.load("ui/banner_fond_3.png", Texture.class);

        layers = new FailsLayers();

        ui.addActor(layers);
    }

    @Override
    public void onResourcesLoaded() {
        super.onResourcesLoaded();

        createPaintings();

        controlsFragment = new ControlsFragment(this);
        controlsFragment.create();

        layers.setControlsLayer(controlsFragment);

        failsFragment = new FailsFragment(this);
        failsFragment.create();

        layers.setContentLayer(failsFragment);

        failsFragment.fadeIn();
    }

    private void createPaintings() {

        for (AudioFail audioFail : game.gameState.audioFailRegistry) {

            StoryAudio storyAudio = new StoryAudio();
            storyAudio.name = audioFail.name;

            GalleryFail fail = new GalleryFail();
            fail.storyAudio = storyAudio;

            fails.add(fail);

            fail.isOpened = game.gameState.history.viewedImages.contains(audioFail.name);
            fail.isViewed = game.gameState.galleryState.visitedImages.contains(audioFail.name);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        navigateTo(new MenuScreen(game));
    }

    @Override
    public void renderAfterLoaded(float delta) {

        if (failsFragment != null) {
            failsFragment.update();
        }

        if (controlsFragment != null) {
            controlsFragment.update();
        }
    }

    @Override
    public void dispose() {
        super.dispose();

        if (fails != null) {
            fails.clear();
            fails = null;
        }

        if (layers != null) {
            layers.dispose();
            layers = null;
        }

        if (failsFragment != null) {
            failsFragment.destroy();
            failsFragment = null;
        }

        if (controlsFragment != null) {
            controlsFragment.destroy();
            controlsFragment = null;
        }
    }
}
