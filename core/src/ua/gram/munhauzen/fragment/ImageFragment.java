package ua.gram.munhauzen.fragment;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.ui.FitImage;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class ImageFragment extends Fragment {

    private final String tag = getClass().getSimpleName();
    private final GameScreen gameScreen;
    public Stack root;

    public Image layer1Image, layer2Image;
    public Table layer1ImageTable, layer2ImageTable;
    public Group layer1ImageGroup, layer2ImageGroup;
    public Image layer1OverlayTop, layer1OverlayBottom, layer2OverlayTop, layer2OverlayBottom;

    public ImageFragment(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    public void create() {

        Log.i(tag, "create");

        Texture overlay = gameScreen.assetManager.get("GameScreen/t_putty.png", Texture.class);

        layer1OverlayBottom = new Image(overlay);
        layer1OverlayTop = new Image(overlay);
        layer2OverlayBottom = new Image(overlay);
        layer2OverlayTop = new Image(overlay);

        layer1OverlayTop.setVisible(false);
        layer1OverlayBottom.setVisible(false);
        layer2OverlayTop.setVisible(false);
        layer2OverlayBottom.setVisible(false);

        layer1Image = new FitImage();
        layer2Image = new FitImage();

        layer1ImageTable = new Table();
        layer1ImageTable.setFillParent(true);
        layer1ImageTable.add(layer1Image).center().expand().fill();

        layer2ImageTable = new Table();
        layer2ImageTable.setFillParent(true);
        layer2ImageTable.add(layer2Image).center().expand().fill();

        layer1ImageGroup = new Group();
        layer1ImageGroup.addActor(layer1ImageTable);
        layer1ImageGroup.addActor(layer1OverlayTop);
        layer1ImageGroup.addActor(layer1OverlayBottom);

        layer2ImageGroup = new Group();
        layer2ImageGroup.addActor(layer2ImageTable);
        layer2ImageGroup.addActor(layer2OverlayTop);
        layer2ImageGroup.addActor(layer2OverlayBottom);

        root = new Stack();
        root.setFillParent(true);

        root.add(layer1ImageGroup);
        root.add(layer2ImageGroup);
    }


    @Override
    public Actor getRoot() {
        return root;
    }

    @Override
    public void dispose() {
        super.dispose();

    }
}
