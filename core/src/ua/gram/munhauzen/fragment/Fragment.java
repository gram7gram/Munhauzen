package ua.gram.munhauzen.fragment;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;

import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class Fragment implements Disposable {

    protected String tag = getClass().getSimpleName();
    Actor root;

    public Fragment() {
    }

    public Fragment(Actor root) {
        this.root = root;
    }

    public Actor getRoot() {
        return root;
    }

    public void destroy() {
        Log.i(tag, "destroy");

        try {
            Actor actor = getRoot();
            if (actor != null) {
                actor.remove();
            }

            if (root != null) {
                root.remove();
                root = null;
            }

            dispose();
        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }

    public boolean isMounted() {
        return getRoot() != null && getRoot().getStage() != null;
    }

    @Override
    public void dispose() {
        Log.i(tag, "dispose");
    }
}
