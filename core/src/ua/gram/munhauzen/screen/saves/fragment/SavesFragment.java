package ua.gram.munhauzen.screen.saves.fragment;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

import ua.gram.munhauzen.FontProvider;
import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.history.Save;
import ua.gram.munhauzen.screen.SavesScreen;
import ua.gram.munhauzen.screen.saves.ui.SaveRow;
import ua.gram.munhauzen.ui.Fragment;
import ua.gram.munhauzen.ui.FragmentRoot;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class SavesFragment extends Fragment {

    private final String tag = getClass().getSimpleName();
    public final SavesScreen screen;
    public FragmentRoot root;

    public SavesFragment(SavesScreen screen) {
        this.screen = screen;
    }

    @Override
    public Actor getRoot() {
        return root;
    }

    public void create() {

        Log.i(tag, "create");

        Image footer = new Image(screen.assetManager.get("saves/gv_paper_3.png", Texture.class));

        Table content = new Table();
        content.add(createHeader()).expandX().row();

        for (Save save : screen.saves) {
            content.add(new SaveRow(save, screen)).row();
        }

        content.add(footer).expandX().row();

        ScrollPane scroll = new ScrollPane(content);

        root = new FragmentRoot();
        root.addContainer(scroll);

        root.setVisible(false);
    }

    public void fadeIn() {
        root.clearActions();

        root.setVisible(true);
        root.addAction(Actions.sequence(
                Actions.alpha(0),
                Actions.moveBy(0, 20),
                Actions.parallel(
                        Actions.alpha(1, .3f),
                        Actions.moveBy(0, -20, .3f)
                )
        ));
    }

    public void fadeOut(Runnable task) {

        root.clearActions();

        root.addAction(Actions.sequence(
                Actions.parallel(
                        Actions.alpha(0, .3f),
                        Actions.moveBy(0, 20, .3f)
                ),
                Actions.visible(false),
                Actions.run(task)
        ));
    }

    public void update() {

    }

    private Actor createHeader() {

        Stack root = new Stack();

        Image back = new Image(screen.assetManager.get("saves/gv_paper_1.png", Texture.class));
        Image icon = new Image(screen.assetManager.get("saves/sv_baron.png", Texture.class));

        Label title = new Label("Saves", new Label.LabelStyle(
                screen.game.fontProvider.getFont(FontProvider.h1),
                Color.BLACK
        ));

        Table content = new Table();
        content.add(title).padBottom(5).expandX().row();
        content.add(icon)
                .size(MunhauzenGame.WORLD_WIDTH * .125f)
                .expandX().row();

        Container<Table> container = new Container<>(content);
        container.pad(10);
        container.align(Align.bottom);

        root.add(back);
        root.add(container);

        return root;
    }

    @Override
    public void dispose() {
        super.dispose();

    }
}