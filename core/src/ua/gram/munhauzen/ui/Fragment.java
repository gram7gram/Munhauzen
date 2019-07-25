package ua.gram.munhauzen.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;

import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public abstract class Fragment implements Disposable {

    protected String tag = getClass().getSimpleName();
    public boolean isDisposed;

    public Fragment() {
        isDisposed = false;
    }

    public abstract Actor getRoot();

    public void destroy() {
        Log.i(tag, "destroy");

        try {
            Actor actor = getRoot();
            if (actor != null) {
                actor.remove();
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
        isDisposed = true;
    }
}
