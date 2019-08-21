package ua.gram.munhauzen.screen;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.screen.debug.fragment.ControlsFragment;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class DebugScreen extends AbstractScreen {

    private ControlsFragment controlsFragment;

    public DebugScreen(MunhauzenGame game) {
        super(game);
    }

    @Override
    protected void onResourcesLoaded() {
        super.onResourcesLoaded();

        controlsFragment = new ControlsFragment(this);
        controlsFragment.create();

        ui.addActor(controlsFragment.getRoot());
    }

    @Override
    public void renderAfterLoaded(float delta) {

        if (controlsFragment != null)
            controlsFragment.update();

        ui.act(delta);
        ui.draw();
    }

    @Override
    public void dispose() {
        super.dispose();

        if (controlsFragment != null) {
            controlsFragment.dispose();
            controlsFragment = null;
        }
    }
}
