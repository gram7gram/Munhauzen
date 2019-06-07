package ua.gram.munhauzen.fragment;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;

import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class Fragment implements Disposable {

    String tag = getClass().getSimpleName();
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

        Actor actor = getRoot();
        if (actor != null) {
            actor.remove();
        }
        if (root != null) {
            root.remove();
            root = null;
        }

        dispose();
    }

    @Override
    public void dispose() {
        Log.i(tag, "dispose");
    }
}
