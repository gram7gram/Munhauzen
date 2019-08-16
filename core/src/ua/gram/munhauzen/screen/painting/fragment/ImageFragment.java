package ua.gram.munhauzen.screen.painting.fragment;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.GdxRuntimeException;

import ua.gram.munhauzen.screen.PaintingScreen;
import ua.gram.munhauzen.screen.painting.ui.BonusNotice;
import ua.gram.munhauzen.screen.painting.ui.BonusPainting;
import ua.gram.munhauzen.screen.painting.ui.ColorPainting;
import ua.gram.munhauzen.screen.painting.ui.Painting;
import ua.gram.munhauzen.screen.painting.ui.SimplePainting;
import ua.gram.munhauzen.screen.painting.ui.Statue;
import ua.gram.munhauzen.screen.painting.ui.StatuePainting;
import ua.gram.munhauzen.ui.Fragment;
import ua.gram.munhauzen.ui.FragmentRoot;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class ImageFragment extends Fragment {

    private final String tag = getClass().getSimpleName();
    private final PaintingScreen screen;
    public FragmentRoot root;
    public Painting painting;
    public Statue statue;
    public BonusNotice bonusNotice;

    public ImageFragment(PaintingScreen screen) {
        this.screen = screen;
    }

    public void create() {
        Log.i(tag, "create");

        if (screen.paintingImage.image.type == null) {
            painting = new SimplePainting(screen);
        } else {
            switch (screen.paintingImage.image.type) {
                case "bonus":
                    painting = new BonusPainting(screen);

                    bonusNotice = new BonusNotice(screen);
                    bonusNotice.setTouchable(Touchable.disabled);

                    break;
                case "statue":
                    painting = new StatuePainting(screen);

                    statue = new Statue(screen);
                    statue.setTouchable(Touchable.childrenOnly);

                    break;
                case "color":
                    painting = new ColorPainting(screen);
                    break;
                default:
                    throw new GdxRuntimeException("Unknown image type " + screen.paintingImage.image.type);
            }
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

        root.setVisible(true);
        root.addAction(Actions.sequence(
                Actions.alpha(0),
                Actions.parallel(
                        Actions.alpha(1, .2f)
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
}
