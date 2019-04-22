package ua.gram.munhauzen.fragment;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.Decision;
import ua.gram.munhauzen.entity.Option;
import ua.gram.munhauzen.entity.OptionRepository;
import ua.gram.munhauzen.entity.Scenario;
import ua.gram.munhauzen.screen.GameScreen;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class ScenarioFragment {

    private final MunhauzenGame game;
    private final GameScreen gameScreen;
    private VerticalGroup group2, group1, group3;
    private Texture decorationTop;
    private Sprite decorLeft, decorRight;

    public ScenarioFragment(GameScreen gameScreen) {
        this.game = gameScreen.game;
        this.gameScreen = gameScreen;
    }

    public Stack create(ArrayList<Decision> decisions) {

        final Stack stack = new Stack();
        stack.setFillParent(true);

        Table table = new Table();
        table.setFillParent(true);

        for (final Decision decision : decisions) {

            Option option = OptionRepository.find(game.gameState, decision.option);

            Button button = game.buttonBuilder.primary(option.text, new Runnable() {
                @Override
                public void run() {

                    final Scenario newScenario = gameScreen.scenarioManager.createScenario(decision.option);

                    gameScreen.scenarioManager.startLoadingResources(newScenario);

                    gameScreen.scenarioFragment.fadeOut(new Runnable() {
                        @Override
                        public void run() {
                            gameScreen.scenarioFragment = null;

                            gameScreen.uiLayers.removeActor(stack); //animate removal

                            game.gameState.history.activeSave.scenario = newScenario;
                        }
                    });
                }
            });

            table.add(button).width(600).height(150).pad(10).row();
        }

        ScrollPane scrollPane = new ScrollPane(table);
        scrollPane.setScrollingDisabled(true, false);

        Texture borders = gameScreen.assetManager.get("GameScreen/b_tulip_1.png", Texture.class);
        decorationTop = gameScreen.assetManager.get("GameScreen/b_star_game.png", Texture.class);

        decorLeft = new Sprite(borders);

        decorRight = new Sprite(borders);
        decorRight.setFlip(true, false);

        Image decor1 = new Image(new SpriteDrawable(decorLeft));
        Image decor2 = new Image(new SpriteDrawable(decorRight));
        Image decor3 = new Image(decorationTop);

        group1 = new VerticalGroup();
        group1.align(Align.topLeft);
        group1.addActor(decor1);

        group2 = new VerticalGroup();
        group2.align(Align.top);
        group2.addActor(decor3);

        group3 = new VerticalGroup();
        group3.align(Align.topRight);
        group3.addActor(decor2);

        stack.addActorAt(0, group1);
        stack.addActorAt(1, group2);
        stack.addActorAt(2, group3);
        stack.addActorAt(3, scrollPane);

        fadeIn();

        return stack;
    }

    private void fadeOut(Runnable task) {
        float duration = .5f;

        group1.addAction(Actions.parallel(
                Actions.moveTo(-decorLeft.getWidth(), 0, duration),
                Actions.alpha(0, duration)
        ));

        group2.addAction(Actions.parallel(
                Actions.moveTo(0, decorationTop.getHeight(), duration),
                Actions.alpha(0, duration)
        ));

        group3.addAction(Actions.sequence(
                Actions.parallel(
                        Actions.moveTo(decorRight.getWidth(), 0, duration),
                        Actions.alpha(0, duration)
                ),
                Actions.run(task))
        );
    }

    private void fadeIn() {
        float duration = .5f;

        group1.setX(group1.getX() - decorLeft.getWidth());
        group1.addAction(Actions.alpha(0));
        group1.addAction(Actions.parallel(
                Actions.moveTo(0, 0, duration),
                Actions.alpha(1, duration)
        ));

        group2.setY(group1.getY() + decorationTop.getHeight());
        group2.addAction(Actions.alpha(0));
        group2.addAction(Actions.parallel(
                Actions.moveTo(0, 0, duration),
                Actions.alpha(1, duration)
        ));

        group3.setX(group3.getX() + decorRight.getWidth());
        group3.addAction(Actions.alpha(0));
        group3.addAction(Actions.parallel(
                Actions.moveTo(0, 0, duration),
                Actions.alpha(1, duration)
        ));
    }
}
