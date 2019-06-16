package ua.gram.munhauzen.interaction;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import ua.gram.munhauzen.fragment.Fragment;
import ua.gram.munhauzen.screen.GameScreen;

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

        if (gameScreen.progressBarFragment != null) {
            gameScreen.progressBarFragment.fadeOut();
        }

        Button button = gameScreen.game.buttonBuilder.primary("Продолжить", new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                root.setTouchable(Touchable.disabled);

                root.addAction(
                        Actions.sequence(
                                Actions.alpha(0, .4f),
                                Actions.run(new Runnable() {
                                    @Override
                                    public void run() {
                                        gameScreen.interactionService.destroy();
                                    }
                                })
                        )
                );
            }
        });

        root = new Table();
        root.setFillParent(true);
        root.add(button).expand().align(Align.bottom).pad(10, 10, 30, 10);

        root.addAction(Actions.sequence(
                Actions.alpha(0),
                Actions.delay(1),
                Actions.alpha(1, .4f)
        ));

        gameScreen.gameLayers.setInteractionLayer(
                new Fragment(root)
        );

    }

    @Override
    public void update() {
        super.update();

    }

    @Override
    public void dispose() {
        super.dispose();

    }
}
