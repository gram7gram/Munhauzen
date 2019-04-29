package ua.gram.munhauzen.fragment;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Timer;

import java.util.ArrayList;

import ua.gram.munhauzen.FontProvider;
import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.animation.CannonAnimation;
import ua.gram.munhauzen.animation.CannonLetterAnimation;
import ua.gram.munhauzen.entity.Decision;
import ua.gram.munhauzen.entity.Option;
import ua.gram.munhauzen.entity.OptionRepository;
import ua.gram.munhauzen.entity.Scenario;
import ua.gram.munhauzen.screen.GameScreen;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class ScenarioFragment implements Disposable {

    private final MunhauzenGame game;
    public final GameScreen gameScreen;
    public final AssetManager assetManager;
    private VerticalGroup group2, group1, group3;
    private Texture decorationTop;
    private Sprite decorLeft, decorRight;

    public ScenarioFragment(GameScreen gameScreen) {
        this.game = gameScreen.game;
        this.gameScreen = gameScreen;
        assetManager = new AssetManager();
    }

    @Override
    public void dispose() {
        assetManager.dispose();
    }

    public Stack create(ArrayList<Decision> decisions) {

        assetManager.load("GameScreen/b_star_game.png", Texture.class);
        assetManager.load("GameScreen/b_tulip_1.png", Texture.class);
        assetManager.load("GameScreen/b_decision_add_line.png", Texture.class);
        assetManager.load("GameScreen/b_decision_first_line.png", Texture.class);
        assetManager.load("GameScreen/b_decision_last_line.png", Texture.class);
        assetManager.load("GameScreen/an_cannons_sheet.png", Texture.class);
        assetManager.load("GameScreen/an_cannons_left_sheet.png", Texture.class);
        assetManager.load("GameScreen/an_letter_sheet_A.png", Texture.class);
        assetManager.load("GameScreen/an_letter_sheet_B.png", Texture.class);
        assetManager.load("GameScreen/an_letter_sheet_C.png", Texture.class);
        assetManager.load("GameScreen/an_letter_sheet_D.png", Texture.class);
        assetManager.load("GameScreen/an_letter_sheet_E.png", Texture.class);
        assetManager.load("GameScreen/an_letter_sheet_F.png", Texture.class);
        assetManager.load("GameScreen/an_letter_sheet_G.png", Texture.class);

        assetManager.finishLoading();

        Texture borders = assetManager.get("GameScreen/b_tulip_1.png", Texture.class);
        decorationTop = assetManager.get("GameScreen/b_star_game.png", Texture.class);

        final Stack stack = new Stack();
        stack.setFillParent(true);

        Table table = new Table();
        table.setFillParent(true);

        for (int i = 0; i < decisions.size(); i++) {

            final Decision decision = decisions.get(i);

            Option option = OptionRepository.find(game.gameState, decision.option);

            Actor button = primaryDecision(option.text, i, new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);

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

            table.add(button)
                    .minWidth(600)
                    .width(MunhauzenGame.WORLD_WIDTH * 3 / 4f)
                    .maxWidth(1000)
                    .pad(10).row();
        }

        ScrollPane scrollPane = new ScrollPane(table);
        scrollPane.setScrollingDisabled(true, false);

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

        group3.addAction(
                Actions.sequence(
                        Actions.parallel(
                                Actions.moveTo(decorRight.getWidth(), 0, duration),
                                Actions.alpha(0, duration)
                        ),
                        Actions.run(task),
                        Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                dispose();
                            }
                        })
                )
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

    public Actor primaryDecision(String text, int index, final ClickListener onClick) {

        Texture top = assetManager.get("GameScreen/b_decision_first_line.png", Texture.class);
        Texture bottom = assetManager.get("GameScreen/b_decision_last_line.png", Texture.class);
        Texture middle = assetManager.get("GameScreen/b_decision_add_line.png", Texture.class);
        Texture sheet = assetManager.get("GameScreen/an_cannons_sheet.png", Texture.class);
        Texture sheetLeft = assetManager.get("GameScreen/an_cannons_left_sheet.png", Texture.class);

        Texture letter;
        switch (index) {
            case 0:
                letter = assetManager.get("GameScreen/an_letter_sheet_A.png", Texture.class);
                break;
            case 1:
                letter = assetManager.get("GameScreen/an_letter_sheet_B.png", Texture.class);
                break;
            case 2:
                letter = assetManager.get("GameScreen/an_letter_sheet_C.png", Texture.class);
                break;
            case 3:
                letter = assetManager.get("GameScreen/an_letter_sheet_D.png", Texture.class);
                break;
            case 4:
                letter = assetManager.get("GameScreen/an_letter_sheet_E.png", Texture.class);
                break;
            case 5:
                letter = assetManager.get("GameScreen/an_letter_sheet_F.png", Texture.class);
                break;
            case 6:
                letter = assetManager.get("GameScreen/an_letter_sheet_G.png", Texture.class);
                break;
            default:
                throw new NullPointerException("Missing letter for decision at " + index);
        }

        final CannonAnimation cannonAnimationLeft = new CannonAnimation(sheet);
        final CannonAnimation cannonAnimationRight = new CannonAnimation(sheetLeft);
//        cannonAnimationRight.flipX = true;

        final CannonLetterAnimation cannonLetterAnimation = new CannonLetterAnimation(letter);

        final NinePatchDrawable middleBackground = new NinePatchDrawable(new NinePatch(
                middle, 0, 0, 5, 5
        ));

        Image backTop = new Image(top);
        Image backMiddle = new Image(middleBackground);
        Image backBottom = new Image(bottom);

        Label label = new Label(text, new Label.LabelStyle(
                game.fontProvider.getFont(FontProvider.BuxtonSketch, FontProvider.h3), Color.BLACK
        ));
        label.setAlignment(Align.center);
        label.setWrap(true);

        final Table cannonTable = new Table();
        cannonTable.add(cannonAnimationLeft).right().padLeft(80);
        cannonTable.add(cannonAnimationRight).left().padRight(80);

        Stack stackTop = new Stack();
        stackTop.addActor(backTop);
        stackTop.addActor(cannonTable);

        Stack stackMiddle = new Stack();
        stackMiddle.addActor(backMiddle);
        stackMiddle.addActor(label);

        Table tableBack = new Table();

        tableBack.add(stackTop).fillX().expandX().height(100).row();
        tableBack.add(stackMiddle).fillX().expandX().row();
        tableBack.add(backBottom).fillX().expandX().height(65).row();

        tableBack.addListener(new ClickListener() {
            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                super.clicked(event, x, y);

                cannonAnimationLeft.start();
                cannonAnimationRight.start();
                cannonLetterAnimation.start();

                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        onClick.clicked(event, x, y);
                    }
                }, 0.5f);

            }
        });

        Table letterTable = new Table();
        letterTable.setFillParent(true);
        letterTable.add(cannonLetterAnimation).expand().top().padLeft(30);

        Stack stack = new Stack();
        stack.add(tableBack);
        stack.add(letterTable);

        return stack;
    }
}
