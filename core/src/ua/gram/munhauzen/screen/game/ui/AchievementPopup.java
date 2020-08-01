package ua.gram.munhauzen.screen.game.ui;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.utils.Align;

import ua.gram.munhauzen.entity.Inventory;
import ua.gram.munhauzen.screen.GameScreen;

public class AchievementPopup extends Container<Statue> {

    Statue statue;
    final GameScreen screen;

    public AchievementPopup(GameScreen screen) {
        this.screen = screen;
    }

    public void create(Inventory inventory) {
        statue = new Statue(screen, inventory);

        setFillParent(true);
        align(Align.bottomLeft);

        setActor(statue);
    }

    public void animate() {
        statue.addAction(Actions.sequence(
                Actions.alpha(0),
                Actions.moveTo(0, -statue.height),
                Actions.parallel(
                        Actions.moveTo(0, 0, .3f),
                        Actions.alpha(1, .15f)
                )
        ));
    }
}
