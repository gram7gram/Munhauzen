package ua.gram.munhauzen.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Timer;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.screen.gallery.entity.PaintingImage;
import ua.gram.munhauzen.screen.painting.fragment.ControlsFragment;
import ua.gram.munhauzen.screen.painting.fragment.FullscreenFragment;
import ua.gram.munhauzen.screen.painting.fragment.PaintingFragment;
import ua.gram.munhauzen.screen.painting.ui.PaintingLayers;
import ua.gram.munhauzen.ui.AdultGateFragment;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class PaintingScreen extends AbstractScreen {

    public PaintingLayers layers;
    public PaintingFragment paintingFragment;
    public ControlsFragment controlsFragment;
    public FullscreenFragment fullscreenFragment;
    public AdultGateFragment adultGateFragment;

    public PaintingImage paintingImage;

    public PaintingScreen(MunhauzenGame game, PaintingImage image) {
        super(game);

        paintingImage = image;
    }

    @Override
    public void show() {
        super.show();

        background = game.internalAssetManager.get("p1.jpg", Texture.class);

        assetManager.load("ui/gv_paper_3.png", Texture.class);
        assetManager.load("gallery/b_closed_1.png", Texture.class);
        assetManager.load("gallery/b_opened_3.png", Texture.class);
        assetManager.load("ui/playbar_skip_forward.png", Texture.class);
        assetManager.load("ui/playbar_skip_backward.png", Texture.class);

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

        try {

            paintingFragment = new PaintingFragment(this);
            paintingFragment.create(paintingImage);

            layers.setContentLayer(paintingFragment);

            paintingFragment.fadeIn();

            controlsFragment = new ControlsFragment(this);
            controlsFragment.create();

            layers.setControlsLayer(controlsFragment);

            game.gameState.galleryState.visitedImages.add(paintingImage.image.name);
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

        if (paintingFragment != null) {
            paintingFragment.update();
        }

        if (controlsFragment != null) {
            controlsFragment.update();
        }
    }

    public void nextPainting() {

        final PaintingImage next = paintingFragment.paintingImage.next;
        if (next == null) return;

        try {
            Log.i(tag, "nextPainting " + next.image.name);

            game.sfxService.onGalleryArrowClick();

            paintingFragment.fadeOutLeft(new Runnable() {
                @Override
                public void run() {

                    try {

                        paintingFragment.destroy();
                        paintingFragment = null;

                    } catch (Throwable e) {
                        Log.e(tag, e);
                    }
                }
            });

            Timer.instance().scheduleTask(new Timer.Task() {
                @Override
                public void run() {
                    try {

                        paintingFragment = new PaintingFragment(PaintingScreen.this);
                        paintingFragment.create(next);

                        layers.setContentLayer(paintingFragment);

                        paintingFragment.fadeInLeft();

                        game.gameState.galleryState.visitedImages.add(next.image.name);

                        controlsFragment.leftArrow.setTouchable(Touchable.enabled);
                        controlsFragment.rightArrow.setTouchable(Touchable.enabled);

                    } catch (Throwable e) {
                        Log.e(tag, e);

                        onCriticalError(e);
                    }
                }
            }, .22f);

        } catch (Throwable e) {
            Log.e(tag, e);

            onCriticalError(e);
        }
    }

    public void prevPainting() {
        final PaintingImage prev = paintingFragment.paintingImage.prev;
        if (prev == null) return;

        try {
            Log.i(tag, "prevPainting " + prev.image.name);

            game.sfxService.onGalleryArrowClick();

            paintingFragment.fadeOutRight(new Runnable() {
                @Override
                public void run() {

                    try {

                        paintingFragment.destroy();
                        paintingFragment = null;

                    } catch (Throwable e) {
                        Log.e(tag, e);
                    }
                }
            });

            Timer.instance().scheduleTask(new Timer.Task() {
                @Override
                public void run() {
                    try {

                        paintingFragment = new PaintingFragment(PaintingScreen.this);
                        paintingFragment.create(prev);

                        layers.setContentLayer(paintingFragment);

                        paintingFragment.fadeInRight();

                        game.gameState.galleryState.visitedImages.add(prev.image.name);

                        controlsFragment.leftArrow.setTouchable(Touchable.enabled);
                        controlsFragment.rightArrow.setTouchable(Touchable.enabled);

                    } catch (Throwable e) {
                        Log.e(tag, e);

                        onCriticalError(e);
                    }
                }
            }, .22f);

        } catch (Throwable e) {
            Log.e(tag, e);

            onCriticalError(e);
        }
    }

    public void openAdultGateBanner(Runnable task) {
        super.openAdultGateBanner(task);

        try {

            destroyBanners();

            if (!game.params.isAdultGateEnabled) {
                if (task != null) {
                    task.run();
                }
                return;
            }

            adultGateFragment = new AdultGateFragment(this);
            adultGateFragment.create(task);

            layers.setBannerLayer(adultGateFragment);

            adultGateFragment.fadeIn();

        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }

    @Override
    public void destroyBanners() {
        super.destroyBanners();

        if (adultGateFragment != null) {
            adultGateFragment.destroy();
            adultGateFragment = null;
        }
    }

    @Override
    public void dispose() {
        super.dispose();

        destroyBanners();

        if (layers != null) {
            layers.dispose();
            layers = null;
        }

        if (paintingFragment != null) {
            paintingFragment.destroy();
            paintingFragment = null;
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
