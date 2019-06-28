package ua.gram.munhauzen.interaction;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.fragment.Fragment;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class ContinueInteraction extends AbstractInteraction {

    public Group root;
    public boolean isFadeIn;
    public boolean isFadeOut;

    public ContinueInteraction(GameScreen gameScreen) {
        super(gameScreen);
    }

    @Override
    public void start() {
        super.start();

        root = new Group();

        final Button button = gameScreen.game.buttonBuilder.danger("Continue", new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                try {
                    root.setTouchable(Touchable.disabled);

                    root.addAction(
                            Actions.sequence(
                                    Actions.alpha(0, .4f),
                                    Actions.run(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                gameScreen.interactionService.complete();

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
        });


        Table table = new Table();
        table.setFillParent(true);
        table.pad(10);
        table.add(button).center().height(MunhauzenGame.WORLD_HEIGHT / 10f);

        root.addActor(table);

        root.addAction(Actions.sequence(
                Actions.alpha(0),
                Actions.alpha(1, .3f)
        ));

        gameScreen.gameLayers.setInteractionLayer(
                new Fragment(root)
        );

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

    public boolean canFadeOut() {
        return root.isVisible() && !isFadeOut;
    }
}
