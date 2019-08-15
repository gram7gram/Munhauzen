package ua.gram.munhauzen.screen.painting.fragment;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.screen.GalleryScreen;
import ua.gram.munhauzen.screen.PaintingScreen;
import ua.gram.munhauzen.ui.Fragment;
import ua.gram.munhauzen.ui.FragmentRoot;
import ua.gram.munhauzen.ui.PrimaryButton;
import ua.gram.munhauzen.utils.Log;

public class ControlsFragment extends Fragment {

    private final String tag = getClass().getSimpleName();
    private final PaintingScreen screen;
    public FragmentRoot root;
    Container<Table> menuContainer;

    public ControlsFragment(PaintingScreen screen) {
        this.screen = screen;
    }

    public void create() {

        Log.i(tag, "create");

        PrimaryButton menuBtn = screen.game.buttonBuilder.primary("Back", new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                try {
                    screen.game.setScreen(new GalleryScreen(screen.game));
                    screen.dispose();
                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }
        });

        Table menuTable = new Table();
        menuTable.add(menuBtn)
                .width(MunhauzenGame.WORLD_WIDTH * .25f)
                .height(MunhauzenGame.WORLD_HEIGHT * .08f)
                .row();

        menuContainer = new Container<>(menuTable);
        menuContainer.align(Align.topLeft);
        menuContainer.pad(10);

        root = new FragmentRoot();
        root.addContainer(menuContainer);

        root.setName(tag);
    }

    @Override
    public Actor getRoot() {
        return root;
    }
}
