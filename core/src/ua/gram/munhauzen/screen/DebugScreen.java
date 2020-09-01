package ua.gram.munhauzen.screen;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.screen.debug.fragment.DebugFragment;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class DebugScreen extends AbstractScreen {

    private DebugFragment debugFragment;

    public DebugScreen(MunhauzenGame game) {
        super(game);
    }

    @Override
    protected void onResourcesLoaded() {
        super.onResourcesLoaded();

        try {
            debugFragment = new DebugFragment(this);
            debugFragment.create();

            ui.addActor(debugFragment.getRoot());
        } catch (Throwable e) {
            Log.e(tag, e);

            onCriticalError(e);
        }
    }

    @Override
    public void renderAfterLoaded(float delta) {

        if (debugFragment != null)
            debugFragment.update();

        ui.act(delta);
        ui.draw();
    }

    @Override
    public void dispose() {
        super.dispose();

        if (debugFragment != null) {
            debugFragment.dispose();
            debugFragment = null;
        }
    }
}
