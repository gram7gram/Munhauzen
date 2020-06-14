package ua.gram.munhauzen.screen;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.screen.debug.fragment.AudioFragment;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class DebugAudioScreen extends AbstractScreen {

    AudioFragment fragment;

    public DebugAudioScreen(MunhauzenGame game) {
        super(game);
    }

    @Override
    protected void onResourcesLoaded() {
        super.onResourcesLoaded();

        try {

            fragment = new AudioFragment(this);
            fragment.create();

            ui.addActor(fragment.getRoot());

            fragment.startTesting();
        } catch (Throwable e) {
            Log.e(tag, e);

            onCriticalError(e);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        navigateTo(new DebugScreen(game));
    }

    @Override
    public void renderAfterLoaded(float delta) {

        if (fragment != null)
            fragment.update();

        ui.act(delta);
        ui.draw();
    }

    @Override
    public void dispose() {
        super.dispose();

        if (fragment != null)
            fragment.dispose();
    }
}
