package ua.gram.munhauzen.interaction.balloons.fragment;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;

import ua.gram.munhauzen.FontProvider;
import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.fragment.Fragment;
import ua.gram.munhauzen.interaction.BalloonsInteraction;
import ua.gram.munhauzen.interaction.hare.animation.DucksAnimation;
import ua.gram.munhauzen.interaction.hare.ui.Cloud;
import ua.gram.munhauzen.ui.FitImage;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class BalloonsImageFragment extends Fragment {

    private final BalloonsInteraction interaction;
    public Group root;
    public Image background;
    public Table backgroundTable;
    Label progress;

    public BalloonsImageFragment(BalloonsInteraction interaction) {
        this.interaction = interaction;
    }

    public void create() {

        Log.i(tag, "create");


        Texture backTex = interaction.assetManager.get("balloons/inter_balloons_fond.jpg", Texture.class);
        Texture cloud1Texture = interaction.assetManager.get("LoadingScreen/lv_cloud_1.png", Texture.class);
        Texture cloud2Texture = interaction.assetManager.get("LoadingScreen/lv_cloud_2.png", Texture.class);
        Texture cloud3Texture = interaction.assetManager.get("LoadingScreen/lv_cloud_3.png", Texture.class);
        Texture ducksTexture = interaction.assetManager.get("balloons/ducks_sheet_1x5.png", Texture.class);
        Texture bal1Texture = interaction.assetManager.get("balloons/inter_balloons_1.png", Texture.class);
        Texture bal2Texture = interaction.assetManager.get("balloons/inter_balloons_2.png", Texture.class);
        Texture bal3Texture = interaction.assetManager.get("balloons/inter_balloons_3.png", Texture.class);
        Texture bal4Texture = interaction.assetManager.get("balloons/inter_balloons_4.png", Texture.class);

        DucksAnimation ducks = new DucksAnimation(ducksTexture);

        Cloud cloud1 = new Cloud(cloud1Texture, 200, 100, -100, MunhauzenGame.WORLD_HEIGHT * .9f);
        Cloud cloud2 = new Cloud(cloud2Texture, 200, 100, -200, MunhauzenGame.WORLD_HEIGHT * .8f);
        Cloud cloud3 = new Cloud(cloud3Texture, 200, 100, -180, MunhauzenGame.WORLD_HEIGHT * .75f);

        Label title = new Label("Catch fruits!", new Label.LabelStyle(
                interaction.gameScreen.game.fontProvider.getFont(FontProvider.FleischmannGotich, FontProvider.h1),
                Color.BLACK
        ));
        title.setWrap(true);
        title.setAlignment(Align.center);

        progress = new Label("", new Label.LabelStyle(
                interaction.gameScreen.game.fontProvider.getFont(FontProvider.FleischmannGotich, FontProvider.h1),
                Color.BLACK
        ));
        progress.setWrap(false);
        progress.setAlignment(Align.right);

        Group stack = new Group();
        stack.addActor(cloud1);
        stack.addActor(cloud2);
        stack.addActor(cloud3);
        stack.addActor(ducks);

        ducks.start();
        cloud1.start();
        cloud2.start();
        cloud3.start();

        background = new FitImage(backTex);

        backgroundTable = new Table();
        backgroundTable.setFillParent(true);
        backgroundTable.add(background).center().expand().fill();

        Table titleTable = new Table();
        titleTable.setFillParent(true);
        titleTable.pad(10);
        titleTable.add(title).top().expand().row();
        titleTable.add(progress).align(Align.bottomRight).expand().row();

        displayImage(backTex);

        root = new Group();
        root.addActor(backgroundTable);
        root.addActor(titleTable);

        root.setName(tag);
    }

    @Override
    public Actor getRoot() {
        return root;
    }

    public void displayImage(Texture texture) {

        interaction.gameScreen.imageFragment.layer1ImageGroup.setVisible(false);
        interaction.gameScreen.imageFragment.layer2ImageGroup.setVisible(false);

        background.setDrawable(new SpriteDrawable(new Sprite(texture)));

        float scale = 1f * MunhauzenGame.WORLD_WIDTH / background.getDrawable().getMinWidth();
        float height = 1f * background.getDrawable().getMinHeight() * scale;

        backgroundTable.getCell(background)
                .width(MunhauzenGame.WORLD_WIDTH)
                .height(height);
    }

    public void update() {

        progress.setText("0/21");

    }
}
