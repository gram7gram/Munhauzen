package ua.gram.munhauzen.screen.painting.fragment;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;

import ua.gram.munhauzen.ButtonBuilder;
import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.screen.GalleryScreen;
import ua.gram.munhauzen.screen.PaintingScreen;
import ua.gram.munhauzen.screen.gallery.entity.PaintingImage;
import ua.gram.munhauzen.ui.Fragment;
import ua.gram.munhauzen.ui.FragmentRoot;
import ua.gram.munhauzen.ui.PrimaryButton;
import ua.gram.munhauzen.ui.ProgressIconButton;
import ua.gram.munhauzen.utils.Log;

public class ControlsFragment extends Fragment {

    private final String tag = getClass().getSimpleName();
    private final PaintingScreen screen;
    public FragmentRoot root;
    public ImageButton leftArrow, rightArrow;

    public ControlsFragment(PaintingScreen screen) {
        this.screen = screen;
    }

    public void create() {

        Log.i(tag, "create");

        leftArrow = getArrowLeft();
        rightArrow = getArrowRight();

        PrimaryButton menuBtn = screen.game.buttonBuilder.primary(screen.game.t("painting.back_btn"), new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                try {
                    screen.navigateTo(new GalleryScreen(screen.game));
                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }
        });

        float controlsSize = MunhauzenGame.WORLD_WIDTH / 15f;

        Table arrowsTable = new Table();
        arrowsTable.setTouchable(Touchable.childrenOnly);
        arrowsTable.setFillParent(true);
        arrowsTable.add(leftArrow).size(controlsSize).left();
        arrowsTable.add().center().grow();
        arrowsTable.add(rightArrow).size(controlsSize).right();

        Table menuTable = new Table();
        menuTable.add(menuBtn)
                .width(ButtonBuilder.BTN_PRIMARY_SM_WIDTH)
                .height(ButtonBuilder.BTN_PRIMARY_SM_HEIGHT)
                .row();

        Container menuContainer = new Container<>(menuTable);
        menuContainer.align(Align.bottomRight);
        menuContainer.pad(10);

        Container arrowsContainer = new Container<>(arrowsTable);
        arrowsContainer.align(Align.center);
        arrowsContainer.pad(10);

        root = new FragmentRoot();
        root.addContainer(menuContainer);
        root.addContainer(arrowsTable);

        root.setName(tag);
    }

    public void update() {
        if (screen.paintingFragment == null) return;

        PaintingImage img = screen.paintingFragment.paintingImage;
        if (img == null) return;

        leftArrow.setVisible(img.prev != null);
        rightArrow.setVisible(img.next != null);
    }

    @Override
    public Actor getRoot() {
        return root;
    }

    private ImageButton getArrowRight() {
        Texture img = screen.assetManager.get("ui/playbar_skip_forward.png", Texture.class);

        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.up = new SpriteDrawable(new Sprite(img));
        style.down = new SpriteDrawable(new Sprite(img));
        style.disabled = new SpriteDrawable(new Sprite(img));

        final ImageButton btn = new ProgressIconButton(style);

        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                btn.setTouchable(Touchable.disabled);

                screen.nextPainting();
            }
        });

        return btn;
    }

    private ImageButton getArrowLeft() {
        Texture img = screen.assetManager.get("ui/playbar_skip_backward.png", Texture.class);

        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.up = new SpriteDrawable(new Sprite(img));
        style.down = new SpriteDrawable(new Sprite(img));
        style.disabled = new SpriteDrawable(new Sprite(img));

        final ImageButton btn = new ProgressIconButton(style);

        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                btn.setTouchable(Touchable.disabled);

                screen.prevPainting();
            }
        });

        return btn;
    }
}
