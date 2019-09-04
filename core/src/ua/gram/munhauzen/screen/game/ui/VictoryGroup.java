package ua.gram.munhauzen.screen.game.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.screen.game.fragment.VictoryFragment;

public class VictoryGroup extends Group {

    final VictoryFragment fragment;
    public VictoryCircle victoryCircle;
    Image blackTop, blackBottom;

    public VictoryGroup(VictoryFragment fragment) {
        super();

        this.fragment = fragment;

        victoryCircle = new VictoryCircle(fragment);

        Pixmap px = new Pixmap(1, 1, Pixmap.Format.RGB888);
        px.setColor(Color.BLACK);
        px.fill();

        blackTop = new Image(new Texture(px));
        blackTop.setVisible(false);

        blackBottom = new Image(new Texture(px));
        blackBottom.setVisible(false);

        addActor(victoryCircle);
        addActor(blackBottom);
        addActor(blackTop);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        float pad = Math.max(0, (MunhauzenGame.WORLD_HEIGHT - fragment.backgroundImage.backgroundHeight) / 2f);

        victoryCircle.setBounds(
                0,
                pad,
                fragment.backgroundImage.backgroundWidth,
                MunhauzenGame.WORLD_HEIGHT - pad * 2);

        if (pad > 0) {

            blackTop.setVisible(true);
            blackBottom.setVisible(true);

            blackTop.setBounds(
                    0,
                    MunhauzenGame.WORLD_HEIGHT - pad,
                    fragment.backgroundImage.backgroundWidth,
                    pad
            );

            blackBottom.setBounds(
                    0,
                    0,
                    fragment.backgroundImage.backgroundWidth,
                    pad
            );

        } else {
            blackTop.setVisible(false);
            blackBottom.setVisible(false);
        }
    }
}
