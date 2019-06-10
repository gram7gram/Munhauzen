package ua.gram.munhauzen.interaction;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import ua.gram.munhauzen.ButtonBuilder;
import ua.gram.munhauzen.fragment.Fragment;
import ua.gram.munhauzen.screen.GameScreen;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class ContinueInteraction extends AbstractInteraction {

    public ContinueInteraction(GameScreen gameScreen) {
        super(gameScreen);
    }

    @Override
    public void start() {
        super.start();

        if (gameScreen.progressBarFragment != null) {
            gameScreen.progressBarFragment.fadeOut();
        }

        Button button = new ButtonBuilder(gameScreen.game).primary("Продолжить", new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                gameScreen.interactionService.destroy();
            }
        });

        Table table = new Table();
        table.setFillParent(true);
        table.add(button).expand().align(Align.bottom).pad(10, 10, 30, 10);

        gameScreen.gameLayers.setInteractionLayer(
                new Fragment(table)
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
