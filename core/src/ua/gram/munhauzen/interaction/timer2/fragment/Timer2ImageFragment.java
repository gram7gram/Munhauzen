package ua.gram.munhauzen.interaction.timer2.fragment;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;

import ua.gram.munhauzen.fragment.InteractionFragment;
import ua.gram.munhauzen.interaction.Timer2Interaction;
import ua.gram.munhauzen.ui.BackgroundImage;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class Timer2ImageFragment extends InteractionFragment {

    final Timer2Interaction interaction;
    Stack root;
    Timer2BombFragment bombFragment;
    public BackgroundImage backgroundImage;

    public Timer2ImageFragment(Timer2Interaction interaction) {
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

            bombFragment = new Timer2BombFragment(interaction);
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
