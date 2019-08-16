package ua.gram.munhauzen.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.Inventory;
import ua.gram.munhauzen.screen.gallery.entity.PaintingImage;
import ua.gram.munhauzen.screen.painting.fragment.ControlsFragment;
import ua.gram.munhauzen.screen.painting.fragment.FullscreenFragment;
import ua.gram.munhauzen.screen.painting.fragment.ImageFragment;
import ua.gram.munhauzen.screen.painting.ui.PaintingLayers;
import ua.gram.munhauzen.utils.ExternalFiles;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class PaintingScreen extends AbstractScreen {

    public PaintingLayers layers;
    public ImageFragment imageFragment;
    public ControlsFragment controlsFragment;
    public FullscreenFragment fullscreenFragment;

    public PaintingImage paintingImage;

    public PaintingScreen(MunhauzenGame game, PaintingImage image) {
        super(game);

        paintingImage = image;
//        image.image.type = "statue";
//        image.image.relatedStatue = "ST_LION";
    }

    @Override
    public void show() {
        super.show();

        background = game.assetManager.get("p1.jpg", Texture.class);

        paintingImage.imageResource = ExternalFiles.getExpansionImage(paintingImage.image).path();

        if (paintingImage.isOpened) {
            assetManager.load(paintingImage.imageResource, Texture.class);
        } else {
            assetManager.load("gallery/aquestion.png", Texture.class);
        }

        if ("statue".equals(paintingImage.image.type)) {

            for (Inventory item : game.gameState.inventoryRegistry) {
                if (item.name.equals(paintingImage.image.relatedStatue)) {
                    paintingImage.inventory = item;
                    paintingImage.statueResource = item.statueImage;// = "gallery/st_lion.png";
                    break;
                }
            }

            if (paintingImage.isOpened) {
                assetManager.load(paintingImage.statueResource, Texture.class);
            }

            assetManager.load("gallery/gv2_statue.png", Texture.class);
            assetManager.load("ui/banner_fond_3.png", Texture.class);
            assetManager.load("gallery/gv2_frame_3.png", Texture.class);

        } else if ("bonus".equals(paintingImage.image.type)) {

            assetManager.load("gallery/gv2_frame_4.png", Texture.class);
            assetManager.load("gallery/gv2_bonus_back.png", Texture.class);
            assetManager.load("gallery/gv2_bonus_stick.png", Texture.class);

        } else if ("color".equals(paintingImage.image.type)) {

            assetManager.load("gallery/gv2_frame_2.png", Texture.class);

        } else {

            assetManager.load("gallery/gv2_frame_1.png", Texture.class);

        }

        assetManager.load("gallery/gv_paper_3.png", Texture.class);
        assetManager.load("gallery/b_closed_1.png", Texture.class);
        assetManager.load("gallery/b_opened_1.png", Texture.class);

        layers = new PaintingLayers();

        ui.addActor(layers);

        Gdx.input.setInputProcessor(ui);
    }

    @Override
    public void onResourcesLoaded() {
        super.onResourcesLoaded();

        imageFragment = new ImageFragment(this);
        imageFragment.create();

        layers.setContentLayer(imageFragment);

        imageFragment.fadeIn();

        controlsFragment = new ControlsFragment(this);
        controlsFragment.create();

        layers.setControlsLayer(controlsFragment);
    }

    @Override
    public void renderAfterLoaded(float delta) {

        if (imageFragment != null) {
            imageFragment.update();
        }
    }

    @Override
    public void dispose() {
        super.dispose();

        if (layers != null) {
            layers.dispose();
            layers = null;
        }

        if (imageFragment != null) {
            imageFragment.destroy();
            imageFragment = null;
        }

        if (controlsFragment != null) {
            controlsFragment.destroy();
            controlsFragment = null;
        }
    }
}
