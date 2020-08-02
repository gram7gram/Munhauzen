package ua.gram.munhauzen.screen.painting.fragment;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import ua.gram.munhauzen.screen.PaintingScreen;
import ua.gram.munhauzen.screen.gallery.entity.PaintingImage;
import ua.gram.munhauzen.screen.painting.ui.BonusNotice;
import ua.gram.munhauzen.screen.painting.ui.BonusPainting;
import ua.gram.munhauzen.screen.painting.ui.ColorPainting;
import ua.gram.munhauzen.screen.painting.ui.Painting;
import ua.gram.munhauzen.screen.painting.ui.SimplePainting;
import ua.gram.munhauzen.screen.painting.ui.Statue;
import ua.gram.munhauzen.screen.painting.ui.StatuePainting;
import ua.gram.munhauzen.ui.Fragment;
import ua.gram.munhauzen.ui.FragmentRoot;
import ua.gram.munhauzen.utils.ExpansionAssetManager;
import ua.gram.munhauzen.utils.ExternalFiles;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class PaintingFragment extends Fragment {

    private final String tag = getClass().getSimpleName();
    public final PaintingScreen screen;
    public FragmentRoot root;
    public Painting painting;
    public Statue statue;
    public BonusNotice bonusNotice;
    public PaintingImage paintingImage;
    public ExpansionAssetManager assetManager;

    public PaintingFragment(PaintingScreen screen) {
        this.screen = screen;
    }

    @Override
    public void dispose() {
        super.dispose();

        if (assetManager != null) {
            assetManager.dispose();
            assetManager = null;
        }
    }

    public Texture getPaintingTexture() {
        if (paintingImage.isOpened) {
            return assetManager.get(paintingImage.imageResource, Texture.class);
        } else {
            return assetManager.get("gallery/aquestion.png", Texture.class);
        }
    }

    public Texture getBonusFrameTexture() {
        return assetManager.get("gallery/gv2_frame_4.png", Texture.class);
    }

    public Texture getStatueFrameTexture() {
        return assetManager.get("gallery/gv2_frame_3.png", Texture.class);
    }

    public Texture getColorFrameTexture() {
        return assetManager.get("gallery/gv2_frame_2.png", Texture.class);
    }

    public Texture getSimpleFrameTexture() {
        return assetManager.get("gallery/gv2_frame_1.png", Texture.class);
    }

    private void loadFrame() {
        if (paintingImage.image.isBonus()) {

            assetManager.load("gallery/gv2_frame_4.png", Texture.class);

        } else if (paintingImage.image.isStatue()) {

            assetManager.load("gallery/gv2_frame_3.png", Texture.class);

        } else if (paintingImage.image.isColor()) {

            assetManager.load("gallery/gv2_frame_2.png", Texture.class);

        } else {

            assetManager.load("gallery/gv2_frame_1.png", Texture.class);

        }
    }

    public void create(PaintingImage img) {
        Log.i(tag, "create " + img.image.name);

        assetManager = new ExpansionAssetManager(screen.game);

        this.paintingImage = img;

        paintingImage.imageResource = ExternalFiles.getExpansionImage(screen.game.params, paintingImage.image).path();

        if (paintingImage.isOpened) {
            assetManager.load(paintingImage.imageResource, Texture.class);
        } else {
            assetManager.load("gallery/aquestion.png", Texture.class);
        }

        loadFrame();

        if (paintingImage.image.isStatue()) {

            if (paintingImage.canDisplayStatueItem()) {
                assetManager.load(paintingImage.statueResource, Texture.class);
            }

            assetManager.load("gallery/gv2_statue.png", Texture.class);
            assetManager.load("ui/banner_fond_3.png", Texture.class);

        } else if (paintingImage.image.isBonus()) {

            assetManager.load("gallery/gv2_bonus_back.png", Texture.class);
            assetManager.load("gallery/gv2_bonus_stick.png", Texture.class);

        }

        assetManager.finishLoading();

        if (paintingImage.image.isBonus()) {

            painting = new BonusPainting(screen);

            bonusNotice = new BonusNotice(screen);
            bonusNotice.setTouchable(Touchable.disabled);

        } else if (paintingImage.image.isStatue()) {

            painting = new StatuePainting(screen);

            statue = new Statue(screen);
            statue.setTouchable(Touchable.childrenOnly);

        } else if (paintingImage.image.isColor()) {

            painting = new ColorPainting(screen);

        } else {
            painting = new SimplePainting(screen);
        }

        root = new FragmentRoot();
        root.addContainer(painting);

        if (bonusNotice != null) {
            root.addContainer(bonusNotice);
        }

        if (statue != null) {
            root.addContainer(statue);
        }

        root.setName(tag);
        root.setVisible(false);
    }

    @Override
    public Actor getRoot() {
        return root;
    }

    public void update() {

    }

    public void fadeIn() {

        float duration = .2f;

        root.setVisible(true);
        root.addAction(Actions.sequence(
                Actions.alpha(0),
                Actions.parallel(
                        Actions.alpha(1, duration)
                )
        ));
    }

    public void fadeInLeft() {

        float duration = .2f;

        root.setVisible(true);
        root.addAction(Actions.sequence(
                Actions.alpha(0),
                Actions.moveBy(50, root.getY()),
                Actions.parallel(
                        Actions.alpha(1, duration),
                        Actions.moveBy(-50, root.getY(), duration)
                )
        ));
    }

    public void fadeInRight() {

        float duration = .2f;

        root.setVisible(true);
        root.addAction(Actions.sequence(
                Actions.alpha(0),
                Actions.moveBy(-50, root.getY()),
                Actions.parallel(
                        Actions.alpha(1, duration),
                        Actions.moveBy(50, root.getY(), duration)
                )
        ));
    }

    public void fadeOut(Runnable task) {

        root.setVisible(true);
        root.addAction(Actions.sequence(
                Actions.parallel(
                        Actions.alpha(0, .15f)
                ),
                Actions.visible(false),
                Actions.run(task)
        ));
    }

    public void fadeOutLeft(Runnable task) {

        float duration = .2f;

        root.setVisible(true);
        root.addAction(Actions.sequence(
                Actions.parallel(
                        Actions.alpha(0, duration),
                        Actions.moveBy(-50, root.getY(), duration)
                ),
                Actions.visible(false),
                Actions.run(task)
        ));
    }

    public void fadeOutRight(Runnable task) {

        float duration = .2f;

        root.setVisible(true);
        root.addAction(Actions.sequence(
                Actions.parallel(
                        Actions.alpha(0, duration),
                        Actions.moveBy(50, root.getY(), duration)
                ),
                Actions.visible(false),
                Actions.run(task)
        ));
    }
}
