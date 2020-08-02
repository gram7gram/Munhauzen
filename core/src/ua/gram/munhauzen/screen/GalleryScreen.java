package ua.gram.munhauzen.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Timer;

import java.util.ArrayList;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.GalleryState;
import ua.gram.munhauzen.entity.Image;
import ua.gram.munhauzen.entity.Inventory;
import ua.gram.munhauzen.screen.gallery.entity.PaintingImage;
import ua.gram.munhauzen.screen.gallery.fragment.ControlsFragment;
import ua.gram.munhauzen.screen.gallery.fragment.GalleryBannerFragment;
import ua.gram.munhauzen.screen.gallery.fragment.GalleryFragment;
import ua.gram.munhauzen.screen.gallery.ui.GalleryLayers;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class GalleryScreen extends AbstractScreen {

    public GalleryLayers layers;
    public GalleryFragment galleryFragment;
    public ControlsFragment controlsFragment;
    public ArrayList<PaintingImage> paintings;
    public GalleryBannerFragment galleryBannerFragment;

    public GalleryScreen(MunhauzenGame game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();

        paintings = new ArrayList<>();

        background = game.internalAssetManager.get("p1.jpg", Texture.class);

        assetManager.load("menu/b_menu.png", Texture.class);
        assetManager.load("ui/gv_paper_1.png", Texture.class);
        assetManager.load("ui/gv_paper_2.png", Texture.class);
        assetManager.load("ui/gv_paper_3.png", Texture.class);
        assetManager.load("gallery/gv_painting.png", Texture.class);
        assetManager.load("gallery/b_closed_0.png", Texture.class);
        assetManager.load("gallery/b_opened_0.png", Texture.class);
        assetManager.load("gallery/b_star_black.png", Texture.class);
        assetManager.load("gallery/b_star_color.png", Texture.class);

        assetManager.load("ui/banner_fond_3.png", Texture.class);

        layers = new GalleryLayers();

        ui.addActor(layers);
    }

    @Override
    public void onResourcesLoaded() {
        super.onResourcesLoaded();

        try {

            createPaintings();

            controlsFragment = new ControlsFragment(this);
            controlsFragment.create();

            layers.setControlsLayer(controlsFragment);

            galleryFragment = new GalleryFragment(this);
            galleryFragment.create();

            layers.setContentLayer(galleryFragment);

            galleryFragment.fadeIn();

            if (game.gameState.galleryState != null) {
                if (!game.gameState.galleryState.isGalleryBannerViewed) {
                    game.gameState.galleryState.isGalleryBannerViewed = true;

                    Timer.instance().scheduleTask(new Timer.Task() {
                        @Override
                        public void run() {
                            openGalleryBanner();
                        }
                    }, .5f);
                }
            }
        } catch (Throwable e) {
            Log.e(tag, e);

            onCriticalError(e);
        }
    }

    private void createPaintings() {
        ArrayList<Image> galleryImages = game.gameState.getGalleryImages();

        for (Image image : galleryImages) {

            PaintingImage painting = new PaintingImage();
            painting.image = image;

            paintings.add(painting);

            painting.isOpened = game.gameState.history.viewedImages.contains(image.name);
            painting.isViewed = game.gameState.galleryState.visitedImages.contains(image.name);

            if (painting.image.relatedStatue != null) {
                for (Inventory item : game.gameState.inventoryRegistry) {
                    if (item.name.equals(painting.image.relatedStatue)) {
                        painting.inventory = item;
                        painting.statueResource = item.statueImage;
                        break;
                    }
                }
            }
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

    public void openGalleryBanner() {
        try {

            if (assetManager == null) return;

            galleryBannerFragment = new GalleryBannerFragment(this);
            galleryBannerFragment.create();

            layers.setBannerLayer(galleryBannerFragment);

            galleryBannerFragment.fadeIn();

        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        beforeGalleryLeave();

        game.stopAllAudio();

        game.sfxService.onBackToMenuClicked();

        navigateTo(new MenuScreen(game));
    }

    public void beforeGalleryLeave() {
        GalleryState state = game.gameState.galleryState;

        state.scrollY = 0;
    }

    @Override
    public void fillBackgroundColor() {
        Gdx.gl.glClearColor(137 / 255f, 60 / 255f, 54 / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
    }

    @Override
    public void renderAfterLoaded(float delta) {

        if (galleryFragment != null) {
            galleryFragment.update();
        }
    }

    public void destroyBanners() {
        if (galleryBannerFragment != null) {
            galleryBannerFragment.destroy();
            galleryBannerFragment = null;
        }
    }

    @Override
    public void dispose() {
        super.dispose();

        destroyBanners();

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
