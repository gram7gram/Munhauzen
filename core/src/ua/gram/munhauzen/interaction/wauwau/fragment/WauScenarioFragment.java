package ua.gram.munhauzen.interaction.wauwau.fragment;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;

import java.util.ArrayList;

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
import ua.gram.munhauzen.ui.ScenarioFragment;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class WauScenarioFragment extends ScenarioFragment {

    private final WauInteraction interaction;

    public WauScenarioFragment(GameScreen gameScreen, WauInteraction interaction) {
        super(gameScreen);
        this.interaction = interaction;
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

            try {
                interaction.assetManager.load(letterResource, Texture.class);
            } catch (Throwable ignore) {
            }
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

            PrimaryDecision button = new PrimaryDecision(this, interaction.assetManager);
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

            boolean isVisited = screen.game.gameState.activeSave.visitedStories.contains(decision.scenario);
            button.setVisited(isVisited);

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

        SpriteDrawable bg = new SpriteDrawable(new Sprite(
                game.internalAssetManager.get("p0.jpg", Texture.class)
        ));
        bgContainer = new Container<>();
        bgContainer.background(bg);
        bgContainer.getColor().a = .4f;

        root = new Stack();
        root.add(bgContainer);
        root.add(blocks);
        root.add(decorLeft);
        root.add(decorTop);
        root.add(decorRight);

        GameState.pause(tag);

        root.setName(tag);
        root.setVisible(false);
    }

    public void makeDecision(final int currentIndex, Decision decision) {
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

}