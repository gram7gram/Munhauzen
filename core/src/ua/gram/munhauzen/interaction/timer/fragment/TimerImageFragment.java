package ua.gram.munhauzen.interaction.timer.fragment;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;

import ua.gram.munhauzen.interaction.TimerInteraction;
import ua.gram.munhauzen.screen.game.fragment.InteractionFragment;
import ua.gram.munhauzen.screen.game.ui.BackgroundImage;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class TimerImageFragment extends InteractionFragment {

    final TimerInteraction interaction;
    Stack root;
    TimerBombFragment bombFragment;
    public BackgroundImage backgroundImage;

    public TimerImageFragment(TimerInteraction interaction) {
        this.interaction = interaction;
    }

    public void create() {

        Log.i(tag, "create");

        interaction.gameScreen.hideImageFragment();

        backgroundImage = new BackgroundImage(interaction.gameScreen);

        root = new Stack();
        root.addActor(backgroundImage);
    }

    public void startTimer() {

        if (bombFragment == null) {

            bombFragment = new TimerBombFragment(interaction);
            bombFragment.create();

            root.addActor(bombFragment.getRoot());
        }
    }

    public void update() {
        if (bombFragment != null) {
            bombFragment.update();
        }
    }

    @Override
    public Actor getRoot() {
        return root;
    }

    @Override
    public void dispose() {
        super.dispose();

        if (bombFragment != null) {
            bombFragment.destroy();
            bombFragment = null;
        }
    }
}
