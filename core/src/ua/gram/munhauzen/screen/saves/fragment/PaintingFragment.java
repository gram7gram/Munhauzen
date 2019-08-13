package ua.gram.munhauzen.screen.saves.fragment;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import ua.gram.munhauzen.entity.Image;
import ua.gram.munhauzen.screen.GalleryScreen;
import ua.gram.munhauzen.ui.Fragment;
import ua.gram.munhauzen.ui.FragmentRoot;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class PaintingFragment extends Fragment {

    private final String tag = getClass().getSimpleName();
    public final GalleryScreen screen;
    final Image image;
    public FragmentRoot root;

    public PaintingFragment(GalleryScreen screen, Image image) {
        this.screen = screen;
        this.image = image;
    }

    @Override
    public Actor getRoot() {
        return root;
    }

    public void create() {


        root = new FragmentRoot();

        root.setVisible(false);
    }

    public void fadeIn() {
        root.clearActions();

        root.setVisible(true);
        root.addAction(Actions.sequence(
                Actions.alpha(0),
                Actions.moveBy(0, 20),
                Actions.parallel(
                        Actions.alpha(1, .3f),
                        Actions.moveBy(0, -20, .3f)
                )
        ));
    }

    public void fadeOut(Runnable task) {

        root.clearActions();

        root.addAction(Actions.sequence(
                Actions.parallel(
                        Actions.alpha(0, .3f),
                        Actions.moveBy(0, 20, .3f)
                ),
                Actions.visible(false),
                Actions.run(task)
        ));
    }

    public void update() {

    }
}