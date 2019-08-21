package ua.gram.munhauzen.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Timer;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.screen.gallery.entity.PaintingImage;
import ua.gram.munhauzen.screen.painting.fragment.ControlsFragment;
import ua.gram.munhauzen.screen.painting.fragment.FullscreenFragment;
import ua.gram.munhauzen.screen.painting.fragment.ImageFragment;
import ua.gram.munhauzen.screen.painting.ui.PaintingLayers;
import ua.gram.munhauzen.utils.Log;

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
    }

    @Override
    public void show() {
        super.show();

        background = game.assetManager.get("p1.jpg", Texture.class);

        assetManager.load("ui/gv_paper_3.png", Texture.class);
        assetManager.load("gallery/b_closed_1.png", Texture.class);
        assetManager.load("gallery/b_opened_1.png", Texture.class);
        assetManager.load("ui/playbar_skip_forward.png", Texture.class);
        assetManager.load("ui/playbar_skip_forward_off.png", Texture.class);
        assetManager.load("ui/playbar_skip_backward.png", Texture.class);
        assetManager.load("ui/playbar_skip_backward_off.png", Texture.class);

        layers = new PaintingLayers();

        ui.addActor(layers);

        Gdx.input.setInputProcessor(ui);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        navigateTo(new GalleryScreen(game));
    }

    @Override
    public void onResourcesLoaded() {
        super.onResourcesLoaded();

        imageFragment = new ImageFragment(this);
        imageFragment.create(paintingImage);

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

        if (controlsFragment != null) {
            controlsFragment.update();
        }
    }

    public void nextPainting() {

        final PaintingImage next = imageFragment.paintingImage.next;
        if (next == null) return;

        Log.i(tag, "nextPainting " + next.image.name);

        imageFragment.fadeOutLeft(new Runnable() {
            @Override
            public void run() {

                try {

                    imageFragment.destroy();
                    imageFragment = null;

                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }
        });

        Timer.instance().scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                try {

                    imageFragment = new ImageFragment(PaintingScreen.this);
                    imageFragment.create(next);

                    layers.setContentLayer(imageFragment);

                    imageFragment.fadeInLeft();

                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }
        }, .21f);
    }

    public void prevPainting() {
        final PaintingImage prev = imageFragment.paintingImage.prev;
        if (prev == null) return;

        Log.i(tag, "prevPainting " + prev.image.name);

        final ImageFragment prevFragment = new ImageFragment(this);
        prevFragment.create(prev);

        imageFragment.fadeOutRight(new Runnable() {
            @Override
            public void run() {

                try {

                    imageFragment.destroy();
                    imageFragment = null;

                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }
        });

        Timer.instance().scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                try {

                    imageFragment = new ImageFragment(PaintingScreen.this);
                    imageFragment.create(prev);

                    layers.setContentLayer(imageFragment);

                    imageFragment.fadeInRight();

                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }
        }, .21f);
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

        if (fullscreenFragment != null) {
            fullscreenFragment.destroy();
            fullscreenFragment = null;
        }
    }
}
