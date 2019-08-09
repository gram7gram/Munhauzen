package ua.gram.munhauzen.screen.game.fragment;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import ua.gram.munhauzen.ui.Fragment;

public abstract class InteractionFragment extends Fragment {

    public void fadeInRoot() {
        final Actor actor = getRoot();
        if (actor == null) return;

        final Touchable before = actor.getTouchable();

        actor.setTouchable(Touchable.disabled);
        actor.setVisible(true);

        actor.addAction(Actions.sequence(
                Actions.alpha(0),
                Actions.alpha(1, .5f),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        actor.setTouchable(before);
                    }
                })
        ));
    }
}
