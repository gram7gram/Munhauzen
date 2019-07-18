package ua.gram.munhauzen.interaction.timer.fragment;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.fragment.Fragment;
import ua.gram.munhauzen.interaction.TimerInteraction;
import ua.gram.munhauzen.ui.FitImage;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class TimerImageFragment extends Fragment {

    final TimerInteraction interaction;
    Stack root;
    public Table backgroundTable;
    public FitImage background;
    TimerBombFragment bombFragment;

    public TimerImageFragment(TimerInteraction interaction) {
        this.interaction = interaction;
    }

    public void create() {
//        Texture cloud1Texture = interaction.assetManager.get("LoadingScreen/lv_cloud_1.png", Texture.class);

        background = new FitImage();

        backgroundTable = new Table();
        backgroundTable.setFillParent(true);
        backgroundTable.add(background).center().expand().fill();

        root = new Stack();
        root.addActor(backgroundTable);
    }

    public void startTimer() {

        if (bombFragment == null) {

            bombFragment = new TimerBombFragment(interaction);
            bombFragment.create();

            root.addActor(bombFragment.getRoot());
        }
    }

    public void update() {
        if (bombFragment != null) {
            bombFragment.update();
        }
    }

    public void setBackground(Texture texture) {
        setBackground(new SpriteDrawable(new Sprite(texture)));
    }

    public void setBackground(SpriteDrawable texture) {

        interaction.gameScreen.hideImageFragment();

        backgroundTable.setVisible(true);

        background.setDrawable(texture);

        float scale = 1f * MunhauzenGame.WORLD_WIDTH / background.getDrawable().getMinWidth();
        float height = 1f * background.getDrawable().getMinHeight() * scale;

        backgroundTable.getCell(background)
                .width(MunhauzenGame.WORLD_WIDTH)
                .height(height);
    }

    @Override
    public Actor getRoot() {
        return root;
    }

    @Override
    public void dispose() {
        super.dispose();

        if (bombFragment != null) {
            bombFragment.destroy();
            bombFragment = null;
        }
    }
}
