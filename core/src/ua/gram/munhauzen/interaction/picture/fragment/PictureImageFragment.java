package ua.gram.munhauzen.interaction.picture.fragment;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;

import ua.gram.munhauzen.fragment.Fragment;
import ua.gram.munhauzen.interaction.PictureInteraction;
import ua.gram.munhauzen.ui.BackgroundImage;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class PictureImageFragment extends Fragment {

    private final PictureInteraction interaction;
    public Stack root;
    public BackgroundImage backgroundImage;

    public PictureImageFragment(PictureInteraction interaction) {
        this.interaction = interaction;
    }

    public void create() {

        Log.i(tag, "create");

        backgroundImage = new BackgroundImage(interaction.gameScreen);

        root = new Stack();
        root.setFillParent(true);
        root.setTouchable(Touchable.childrenOnly);
        root.addActor(backgroundImage);
    }

    @Override
    public Actor getRoot() {
        return root;
    }

    public void update() {

    }
}
