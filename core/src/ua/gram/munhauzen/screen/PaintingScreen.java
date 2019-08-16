package ua.gram.munhauzen.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.Image;
import ua.gram.munhauzen.entity.Inventory;
import ua.gram.munhauzen.screen.painting.fragment.ControlsFragment;
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
    public final Image image;
    public Inventory inventory;
    public String imageResource, statueResource;

    public PaintingScreen(MunhauzenGame game, Image image) {
        super(game);

        this.image = image;
    }

    @Override
    public void show() {
        super.show();

        background = game.assetManager.get("p1.jpg", Texture.class);

        imageResource = ExternalFiles.getExpansionImage(image).path();

        assetManager.load(imageResource, Texture.class);

        if ("statue".equals(image.type)) {

            for (Inventory item : game.gameState.inventoryRegistry) {
                if (item.name.equals(image.relatedStatue)) {
                    inventory = item;
                    statueResource = inventory.statueImage;
                    break;
                }
            }

            assetManager.load(statueResource, Texture.class);
            assetManager.load("gallery/gv2_statue.png", Texture.class);
            assetManager.load("ui/banner_fond_3.png", Texture.class);
            assetManager.load("gallery/gv2_frame_3.png", Texture.class);

        } else if ("bonus".equals(image.type)) {

            assetManager.load("gallery/gv2_frame_4.png", Texture.class);
            assetManager.load("gallery/gv2_bonus_back.png", Texture.class);
            assetManager.load("gallery/gv2_bonus_stick.png", Texture.class);

        } else if ("color".equals(image.type)) {

            assetManager.load("gallery/gv2_frame_2.png", Texture.class);

        } else {

            assetManager.load("gallery/gv2_frame_1.png", Texture.class);

        }

        assetManager.load("gallery/gv_paper_3.png", Texture.class);

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
