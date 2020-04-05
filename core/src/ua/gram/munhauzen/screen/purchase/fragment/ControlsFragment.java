package ua.gram.munhauzen.screen.purchase.fragment;

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
import ua.gram.munhauzen.screen.MenuScreen;
import ua.gram.munhauzen.screen.PurchaseScreen;
import ua.gram.munhauzen.ui.Fragment;
import ua.gram.munhauzen.ui.FragmentRoot;
import ua.gram.munhauzen.utils.Log;

public class ControlsFragment extends Fragment {

    private final String tag = getClass().getSimpleName();
    private final PurchaseScreen screen;
    public FragmentRoot root;
    public Container<?> restoreContainer, menuContainer;

    public ControlsFragment(PurchaseScreen screen) {
        this.screen = screen;
    }

    public void create() {

        Log.i(tag, "create");

        Texture txt = screen.game.internalAssetManager.get("purchase/restore.png", Texture.class);

        ImageButton menuBtn = getMenuBtn();
        ImageButton restoreBtn = getRestoreBtn(txt);

        float iconWidth = MunhauzenGame.WORLD_WIDTH * .15f;

        Table menuTable = new Table();
        menuTable.add(menuBtn)
                .width(iconWidth)
                .height(MunhauzenGame.WORLD_WIDTH * .1f);

        float restoreScale = 1f * iconWidth / txt.getWidth();
        float restoreHeight = restoreScale * txt.getHeight();

        Table restoreTable = new Table();
        restoreTable.add(restoreBtn)
                .width(iconWidth)
                .height(restoreHeight)
                .center();

        menuContainer = new Container<>(menuTable);
        menuContainer.align(Align.bottomLeft);
        menuContainer.pad(10);
        menuContainer.setVisible(false);

        restoreContainer = new Container<>(restoreTable);
        restoreContainer.align(Align.bottomRight);
        restoreContainer.pad(10);
        restoreContainer.setVisible(false);

        root = new FragmentRoot();
        root.addContainer(menuContainer);
        root.addContainer(restoreContainer);

        root.setName(tag);
        root.setVisible(false);
    }

    public void fadeIn() {
        root.addAction(Actions.sequence(
                Actions.alpha(0),
                Actions.visible(true),
                Actions.alpha(1, .2f)
        ));
    }

    public void fadeInRestore() {
        restoreContainer.addAction(Actions.sequence(
                Actions.alpha(0),
                Actions.visible(true),
                Actions.alpha(1, .2f)
        ));
    }

    public void fadeOutRestore() {
        restoreContainer.addAction(Actions.sequence(
                Actions.alpha(1),
                Actions.visible(true),
                Actions.alpha(0, .2f),
                Actions.visible(false)
        ));
    }

    public void update() {
        menuContainer.setVisible(
                screen.game.gameState != null
                        && screen.game.gameState.expansionInfo != null
                        && screen.game.gameState.expansionInfo.isCompleted
        );
    }

    @Override
    public Actor getRoot() {
        return root;
    }

    private ImageButton getMenuBtn() {
        Texture txt = screen.game.internalAssetManager.get("purchase/b_menu.png", Texture.class);

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
                    screen.game.sfxService.onBackToMenuClicked();

                    screen.navigateTo(new MenuScreen(screen.game));
                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }
        });

        return btn;
    }

    private ImageButton getRestoreBtn(Texture txt) {

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
                    screen.game.sfxService.onAnyBtnClicked();

                    fadeOutRestore();

                    screen.restorePurchases();
                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }
        });

        return btn;
    }
}
