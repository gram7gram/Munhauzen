package ua.gram.munhauzen.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.Image;
import ua.gram.munhauzen.screen.gallery.fragment.ControlsFragment;
import ua.gram.munhauzen.screen.gallery.fragment.GalleryFragment;
import ua.gram.munhauzen.screen.gallery.ui.GalleryLayers;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class GalleryScreen extends AbstractScreen {

    public GalleryLayers layers;
    public GalleryFragment galleryFragment;
    public ControlsFragment controlsFragment;

    public GalleryScreen(MunhauzenGame game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();

        background = game.assetManager.get("p1.jpg", Texture.class);

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

        for (Image image : game.gameState.imageRegistry) {
            game.gameState.history.viewedImages.add(image.name);
        }

        controlsFragment = new ControlsFragment(this);
        controlsFragment.create();

        layers.setControlsLayer(controlsFragment);

        galleryFragment = new GalleryFragment(this);
        galleryFragment.create();

        layers.setContentLayer(galleryFragment);

        //galleryFragment.fadeIn();
    }

    @Override
    public void renderAfterLoaded(float delta) {

        if (galleryFragment != null) {
            galleryFragment.update();
        }
    }

    @Override
    public void dispose() {
        super.dispose();

        if (layers != null) {
            layers.dispose();
            layers = null;
        }

        if (galleryFragment != null) {
            galleryFragment.destroy();
            galleryFragment = null;
        }

        if (controlsFragment != null) {
            controlsFragment.destroy();
            controlsFragment = null;
        }
    }
}
