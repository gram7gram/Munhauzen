package ua.gram.munhauzen.interaction.picture.fragment;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;

import ua.gram.munhauzen.interaction.PictureInteraction;
import ua.gram.munhauzen.screen.game.fragment.InteractionFragment;
import ua.gram.munhauzen.screen.game.ui.BackgroundImage;
import ua.gram.munhauzen.ui.FragmentRoot;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class PictureImageFragment extends InteractionFragment {

    private final PictureInteraction interaction;
    public FragmentRoot root;
    public BackgroundImage backgroundImage;

    public PictureImageFragment(PictureInteraction interaction) {
        this.interaction = interaction;
    }

    public void create() {

        Log.i(tag, "create");

        backgroundImage = new BackgroundImage(interaction.gameScreen);

        root = new FragmentRoot();
        root.setTouchable(Touchable.childrenOnly);
        root.addContainer(backgroundImage);
    }

    @Override
    public Actor getRoot() {
        return root;
    }

    public void update() {

    }
}
