package ua.gram.munhauzen.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.screen.GameScreen;

public class ScenarioBar extends ProgressBar {

    final SpriteDrawable knob;

    public static ProgressBar.ProgressBarStyle getDefaultStyle(GameScreen screen) {

        Texture line = screen.assetManager.get("ui/player_progress_bar_progress.9.jpg", Texture.class);
        Texture knob = screen.assetManager.get("ui/player_progress_bar_knob.png", Texture.class);

        ProgressBar.ProgressBarStyle barStyle = new ProgressBar.ProgressBarStyle();
        barStyle.background = new NinePatchDrawable(new NinePatch(
                line,
                10, 10, 0, 0
        ));
        barStyle.knob = new SpriteDrawable(new Sprite(knob));

        return barStyle;
    }

    @Override
    public void layout() {
        super.layout();

        int controlsSize = MunhauzenGame.WORLD_HEIGHT / 20;

        float originalKnobHeight = knob.getMinHeight();
        float knobScale = originalKnobHeight > 0 ? 1f * controlsSize / originalKnobHeight : 1;

        knob.setMinHeight(controlsSize);
        knob.setMinWidth(knob.getMinWidth() * knobScale);
    }

    public ScenarioBar(GameScreen screen) {
        super(0, 100, 1, false, getDefaultStyle(screen));

        knob = (SpriteDrawable) getStyle().knob;
    }

    public void setEnabled(boolean state) {

        setTouchable(state ? Touchable.enabled : Touchable.disabled);

        knob.getSprite().setAlpha(state ? 1 : 0);

    }
}
