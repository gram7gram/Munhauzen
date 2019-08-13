package ua.gram.munhauzen.screen.gallery.fragment;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.screen.GalleryScreen;
import ua.gram.munhauzen.screen.MenuScreen;
import ua.gram.munhauzen.ui.Fragment;
import ua.gram.munhauzen.ui.FragmentRoot;
import ua.gram.munhauzen.utils.Log;

public class ControlsFragment extends Fragment {

    private final String tag = getClass().getSimpleName();
    private final GalleryScreen screen;
    public FragmentRoot root;
    Container<Table> menuContainer;
    boolean isFadeIn, isFadeOut;

    public ControlsFragment(GalleryScreen screen) {
        this.screen = screen;
    }

    public void create() {

        Log.i(tag, "create");

        ImageButton menuBtn = getMenuBtn();

        float iconSize = MunhauzenGame.WORLD_WIDTH * .18f;
        float iconSize2 = iconSize * 1.5f;

        Table menuTable = new Table();
        menuTable.add(menuBtn).expandX().left().width(iconSize2).height(iconSize2 / 2f).row();

        menuContainer = new Container<>(menuTable);
        menuContainer.align(Align.bottomLeft);
        menuContainer.pad(10);

        root = new FragmentRoot();
        root.addContainer(menuContainer);

        root.setName(tag);

        root.setVisible(false);
    }

    public void fadeOut() {

        if (isFadeOut) return;

        isFadeIn = false;
        isFadeOut = true;

        menuContainer.addAction(Actions.sequence(
                Actions.parallel(
                        Actions.moveBy(0, -20, .3f),
                        Actions.alpha(0, .3f)
                ),
                Actions.visible(false)
        ));

        root.addAction(Actions.sequence(
                Actions.delay(.4f),
                Actions.visible(false),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        isFadeIn = false;
                        isFadeOut = false;
                    }
                })
        ));
    }

    public void fadeIn() {

        if (isFadeIn) return;

        isFadeIn = true;
        isFadeOut = false;

        menuContainer.addAction(Actions.sequence(
                Actions.visible(true),
                Actions.alpha(0),
                Actions.parallel(
                        Actions.moveBy(0, 20, .3f),
                        Actions.alpha(1, .3f)
                )
        ));

        root.addAction(Actions.sequence(
                Actions.visible(true),
                Actions.delay(.4f),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        isFadeIn = false;
                        isFadeOut = false;
                    }
                })
        ));
    }

    @Override
    public Actor getRoot() {
        return root;
    }

    private ImageButton getMenuBtn() {
        Texture txt = screen.assetManager.get("menu/b_menu.png", Texture.class);

        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.up = new SpriteDrawable(new Sprite(txt));
        style.down = new SpriteDrawable(new Sprite(txt));
        style.disabled = new SpriteDrawable(new Sprite(txt));

        ImageButton btn = new ImageButton(style);

        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                try {
                    screen.game.setScreen(new MenuScreen(screen.game));
                    screen.dispose();
                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }
        });

        return btn;
    }
}
