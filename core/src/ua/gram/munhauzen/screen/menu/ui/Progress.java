package ua.gram.munhauzen.screen.menu.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.AchievementState;
import ua.gram.munhauzen.screen.MenuScreen;
import ua.gram.munhauzen.ui.FixedImage;

public class Progress extends Stack {

    final MenuScreen screen;
    Container<?> color, black;
    public final float width, height;

    public Progress(MenuScreen screen) {

        this.screen = screen;

        Texture txt1 = screen.assetManager.get("menu/progress_black.png", Texture.class);
        Texture txt2 = screen.assetManager.get("menu/progress_color.png", Texture.class);

        width = MunhauzenGame.WORLD_WIDTH * .5f;

        FixedImage progressColor = new FixedImage(txt2, width);
        FixedImage progressBlack = new FixedImage(txt1, width);

        height = progressColor.height;

        color = new Container<>(progressColor);
        color.setFillParent(false);
        color.setClip(true);

        black = new Container<>(progressBlack);
        black.setFillParent(false);
        black.setClip(true);

        setFillParent(true);
        add(black);
        add(color);

        layout();
    }

    @Override
    public void layout() {
        super.layout();

        setWidth(width);
        setHeight(height);

        float progress = 0;

        AchievementState state = screen.game.gameState.achievementState;
        if (state != null) {
            progress = 1f * state.points / screen.game.params.achievementPoints;
        }

        progress = Math.min(1f, progress);

        black.setWidth(width);
        color.setWidth(width * progress);
    }
}
