package ua.gram.munhauzen.interaction.wauwau.fragment;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;

import java.util.ArrayList;
import java.util.HashMap;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.Decision;
import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.entity.Translation;
import ua.gram.munhauzen.interaction.WauInteraction;
import ua.gram.munhauzen.interaction.wauwau.WauScenario;
import ua.gram.munhauzen.interaction.wauwau.WauStory;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.screen.game.ui.PrimaryDecision;
import ua.gram.munhauzen.ui.FitImage;
import ua.gram.munhauzen.ui.Fragment;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class WauScenarioFragment extends Fragment {

    private final String tag = getClass().getSimpleName();
    private final WauInteraction interaction;
    private final MunhauzenGame game;
    public final GameScreen gameScreen;
    private FitImage imgLeft, imgRight, imgTop;
    private Table decorLeft;
    private Table decorRight;
    private Table decorTop;
    public Table blocks;
    public Stack root;
    private final ArrayList<Actor> buttonList;
    private final HashMap<Integer, String> animatedMap = new HashMap<>(7);
    public boolean isFadeIn, isFadeOut;

    public WauScenarioFragment(GameScreen gameScreen, WauInteraction interaction) {
        this.game = gameScreen.game;
        this.gameScreen = gameScreen;
        this.interaction = interaction;

        buttonList = new ArrayList<>();

        animatedMap.put(0, "GameScreen/an_letter_A_sheet.png");
        animatedMap.put(1, "GameScreen/an_letter_B_sheet.png");
        animatedMap.put(2, "GameScreen/an_letter_C_sheet.png");
        animatedMap.put(3, "GameScreen/an_letter_D_sheet.png");
        animatedMap.put(4, "GameScreen/an_letter_E_sheet.png");
        animatedMap.put(5, "GameScreen/an_letter_F_sheet.png");
        animatedMap.put(6, "GameScreen/an_letter_G_sheet.png");
    }

    @Override
    public Actor getRoot() {
        return root;
    }

    @Override
    public void dispose() {
        super.dispose();

        buttonList.clear();
    }

    public void create(ArrayList<Decision> decisions) {

        Log.i(tag, "create x" + decisions.size());

        interaction.showProgressBar();

        interaction.assetManager.load("GameScreen/an_cannons_sheet.png", Texture.class);
        interaction.assetManager.load("GameScreen/an_cannons_left_sheet.png", Texture.class);
        interaction.assetManager.load("GameScreen/b_star_game.png", Texture.class);
        interaction.assetManager.load("GameScreen/b_tulip_1.png", Texture.class);
        interaction.assetManager.load("GameScreen/b_decision_add_line.png", Texture.class);
        interaction.assetManager.load("GameScreen/b_decision_first_line.png", Texture.class);
        interaction.assetManager.load("GameScreen/b_decision_last_line.png", Texture.class);

        for (int i = 0; i < decisions.size(); i++) {
            String letterResource = animatedMap.get(i);

            interaction.assetManager.load(letterResource, Texture.class);
        }

        interaction.assetManager.finishLoading();

        Texture borders = interaction.assetManager.get("GameScreen/b_tulip_1.png", Texture.class);
        Texture drawableTop = interaction.assetManager.get("GameScreen/b_star_game.png", Texture.class);

        final Table buttons = new Table();
        buttons.add()
                .height(interaction.progressBarFragment.getHeight() / 2 - 20)
                .row();

        for (int i = 0; i < decisions.size(); i++) {

            final Decision decision = decisions.get(i);

            Translation translation = null;

            for (WauScenario scenario : interaction.scenarioRegistry) {
                if (decision.scenario.equals(scenario.name)) {

                    for (Translation item : scenario.translations) {
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

            PrimaryDecision button = new PrimaryDecision(interaction.gameScreen.game, interaction.assetManager);
            button.setText(text);
            button.setIndex(currentIndex);
            button.setAnimatedMap(animatedMap);
            button.setOnClick(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);

                    root.setTouchable(Touchable.disabled);

                    try {
                        makeDecision(currentIndex, decision);
                    } catch (Throwable e) {
                        Log.e(tag, e);
                    }
                }
            });

            button.init();

            buttonList.add(button);

            buttons.add(button)
                    .width(button.buttonSize)
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

        blocks = new Table();
        blocks.add(scrollPane).expandY().fillY().top();

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
        root.add(blocks);
        root.add(decorLeft);
        root.add(decorTop);
        root.add(decorRight);

        GameState.pause(tag);

        root.setName(tag);
        root.setVisible(false);
    }

    private void makeDecision(final int currentIndex, Decision decision) {
        try {
            Log.i(tag, "makeDecision " + decision.scenario);

            GameState.unpause(tag);

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

        isFadeIn = false;
        isFadeOut = true;

        float duration = .3f;

        blocks.addAction(Actions.sequence(
                Actions.alpha(0, duration),
                Actions.visible(false),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        isFadeIn = false;
                        isFadeOut = false;
                    }
                }),
                Actions.run(task)
        ));

        fadeOutDecoration();
    }

    public void fadeOut() {

        fadeOutWithoutDecoration();

        fadeOutDecoration();
    }

    public void fadeOutWithoutDecoration() {

        isFadeIn = false;
        isFadeOut = true;

        float duration = .3f;

        blocks.addAction(Actions.sequence(
                Actions.alpha(0, duration),
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

        if (!isMounted()) return;

        root.setVisible(true);

        fadeInWithoutDecoration();

        fadeInDecoration();
    }

    public void fadeInWithoutDecoration() {

        if (!isMounted()) return;

        isFadeIn = true;
        isFadeOut = false;

        blocks.setVisible(true);
        blocks.addAction(Actions.sequence(
                Actions.alpha(0),
                Actions.alpha(1, .3f),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        isFadeIn = false;
                        isFadeOut = false;
                    }
                })
        ));
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

}