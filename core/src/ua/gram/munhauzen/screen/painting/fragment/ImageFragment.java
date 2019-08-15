package ua.gram.munhauzen.screen.painting.fragment;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.GdxRuntimeException;

import ua.gram.munhauzen.screen.PaintingScreen;
import ua.gram.munhauzen.screen.painting.ui.BonusPainting;
import ua.gram.munhauzen.screen.painting.ui.ColorPainting;
import ua.gram.munhauzen.screen.painting.ui.Painting;
import ua.gram.munhauzen.screen.painting.ui.SimplePainting;
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

    public ImageFragment(PaintingScreen screen) {
        this.screen = screen;
    }

    public void create() {
        Log.i(tag, "create");

        if (screen.image.type == null) {
            painting = new SimplePainting(screen);
        } else {
            switch (screen.image.type) {
                case "bonus":
                    painting = new BonusPainting(screen);
                    break;
                case "statue":
                    painting = new StatuePainting(screen);
                    break;
                case "color":
                    painting = new ColorPainting(screen);
                    break;
                default:
                    throw new GdxRuntimeException("Unknown image type " + screen.image.type);
            }
        }

        root = new FragmentRoot();
        root.addContainer(painting);

        root.setName(tag);
    }

    @Override
    public Actor getRoot() {
        return root;
    }

    public void update() {

    }
}
