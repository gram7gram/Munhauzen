package ua.gram.munhauzen.interaction;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

import ua.gram.munhauzen.FontProvider;
import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.entity.Story;
import ua.gram.munhauzen.entity.StoryInteraction;
import ua.gram.munhauzen.fragment.SimpleFragment;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.ui.PrimaryButton;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class ContinueInteraction extends AbstractInteraction {

    public Group root;
    public boolean isFadeIn, isFadeOut, isLoaded;
    Button button;

    public ContinueInteraction(GameScreen gameScreen) {
        super(gameScreen);
    }

    @Override
    public void start() {
        super.start();

        gameScreen.showProgressBar();

        assetManager.load("continue/btn_enabled.png", Texture.class);
        assetManager.load("continue/btn_disabled.png", Texture.class);
    }

    private void onResourcesLoaded() {

        root = new Group();

        button = button("Continue", new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                complete();
            }
        });


        Table table = new Table();
        table.setFillParent(true);
        table.pad(10);
        table.add(button).center()
                .width(MunhauzenGame.WORLD_WIDTH / 3f)
                .height(MunhauzenGame.WORLD_HEIGHT / 12f);

        root.addActor(table);

        root.addAction(Actions.sequence(
                Actions.alpha(0),
                Actions.alpha(1, .3f)
        ));

        gameScreen.gameLayers.setInteractionLayer(
                new SimpleFragment(root)
        );

        GameState.isPaused = true;

    }

    @Override
    public void update() {
        super.update();

        assetManager.update();

        if (!isLoaded) {
            if (assetManager.isFinished()) {
                onResourcesLoaded();
                isLoaded = true;
            }
            return;
        }

        try {
            Story story = gameScreen.getStory();
            if (story.currentScenario == null) return;

            String name = story.currentScenario.scenario.interaction;
            if (name == null) return;

            StoryInteraction interaction = story.currentInteraction;

            if (interaction != null) {

                interaction.isCompleted = false;
            }
        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }

    private void complete() {

        Log.i(tag, "complete");

        try {
            button.setTouchable(Touchable.disabled);

            root.addAction(
                    Actions.sequence(
                            Actions.alpha(0, .4f),
                            Actions.run(new Runnable() {
                                @Override
                                public void run() {
                                    try {

                                        GameState.isPaused = false;

                                        //Continue interaction is NEVER completed.
                                        //When scrolling back the interaction should reappear
                                        gameScreen.interactionService.destroy();

                                        gameScreen.interactionService.findStoryAfterInteraction();

                                        gameScreen.restoreProgressBarIfDestroyed();
                                    } catch (Throwable e) {
                                        Log.e(tag, e);
                                    }
                                }
                            })
                    )
            );
        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }

    public void fadeIn() {

        if (root.isVisible()) return;
        if (isFadeIn) return;

        isFadeOut = false;
        isFadeIn = true;

        Log.i(tag, "fadeIn");

        root.setVisible(true);
        root.clearActions();
        root.addAction(
                Actions.sequence(
                        Actions.fadeIn(.3f),
                        Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                isFadeIn = false;
                                isFadeOut = false;
                            }
                        })
                )
        );
    }

    public void fadeOut() {

        if (!canFadeOut()) return;

        isFadeIn = false;
        isFadeOut = true;

        Log.i(tag, "fadeOut");

        root.clearActions();
        root.addAction(
                Actions.sequence(
                        Actions.fadeOut(.5f),
                        Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                root.setVisible(false);

                                isFadeIn = false;
                                isFadeOut = false;
                            }
                        })
                )
        );
    }

    @Override
    public void dispose() {
        super.dispose();

        isLoaded = false;
        assetManager.clear();
    }

    public boolean canFadeOut() {
        return root.isVisible() && !isFadeOut;
    }

    public PrimaryButton button(String text, final ClickListener onClick) {
        Texture dangerEnabled = assetManager.get("continue/btn_enabled.png", Texture.class);
        Texture dangerDisabled = assetManager.get("continue/btn_disabled.png", Texture.class);

        NinePatchDrawable background1 = new NinePatchDrawable(new NinePatch(dangerEnabled, 90, 90, 0, 0));
        NinePatchDrawable background2 = new NinePatchDrawable(new NinePatch(dangerDisabled, 90, 90, 0, 0));

        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = gameScreen.game.fontProvider.getFont(FontProvider.h4);
        style.up = background1;
        style.down = background1;
        style.disabled = background2;
        style.fontColor = Color.BLACK;

        PrimaryButton button = new PrimaryButton(text, style);

        button.addListener(onClick);

        return button;
    }
}
