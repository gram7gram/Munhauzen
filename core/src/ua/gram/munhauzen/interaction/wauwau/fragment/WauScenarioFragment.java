package ua.gram.munhauzen.interaction.wauwau.fragment;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;

import java.util.ArrayList;
import java.util.HashMap;

import ua.gram.munhauzen.FontProvider;
import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.animation.CannonAnimation;
import ua.gram.munhauzen.animation.CannonLetterAnimation;
import ua.gram.munhauzen.entity.Decision;
import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.entity.ScenarioTranslation;
import ua.gram.munhauzen.fragment.Fragment;
import ua.gram.munhauzen.interaction.WauInteraction;
import ua.gram.munhauzen.interaction.wauwau.WauScenario;
import ua.gram.munhauzen.interaction.wauwau.WauStory;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.ui.FitImage;
import ua.gram.munhauzen.ui.WrapLabel;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class WauScenarioFragment extends Fragment {

    private final String tag = getClass().getSimpleName();
    private final WauInteraction interaction;
    private final MunhauzenGame game;
    public final GameScreen gameScreen;
    public final AssetManager assetManager;
    private FitImage imgLeft, imgRight, imgTop;
    private Table decorLeft, decorRight, decorTop;
    public Stack root;
    private final ArrayList<Actor> buttonList;
    private final HashMap<Integer, String> map = new HashMap<>(7);
    private final HashMap<Integer, String> animatedMap = new HashMap<>(7);
    private final float headerSize, buttonSize;

    public WauScenarioFragment(GameScreen gameScreen, WauInteraction interaction) {
        this.game = gameScreen.game;
        this.gameScreen = gameScreen;
        this.interaction = interaction;

        assetManager = new AssetManager();
        buttonList = new ArrayList<>(4);

        headerSize = MunhauzenGame.WORLD_HEIGHT / 20f;
        buttonSize = MunhauzenGame.WORLD_WIDTH * 3 / 4f;

        animatedMap.put(0, "GameScreen/an_letter_sheet_A.png");
        animatedMap.put(1, "GameScreen/an_letter_sheet_B.png");
        animatedMap.put(2, "GameScreen/an_letter_sheet_C.png");
        animatedMap.put(3, "GameScreen/an_letter_sheet_D.png");

        map.put(0, "GameScreen/an_letter_A_main.png");
        map.put(1, "GameScreen/an_letter_B_main.png");
        map.put(2, "GameScreen/an_letter_C_main.png");
        map.put(3, "GameScreen/an_letter_D_main.png");
    }

    @Override
    public Actor getRoot() {
        return root;
    }

    @Override
    public void dispose() {
        super.dispose();
        assetManager.dispose();
        buttonList.clear();
    }

    public void create(ArrayList<Decision> decisions) {

        Log.i(tag, "create x" + decisions.size());

        interaction.showProgressBar();

//        assetManager.load("sfx/sfx_decision.mp3", Sound.class);
        assetManager.load("GameScreen/an_cannons_main.png", Texture.class);
        assetManager.load("GameScreen/b_star_game.png", Texture.class);
        assetManager.load("GameScreen/b_tulip_1.png", Texture.class);
        assetManager.load("GameScreen/b_decision_add_line.png", Texture.class);
        assetManager.load("GameScreen/b_decision_first_line.png", Texture.class);
        assetManager.load("GameScreen/b_decision_last_line.png", Texture.class);

        assetManager.finishLoading();

        Texture borders = assetManager.get("GameScreen/b_tulip_1.png", Texture.class);
        Texture drawableTop = assetManager.get("GameScreen/b_star_game.png", Texture.class);

        final Table buttons = new Table();
        buttons.add()
                .height(interaction.progressBarFragment.getHeight() / 2 - 20)
                .row();

        for (int i = 0; i < decisions.size(); i++) {

            final Decision decision = decisions.get(i);

            ScenarioTranslation translation = null;

            for (WauScenario scenario : interaction.scenarioRegistry) {
                if (decision.scenario.equals(scenario.name)) {

                    for (ScenarioTranslation item : scenario.translations) {
                        if (game.params.locale.equals(item.locale)) {
                            translation = item;
                            break;
                        }
                    }

                    break;
                }
            }

            String text;
            if (translation == null) {
                Log.e(tag, "Missing translation for decision " + decision.scenario);
                text = decision.scenario;
            } else {
                text = translation.text;
            }

            final int currentIndex = i;

            Actor button = primaryDecision(text, i, buttonSize, new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);

                    makeDecision(currentIndex, decision);
                }
            });

            buttonList.add(button);

            buttons.add(button)
                    .width(buttonSize)
                    .maxWidth(1000)
                    .pad(10).row();

        }

        buttons.add()
                .height(interaction.progressBarFragment.getHeight() + 20)
                .row();

        ScrollPane scrollPane = new ScrollPane(buttons);
        scrollPane.setScrollingDisabled(true, false);

        Sprite drawableLeft = new Sprite(borders);

        Sprite drawableRight = new Sprite(borders);
        drawableRight.setFlip(true, false);

        imgLeft = new FitImage(new SpriteDrawable(drawableLeft));
        imgRight = new FitImage(new SpriteDrawable(drawableRight));
        imgTop = new FitImage(drawableTop);

        Table table = new Table();
        table.add(scrollPane).expandY().fillY().top();

        decorLeft = new Table();
        decorLeft.setTouchable(Touchable.disabled);
        decorLeft.add(imgLeft).align(Align.topLeft).expand()
                .width(MunhauzenGame.WORLD_WIDTH / 3f)
                .height(MunhauzenGame.WORLD_HEIGHT / 4f);

        decorTop = new Table();
        decorTop.setTouchable(Touchable.disabled);
        decorTop.add(imgTop).align(Align.top).expand()
                .width(MunhauzenGame.WORLD_WIDTH / 5f)
                .height(MunhauzenGame.WORLD_HEIGHT / 13f);

        decorRight = new Table();
        decorRight.setTouchable(Touchable.disabled);
        decorRight.add(imgRight).align(Align.topRight).expand()
                .width(MunhauzenGame.WORLD_WIDTH / 3f)
                .height(MunhauzenGame.WORLD_HEIGHT / 4f);

        root = new Stack();
        root.add(table);
        root.add(decorLeft);
        root.add(decorTop);
        root.add(decorRight);

        fadeIn();

        GameState.pause();

        root.setName(tag);
    }

    private void makeDecision(final int currentIndex, Decision decision) {
        try {
            Log.i(tag, "makeDecision " + decision.scenario);

//            Sound sfx = assetManager.get("sfx/sfx_decision.mp3", Sound.class);
//            sfx.play();

            GameState.unpause();

            final Runnable onComplete = new Runnable() {
                @Override
                public void run() {

                    Log.i(tag, "fadeOut button complete");

                    if (interaction.scenarioFragment != null) {
                        interaction.scenarioFragment.destroy();
                        interaction.scenarioFragment = null;
                    }
                }
            };

            fadeOutDecoration();

            try {
                WauStory newStory = interaction.storyManager.create(decision.scenario);

                interaction.storyManager.story = newStory;

                interaction.storyManager.startLoadingResources();
            } catch (Throwable e) {
                Log.e(tag, e);

                interaction.gameScreen.onCriticalError(e);
                return;
            }

            if (decision.scenario.equals("awau_2_a")) {

                interaction.wauCounter += 1;

                Log.i(tag, "wauCounter=" + interaction.wauCounter + "/" + interaction.maxWauCounter);
            }

            //let cannon animation complete...
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    try {
                        for (Actor button : buttonList) {

                            boolean isCurrent = buttonList.indexOf(button) == currentIndex;

                            button.setTouchable(Touchable.disabled);

                            Log.i(tag, "fadeOut button " + (isCurrent ? "+" : "-"));

                            if (isCurrent) {
                                button.addAction(
                                        Actions.sequence(
                                                Actions.delay(.5f),
                                                Actions.fadeOut(.5f),
                                                Actions.run(onComplete)
                                        )
                                );
                            } else {
                                button.addAction(Actions.fadeOut(.5f));
                            }
                        }
                    } catch (Throwable e) {
                        Log.e(tag, e);
                    }
                }
            }, 1);
        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }

    public void fadeOut(Runnable task) {

        if (!isMounted()) return;

        float duration = .3f;

        root.addAction(Actions.sequence(
                Actions.alpha(0, duration),
                Actions.visible(false),
                Actions.run(task)
        ));

        fadeOutDecoration();
    }

    private void fadeOutDecoration() {

        if (!isMounted()) return;

        float duration = .5f;

        decorLeft.addAction(Actions.parallel(
                Actions.moveTo(-decorLeft.getWidth(), 0, duration),
                Actions.alpha(0, duration)
        ));

        decorTop.addAction(Actions.parallel(
                Actions.moveTo(0, decorTop.getHeight(), duration),
                Actions.alpha(0, duration)
        ));

        decorRight.addAction(
                Actions.parallel(
                        Actions.moveTo(decorRight.getWidth(), 0, duration),
                        Actions.alpha(0, duration)
                ));
    }

    public void fadeIn() {

        if (!isMounted()) return;

        root.setVisible(true);
        root.addAction(Actions.sequence(
                Actions.alpha(0),
                Actions.alpha(1, .3f)
        ));

        fadeInDecoration();
    }

    private void fadeInDecoration() {

        if (!isMounted()) return;

        float duration = .3f;

        decorLeft.addAction(Actions.moveBy(-imgLeft.getWidth(), 0));
        decorLeft.addAction(Actions.alpha(0));
        decorLeft.addAction(
                Actions.parallel(
                        Actions.moveTo(0, 0, duration),
                        Actions.alpha(1, duration)
                )

        );

        decorTop.addAction(Actions.moveBy(0, imgTop.getHeight()));
        decorTop.addAction(Actions.alpha(0));
        decorTop.addAction(
                Actions.parallel(
                        Actions.moveTo(0, 0, duration),
                        Actions.alpha(1, duration)
                )

        );

        decorRight.addAction(Actions.moveBy(imgRight.getWidth(), 0));
        decorRight.addAction(Actions.alpha(0));
        decorRight.addAction(
                Actions.parallel(
                        Actions.moveTo(0, 0, duration),
                        Actions.alpha(1, duration)
                )

        );
    }

    private Actor primaryDecision(String text, final int index, float buttonBounds, final ClickListener onClick) {

        Texture bottom = assetManager.get("GameScreen/b_decision_last_line.png", Texture.class);
        Texture middle = assetManager.get("GameScreen/b_decision_add_line.png", Texture.class);
        Texture top = assetManager.get("GameScreen/b_decision_first_line.png", Texture.class);

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

        final Stack header = createDefaultHeader(index);

        final Stack stack = new Stack();
        stack.addActorAt(0, table);
        stack.addActorAt(1, header);

        stack.addListener(new ClickListener() {
            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                super.clicked(event, x, y);

                root.setTouchable(Touchable.disabled);

                try {
                    Stack animated = createAnimatedHeader(index);

                    stack.removeActor(header);
                    stack.addActorAt(1, animated);

                    onClick.clicked(event, x, y);
                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }
        });

        stack.setName("harePrimaryDecision-" + index);

        return stack;
    }

    private Stack createDefaultHeader(int index) {

        if (!map.containsKey(index)) {
            throw new NullPointerException("Missing letter for decision at " + index);
        }

        String letterResource = map.get(index);

        assetManager.load(letterResource, Texture.class);

        assetManager.finishLoading();

        Texture cannon = assetManager.get("GameScreen/an_cannons_main.png", Texture.class);
        Texture letter = assetManager.get(letterResource, Texture.class);

        SpriteDrawable cannonDrawable = new SpriteDrawable(new Sprite(cannon));
        SpriteDrawable cannonDrawableRight = new SpriteDrawable(new Sprite(cannon));
        cannonDrawableRight.getSprite().setFlip(true, false);

        FitImage left = new FitImage(cannonDrawable);
        FitImage right = new FitImage(cannonDrawableRight);
        FitImage center = new FitImage(letter);

        Table layer1 = new Table();
        layer1.add(left).expand()
                .align(Align.topLeft)
                .padLeft(buttonSize / 2 - headerSize * 3)
                .height(headerSize).width(headerSize * 2);

        Table layer2 = new Table();
        layer2.add(center).expand()
                .align(Align.top)
                .size(headerSize);

        Table layer3 = new Table();
        layer3.add(right).expand()
                .align(Align.topRight)
                .padRight(buttonSize / 2 - headerSize * 3)
                .height(headerSize).width(headerSize * 2);

        Stack root = new Stack();
        root.setFillParent(true);
        root.add(layer1);
        root.add(layer2);
        root.add(layer3);

        return root;
    }

    private Stack createAnimatedHeader(int index) {

        if (!animatedMap.containsKey(index)) {
            throw new NullPointerException("Missing letter for decision at " + index);
        }

        String letterResource = animatedMap.get(index);

        assetManager.load(letterResource, Texture.class);
        assetManager.load("GameScreen/an_cannons_sheet.png", Texture.class);
        assetManager.load("GameScreen/an_cannons_left_sheet.png", Texture.class);

        assetManager.finishLoading();

        Texture letter = assetManager.get(letterResource, Texture.class);
        Texture sheet = assetManager.get("GameScreen/an_cannons_sheet.png", Texture.class);
        Texture sheetLeft = assetManager.get("GameScreen/an_cannons_left_sheet.png", Texture.class);

        CannonLetterAnimation center = new CannonLetterAnimation(letter);
        CannonAnimation left = new CannonAnimation(sheet);
        CannonAnimation right = new CannonAnimation(sheetLeft);

        left.start();
        right.start();
        center.start();

        Table layer1 = new Table();
        layer1.add(left).expand()
                .align(Align.topLeft)
                .padLeft(buttonSize / 2 - headerSize * 3)
                .height(headerSize).width(headerSize * 2);

        Table layer2 = new Table();
        layer2.add(center).expand()
                .align(Align.top).padLeft(30)
                .size(headerSize * 2f);

        Table layer3 = new Table();
        layer3.add(right).expand()
                .align(Align.topRight)
                .padRight(buttonSize / 2 - headerSize * 3)
                .height(headerSize).width(headerSize * 2);

        Stack root = new Stack();
        root.setFillParent(true);
        root.add(layer1);
        root.add(layer2);
        root.add(layer3);

        return root;
    }
}