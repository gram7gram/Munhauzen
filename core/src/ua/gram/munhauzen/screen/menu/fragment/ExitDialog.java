package ua.gram.munhauzen.screen.menu.fragment;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;

import ua.gram.munhauzen.FontProvider;
import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.screen.MenuScreen;
import ua.gram.munhauzen.ui.FitImage;
import ua.gram.munhauzen.ui.Fragment;
import ua.gram.munhauzen.ui.FragmentRoot;
import ua.gram.munhauzen.ui.PrimaryButton;
import ua.gram.munhauzen.ui.WrapLabel;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class ExitDialog extends Fragment {

    private final String tag = getClass().getSimpleName();
    private final MunhauzenGame game;
    public final MenuScreen screen;
    public FragmentRoot root;
    private final float headerSize, buttonSize;

    public ExitDialog(MenuScreen screen) {
        this.game = screen.game;
        this.screen = screen;

        headerSize = MunhauzenGame.WORLD_HEIGHT / 20f;
        buttonSize = MunhauzenGame.WORLD_WIDTH * 3 / 4f;
    }

    @Override
    public Actor getRoot() {
        return root;
    }

    public void create() {

        screen.assetManager.load("GameScreen/b_decision_last_line.png", Texture.class);
        screen.assetManager.load("GameScreen/b_decision_add_line.png", Texture.class);
        screen.assetManager.load("GameScreen/b_decision_first_line.png", Texture.class);
        screen.assetManager.load("GameScreen/an_cannons_main.png", Texture.class);

        screen.assetManager.finishLoading();

        Log.i(tag, "create");

        PrimaryButton yesBtn = game.buttonBuilder.danger("Yes", new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                Log.i(tag, "yes clicked");

                try {
                    root.setTouchable(Touchable.disabled);

                    int delay = game.sfxService.onExitYesClicked();

                    Timer.instance().scheduleTask(new Timer.Task() {
                        @Override
                        public void run() {
                            onYesClicked();
                        }
                    }, delay / 1000f);

                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }
        });

        PrimaryButton noBtn = game.buttonBuilder.danger("No", new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                Log.i(tag, "no clicked");

                try {
                    root.setTouchable(Touchable.disabled);

                    game.sfxService.onExitNoClicked();

                    onNoClicked();

                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }
        });

        Actor button = primaryDecision("You want to exit?", buttonSize);

        Table table = new Table();
        table.add(yesBtn).left().expandX().padRight(5)
                .width(MunhauzenGame.WORLD_WIDTH / 3f)
                .height(MunhauzenGame.WORLD_HEIGHT / 10f);
        table.add(noBtn).right().expandX().padLeft(5)
                .width(MunhauzenGame.WORLD_WIDTH / 3f)
                .height(MunhauzenGame.WORLD_HEIGHT / 10f);

        Table content = new Table();
        content.add(button).width(buttonSize).maxWidth(1000)
                .padBottom(10).row();
        content.add(table).width(buttonSize)
                .maxWidth(1000);

        Container<Table> container = new Container<>(content);
        container.pad(MunhauzenGame.WORLD_WIDTH * .05f);

        Pixmap px = new Pixmap(1, 1, Pixmap.Format.RGBA4444);
        px.setColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, .3f);
        px.fill();

        container.setBackground(new SpriteDrawable(new Sprite(new Texture(px))));

        container.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                if (event.isHandled()) return;

                Log.i(tag, "root clicked");

                fadeOut(new Runnable() {
                    @Override
                    public void run() {
                        destroy();
                        screen.exitDialog = null;
                    }
                });
            }
        });

        root = new FragmentRoot();
        root.addContainer(container);

        root.setVisible(false);
    }

    private void onNoClicked() {
        fadeOut(new Runnable() {
            @Override
            public void run() {
                destroy();
                screen.exitDialog = null;
            }
        });
    }

    private void onYesClicked() {
        Gdx.app.exit();
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

    private Actor primaryDecision(String text, float buttonBounds) {

        Texture bottom = screen.assetManager.get("GameScreen/b_decision_last_line.png", Texture.class);
        Texture middle = screen.assetManager.get("GameScreen/b_decision_add_line.png", Texture.class);
        Texture top = screen.assetManager.get("GameScreen/b_decision_first_line.png", Texture.class);

        final NinePatchDrawable middleBackground = new NinePatchDrawable(new NinePatch(
                middle, 0, 0, 5, 5
        ));

        Image backMiddle = new Image(middleBackground);
        Image backBottom = new Image(bottom);
        Image backTop = new Image(top);

        BitmapFont font = game.fontProvider.getFont(FontProvider.h2);

        Label label = new WrapLabel(text,
                new Label.LabelStyle(font, Color.BLACK),
                buttonBounds);
        label.setAlignment(Align.center);

        Table labelContainer = new Table();
        labelContainer.add(label).center().fillX().expand()
                .padTop(5).padBottom(5)
                .padLeft(headerSize / 5f).padRight(headerSize / 5f);

        Stack stackMiddle = new Stack();
        stackMiddle.addActor(backMiddle);
        stackMiddle.addActor(labelContainer);

        final Table table = new Table();

        table.add(backTop)
                .expandX().height(headerSize).row();
        table.add(stackMiddle).row();
        table.add(backBottom)
                .expandX().height(50).row();

        final Stack header = createDefaultHeader();

        final Stack stack = new Stack();
        stack.addActorAt(0, table);
        stack.addActorAt(1, header);

        return stack;
    }

    private Stack createDefaultHeader() {

        Texture cannon = screen.assetManager.get("GameScreen/an_cannons_main.png", Texture.class);

        SpriteDrawable cannonDrawable = new SpriteDrawable(new Sprite(cannon));
        SpriteDrawable cannonDrawableRight = new SpriteDrawable(new Sprite(cannon));
        cannonDrawableRight.getSprite().setFlip(true, false);

        FitImage left = new FitImage(cannonDrawable);
        FitImage right = new FitImage(cannonDrawableRight);

        Table layer1 = new Table();
        layer1.add(left).expand()
                .align(Align.topLeft)
                .padLeft(buttonSize / 2 - headerSize * 3)
                .height(headerSize).width(headerSize * 2);

        Table layer3 = new Table();
        layer3.add(right).expand()
                .align(Align.topRight)
                .padRight(buttonSize / 2 - headerSize * 3)
                .height(headerSize).width(headerSize * 2);

        Stack root = new Stack();
        root.setFillParent(true);
        root.add(layer1);
        root.add(layer3);

        return root;
    }

    public void update() {

    }
}