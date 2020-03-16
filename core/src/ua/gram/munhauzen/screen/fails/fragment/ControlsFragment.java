package ua.gram.munhauzen.screen.fails.fragment;

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
import ua.gram.munhauzen.screen.FailsScreen;
import ua.gram.munhauzen.screen.MenuScreen;
import ua.gram.munhauzen.ui.Fragment;
import ua.gram.munhauzen.ui.FragmentRoot;
import ua.gram.munhauzen.ui.SoundBtn;
import ua.gram.munhauzen.utils.Log;

public class ControlsFragment extends Fragment {

    private final String tag = getClass().getSimpleName();
    private final FailsScreen screen;
    public FragmentRoot root;

    public ControlsFragment(FailsScreen screen) {
        this.screen = screen;
    }

    public void create() {

        Log.i(tag, "create");

        SoundBtn soundBtn = new SoundBtn(screen);

        ImageButton menuBtn = getMenuBtn();

        Table soundTable = new Table();
        soundTable.add(soundBtn).size(MunhauzenGame.WORLD_WIDTH * .15f);

        Table menuTable = new Table();
        menuTable.add(menuBtn).width(MunhauzenGame.WORLD_WIDTH * .2f)
                .height(MunhauzenGame.WORLD_WIDTH * .12f);

        Container<Table> menuContainer = new Container<>(menuTable);
        menuContainer.align(Align.bottomLeft);
        menuContainer.pad(10);

        Container<Table> soundContainer = new Container<>(soundTable);
        soundContainer.align(Align.bottomRight);
        soundContainer.pad(10);

        root = new FragmentRoot();
        root.addContainer(menuContainer);
        root.addContainer(soundContainer);

        root.setName(tag);
        root.setVisible(false);
    }

    public void fadeOut() {
        root.addAction(Actions.sequence(
                Actions.alpha(0, .2f),
                Actions.visible(false)
        ));
    }

    public void fadeIn() {
        root.addAction(Actions.sequence(
                Actions.alpha(0),
                Actions.visible(true),
                Actions.alpha(1, .2f)
        ));
    }

    @Override
    public Actor getRoot() {
        return root;
    }

    public void update() {

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

                    screen.game.stopAllAudio();

                    screen.game.sfxService.onBackToMenuClicked();

                    screen.navigateTo(new MenuScreen(screen.game));
                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }
        });

        return btn;
    }
}
