package ua.gram.munhauzen.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.screen.gallery.fragment.ControlsFragment;
import ua.gram.munhauzen.screen.gallery.fragment.GalleryFragment;
import ua.gram.munhauzen.screen.gallery.ui.GalleryLayers;
import ua.gram.munhauzen.screen.saves.fragment.PaintingFragment;
import ua.gram.munhauzen.service.AudioService;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class GalleryScreen extends AbstractScreen {

    public AudioService audioService;
    public GalleryLayers layers;
    public GalleryFragment galleryFragment;
    public ControlsFragment controlsFragment;
    public PaintingFragment paintingFragment;

    public GalleryScreen(MunhauzenGame game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();

        background = game.assetManager.get("p1.jpg", Texture.class);
        audioService = new AudioService(game);

        assetManager.load("menu/b_menu.png", Texture.class);
        assetManager.load("gallery/gv_paper_1.png", Texture.class);
        assetManager.load("gallery/gv_paper_2.png", Texture.class);
        assetManager.load("gallery/gv_paper_3.png", Texture.class);
        assetManager.load("gallery/gv_painting.png", Texture.class);
        assetManager.load("gallery/b_closed_0.png", Texture.class);
        assetManager.load("gallery/b_opened_0.png", Texture.class);

        assetManager.load("ui/banner_fond_3.png", Texture.class);

        layers = new GalleryLayers();

        ui.addActor(layers);

        Gdx.input.setInputProcessor(ui);
    }

    @Override
    public void onResourcesLoaded() {
        super.onResourcesLoaded();

        controlsFragment = new ControlsFragment(this);
        controlsFragment.create();

        layers.setControlsLayer(controlsFragment);

        controlsFragment.fadeIn();

        galleryFragment = new GalleryFragment(this);
        galleryFragment.create();

        layers.setContentLayer(galleryFragment);

        galleryFragment.fadeIn();
    }

    @Override
    public void renderAfterLoaded(float delta) {

        if (galleryFragment != null) {
            galleryFragment.update();
        }

        if (paintingFragment != null) {
            paintingFragment.update();
        }
    }

    @Override
    public void dispose() {
        super.dispose();

        audioService.dispose();

        layers.dispose();

        if (galleryFragment != null) {
            galleryFragment.destroy();
            galleryFragment = null;
        }

        if (controlsFragment != null) {
            controlsFragment.destroy();
            controlsFragment = null;
        }

        if (paintingFragment != null) {
            paintingFragment.destroy();
            paintingFragment = null;
        }
    }
}
