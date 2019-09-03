package ua.gram.munhauzen.screen.painting.fragment;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import ua.gram.munhauzen.screen.PaintingScreen;
import ua.gram.munhauzen.screen.painting.ui.FullscreenImage;
import ua.gram.munhauzen.ui.Fragment;
import ua.gram.munhauzen.ui.FragmentRoot;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class FullscreenFragment extends Fragment {

    private final String tag = getClass().getSimpleName();
    public final PaintingScreen screen;
    public FragmentRoot root;
    public FullscreenImage fullscreenImage;

    public FullscreenFragment(PaintingScreen screen) {
        this.screen = screen;
    }

    public void create() {
        Log.i(tag, "create");

        fullscreenImage = new FullscreenImage(screen.paintingFragment);

        root = new FragmentRoot();
        root.setTouchable(Touchable.childrenOnly);
        root.addContainer(fullscreenImage);

        root.setName(tag);
        root.setVisible(false);
    }

    @Override
    public Actor getRoot() {
        return root;
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

    public void fadeOut() {

        root.setVisible(true);
        root.addAction(Actions.sequence(
                Actions.parallel(
                        Actions.alpha(0, .15f)
                ),
                Actions.visible(false)
        ));
    }
}
