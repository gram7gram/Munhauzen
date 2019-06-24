package ua.gram.munhauzen.interaction;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.fragment.Fragment;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class ContinueInteraction extends AbstractInteraction {

    public Table root;

    public ContinueInteraction(GameScreen gameScreen) {
        super(gameScreen);
    }

    @Override
    public void start() {
        super.start();

        Button button = gameScreen.game.buttonBuilder.primary("Continue", new ClickListener() {
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

        root = new Table();
        root.pad(10);
        root.add(button).expand().align(Align.bottom).height(MunhauzenGame.WORLD_HEIGHT / 10f);

        root.addAction(Actions.sequence(
                Actions.alpha(0),
                Actions.alpha(1, .3f)
        ));

        gameScreen.gameLayers.setInteractionLayer(
                new Fragment(root)
        );

    }

    @Override
    public void update() {
        super.update();

        if (gameScreen.progressBarFragment != null) {
            if (gameScreen.progressBarFragment.root.isVisible()) {
                root.padBottom(gameScreen.progressBarFragment.getHeight());
            } else {
                root.padBottom(10);
            }

            root.invalidate();
        }
    }

    @Override
    public void dispose() {
        super.dispose();

    }
}
