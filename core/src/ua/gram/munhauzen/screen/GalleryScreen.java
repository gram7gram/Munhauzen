package ua.gram.munhauzen.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.GalleryState;
import ua.gram.munhauzen.entity.Image;
import ua.gram.munhauzen.screen.gallery.entity.PaintingImage;
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
    public ArrayList<PaintingImage> paintings;

    public GalleryScreen(MunhauzenGame game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();

        paintings = new ArrayList<>();

        background = game.assetManager.get("p1.jpg", Texture.class);

        assetManager.load("menu/b_menu.png", Texture.class);
        assetManager.load("ui/gv_paper_1.png", Texture.class);
        assetManager.load("ui/gv_paper_2.png", Texture.class);
        assetManager.load("ui/gv_paper_3.png", Texture.class);
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

        createPaintings();

        controlsFragment = new ControlsFragment(this);
        controlsFragment.create();

        layers.setControlsLayer(controlsFragment);

        galleryFragment = new GalleryFragment(this);
        galleryFragment.create();

        layers.setContentLayer(galleryFragment);

        galleryFragment.fadeIn();
    }

    private void createPaintings() {
        ArrayList<Image> galleryImages = game.gameState.getGalleryImages();

        for (Image image : galleryImages) {

            PaintingImage painting = new PaintingImage();
            painting.image = image;

            paintings.add(painting);

            painting.isOpened = game.gameState.history.viewedImages.contains(image.name);
            painting.isViewed = game.gameState.galleryState.visitedImages.contains(image.name);
        }

        int size = paintings.size();
        for (int i = 0; i < size; i++) {
            PaintingImage current = paintings.get(i);

            PaintingImage next = null;
            PaintingImage prev = null;
            if (size > 1) {
                if (i == 0) {
                    next = paintings.get(i + 1);
                } else if (i == size - 1) {
                    prev = paintings.get(i - 1);
                } else {
                    next = paintings.get(i + 1);
                    prev = paintings.get(i - 1);
                }
            }

            current.prev = prev;
            current.next = next;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        beforeGalleryLeave();

        game.sfxService.onBackToMenuClicked();

        navigateTo(new MenuScreen(game));
    }

    public void beforeGalleryLeave() {
        GalleryState state = game.gameState.galleryState;

        state.scrollY = 0;
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

        if (paintings != null) {
            paintings.clear();
            paintings = null;
        }

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
