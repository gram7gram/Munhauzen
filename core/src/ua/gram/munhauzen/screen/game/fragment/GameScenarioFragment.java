package ua.gram.munhauzen.screen.game.fragment;

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
import ua.gram.munhauzen.entity.Scenario;
import ua.gram.munhauzen.entity.Story;
import ua.gram.munhauzen.repository.ScenarioRepository;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.screen.game.ui.PrimaryDecision;
import ua.gram.munhauzen.ui.FitImage;
import ua.gram.munhauzen.ui.ScenarioFragment;
import ua.gram.munhauzen.utils.Log;

public class GameScenarioFragment extends ScenarioFragment {
    public final String storyId;

    public GameScenarioFragment(GameScreen gameScreen, String storyId) {
        super(gameScreen);

        this.storyId = storyId;
    }

    public void create(ArrayList<Decision> decisions) {

        Log.i(tag, "create x" + decisions.size());

        gameScreen.showProgressBar();

        gameScreen.assetManager.load("GameScreen/an_cannons_sheet.png", Texture.class);
        gameScreen.assetManager.load("GameScreen/an_cannons_left_sheet.png", Texture.class);
        gameScreen.assetManager.load("GameScreen/b_star_game.png", Texture.class);
        gameScreen.assetManager.load("GameScreen/b_tulip_1.png", Texture.class);
        gameScreen.assetManager.load("GameScreen/b_decision_add_line.png", Texture.class);
        gameScreen.assetManager.load("GameScreen/b_decision_first_line.png", Texture.class);
        gameScreen.assetManager.load("GameScreen/b_decision_last_line.png", Texture.class);

        for (int i = 0; i < decisions.size(); i++) {
            String letterResource = animatedMap.get(i);

            gameScreen.assetManager.load(letterResource, Texture.class);
        }

        gameScreen.assetManager.finishLoading();

        Texture borders = gameScreen.assetManager.get("GameScreen/b_tulip_1.png", Texture.class);
        Texture drawableTop = gameScreen.assetManager.get("GameScreen/b_star_game.png", Texture.class);

        final Table buttons = new Table();
        buttons.add()
                .height(gameScreen.progressBarFragment.getHeight() / 2 - 20)
                .row();

        for (int i = 0; i < decisions.size(); i++) {

            final Decision decision = decisions.get(i);

            Scenario scenario = ScenarioRepository.find(game.gameState, decision.scenario);

            String text = scenario.text;

            final int currentIndex = i;

            PrimaryDecision button = new PrimaryDecision(gameScreen.game, gameScreen.assetManager);
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
                .height(gameScreen.progressBarFragment.getHeight() + 20)
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
            Log.i(tag, "primaryDecision clicked " + decision.scenario);

            GameState.unpause(tag);

            final Runnable onComplete = new Runnable() {
                @Override
                public void run() {

                    try {
                        gameScreen.hideAndDestroyScenarioFragment();
                    } catch (Throwable e) {
                        Log.e(tag, e);
                    }
                }
            };

            fadeOutDecoration();

            Story newStory = gameScreen.storyManager.create(decision.scenario);

            gameScreen.setStory(newStory);

            gameScreen.storyManager.startLoadingResources();

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

            gameScreen.onCriticalError(e);
        }
    }

}
