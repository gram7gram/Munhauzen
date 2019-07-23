package ua.gram.munhauzen.fragment;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;

import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.ui.BackgroundImage;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class ImageFragment extends Fragment {

    private final String tag = getClass().getSimpleName();
    private final GameScreen gameScreen;
    public Stack root;

    public BackgroundImage backgroundTopImage;
    public BackgroundImage backgroundBottomImage;

    public ImageFragment(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    public void create() {

        Log.i(tag, "create");

        backgroundTopImage = new BackgroundImage(gameScreen);

        backgroundBottomImage = new BackgroundImage(gameScreen);

        root = new Stack();
        root.setFillParent(true);

        root.add(backgroundBottomImage);
        root.add(backgroundTopImage);

        root.setName(tag);
    }

    @Override
    public Actor getRoot() {
        return root;
    }

    public void update() {

    }
}
