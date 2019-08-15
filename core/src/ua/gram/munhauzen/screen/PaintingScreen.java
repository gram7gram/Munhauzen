package ua.gram.munhauzen.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.Image;
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
    public String imageResource;

    public PaintingScreen(MunhauzenGame game, Image image) {
        super(game);

        this.image = image;
//        this.image.type = "color";
    }

    @Override
    public void show() {
        super.show();

        background = game.assetManager.get("p1.jpg", Texture.class);

        imageResource = ExternalFiles.getExpansionImage(image).path();

        assetManager.load(imageResource, Texture.class);
        assetManager.load("gallery/gv_paper_3.png", Texture.class);
        assetManager.load("gallery/gv2_frame_1.png", Texture.class);
        assetManager.load("gallery/gv2_frame_2.png", Texture.class);
        assetManager.load("gallery/gv2_frame_3.png", Texture.class);
        assetManager.load("gallery/gv2_frame_4.png", Texture.class);

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
