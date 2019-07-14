package ua.gram.munhauzen.interaction.picture.fragment;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import ua.gram.munhauzen.fragment.Fragment;
import ua.gram.munhauzen.interaction.PictureInteraction;
import ua.gram.munhauzen.ui.FitImage;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class PictureImageFragment extends Fragment {

    private final PictureInteraction interaction;
    public Stack root;
    public Image background;
    public Table backgroundTable;
    public float backgroundHeight;
    public float backgroundWidth;

    public PictureImageFragment(PictureInteraction interaction) {
        this.interaction = interaction;
    }

    public void create() {

        Log.i(tag, "create");

        background = new FitImage();

        backgroundTable = new Table();
        backgroundTable.setFillParent(true);
        backgroundTable.add(background).center().expand().fill();

        root = new Stack();
        root.setFillParent(true);
        root.setTouchable(Touchable.childrenOnly);
        root.addActor(backgroundTable);
    }

    @Override
    public Actor getRoot() {
        return root;
    }

    public void update() {

    }
}
