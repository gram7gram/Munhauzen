package ua.gram.munhauzen.fragment;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.screen.GameScreen;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class ProgressBarFragment {

    private final GameScreen gameScreen;
    public ProgressBar bar;
    public Stack barContainer;

    public ProgressBarFragment(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    public Stack create() {

        ProgressBar.ProgressBarStyle barStyle = new ProgressBar.ProgressBarStyle();
        barStyle.background = new NinePatchDrawable(new NinePatch(
                gameScreen.assetManager.get("ui/player_progress_bar_progress.9.jpg", Texture.class),
                10, 10, 0, 0
        ));
        barStyle.knob = new SpriteDrawable(new Sprite(
                gameScreen.assetManager.get("ui/player_progress_bar_knob.png", Texture.class)));

        bar = new ProgressBar(0, 100, 1, false, barStyle);

        Image barBackgroundImageLeft = new Image(new NinePatchDrawable(new NinePatch(
                gameScreen.assetManager.get("ui/player_progress_bar.9.png", Texture.class),
                130, 500 - 270, 0, 0
        )));

        Image barBackgroundImageRight = new Image(new NinePatchDrawable(new NinePatch(
                gameScreen.assetManager.get("ui/player_progress_bar_right.9.png", Texture.class),
                260, 500 - 360, 0, 0
        )));

        Table barTable = new Table();
        barTable.pad(40, 100, 40, 100);
        barTable.add().expandX().height(100).row();
        barTable.add(bar).fillX().expandX().height(100).row();

        Table backgroundContainer = new Table();

        backgroundContainer.add(barBackgroundImageLeft).fillX()
                .width(1f * MunhauzenGame.WORLD_WIDTH / 2)
                .height(250);

        backgroundContainer.add(barBackgroundImageRight).fillX()
                .width(1f * MunhauzenGame.WORLD_WIDTH / 2)
                .height(250);

        barContainer = new Stack();
        barContainer.addActor(backgroundContainer);
        barContainer.addActor(barTable);

        return barContainer;
    }
}
