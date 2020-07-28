package ua.gram.munhauzen.screen.game.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.utils.Align;

import ua.gram.munhauzen.entity.Inventory;
import ua.gram.munhauzen.screen.GameScreen;

public class AchievementPopup extends Container<Statue> {

    final GameScreen screen;

    public AchievementPopup(GameScreen screen) {
        this.screen = screen;
    }

    public void create(Inventory inventory) {
        Statue statue = new Statue(screen, inventory);

        setFillParent(true);
        align(Align.bottomLeft);

        setActor(statue);
    }
}
