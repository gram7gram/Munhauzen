package ua.gram.munhauzen.screen.menu.fragment;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import ua.gram.munhauzen.screen.MenuScreen;
import ua.gram.munhauzen.screen.menu.ui.ChronoButton;
import ua.gram.munhauzen.screen.menu.ui.ContinueButton;
import ua.gram.munhauzen.screen.menu.ui.FoolsButton;
import ua.gram.munhauzen.screen.menu.ui.GalleryButton;
import ua.gram.munhauzen.screen.menu.ui.MenuButton;
import ua.gram.munhauzen.screen.menu.ui.SavesButton;
import ua.gram.munhauzen.screen.menu.ui.StartButton;
import ua.gram.munhauzen.ui.Fragment;
import ua.gram.munhauzen.ui.FragmentRoot;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class ControlsFragment extends Fragment {

    private final String tag = getClass().getSimpleName();
    private final MenuScreen screen;
    private final AssetManager assetManager;
    public FragmentRoot root;
    public MenuButton startButton, continueButton, savesButton, galleryButton, foolsButton, chronoButton;

    public ControlsFragment(MenuScreen screen) {
        this.screen = screen;
        assetManager = new AssetManager();
    }

    public void create() {

        Log.i(tag, "create");

        startButton = new StartButton(screen);
        continueButton = new ContinueButton(screen);
        savesButton = new SavesButton(screen);
        galleryButton = new GalleryButton(screen);
        foolsButton = new FoolsButton(screen);
        chronoButton = new ChronoButton(screen);

        Table btnTable = new Table();
        btnTable.add(continueButton).row();
        btnTable.add(startButton).row();
        btnTable.add(savesButton).row();
        btnTable.add(galleryButton).row();
        btnTable.add(foolsButton).row();
        btnTable.add(chronoButton).row();

        root = new FragmentRoot();
        root.addContainer(btnTable);

        root.setName(tag);
    }

    @Override
    public Actor getRoot() {
        return root;
    }

    @Override
    public void dispose() {
        super.dispose();
        assetManager.dispose();
    }
}
