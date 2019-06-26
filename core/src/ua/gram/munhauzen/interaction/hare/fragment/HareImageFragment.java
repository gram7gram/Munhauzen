package ua.gram.munhauzen.interaction.hare.fragment;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.fragment.Fragment;
import ua.gram.munhauzen.interaction.HareInteraction;
import ua.gram.munhauzen.interaction.hare.HareStory;
import ua.gram.munhauzen.interaction.hare.animation.DucksAnimation;
import ua.gram.munhauzen.interaction.hare.animation.HareAnimation;
import ua.gram.munhauzen.interaction.hare.animation.HorseAnimation;
import ua.gram.munhauzen.interaction.hare.ui.Cloud;
import ua.gram.munhauzen.interaction.hare.ui.Ground;
import ua.gram.munhauzen.interaction.hare.ui.Misc;
import ua.gram.munhauzen.ui.FitImage;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class HareImageFragment extends Fragment {

    final HareInteraction interaction;
    Group root, items;
    Table backgroundTable;
    FitImage background;

    public HareImageFragment(HareInteraction interaction) {
        this.interaction = interaction;
    }

    public void create() {
        Texture cloud1Texture = interaction.assetManager.get("LoadingScreen/lv_cloud_1.png", Texture.class);
        Texture cloud2Texture = interaction.assetManager.get("LoadingScreen/lv_cloud_2.png", Texture.class);
        Texture cloud3Texture = interaction.assetManager.get("LoadingScreen/lv_cloud_3.png", Texture.class);
        Texture ducksTexture = interaction.assetManager.get("hare/ducks_sheet_1x5.png", Texture.class);
        Texture groundTexture = interaction.assetManager.get("hare/inter_hare_ground.png", Texture.class);
        Texture hareTexture = interaction.assetManager.get("hare/hare_sheet_4x1.png", Texture.class);
        Texture horseTexture = interaction.assetManager.get("hare/horse_sheet_5x1.png", Texture.class);
        Texture miscTexture1 = interaction.assetManager.get("hare/inter_hare_misc_1.png", Texture.class);
        Texture miscTexture2 = interaction.assetManager.get("hare/inter_hare_misc_2.png", Texture.class);
        Texture miscTexture3 = interaction.assetManager.get("hare/inter_hare_misc_3.png", Texture.class);
        Texture miscTexture4 = interaction.assetManager.get("hare/inter_hare_misc_4.png", Texture.class);
        Texture miscTexture5 = interaction.assetManager.get("hare/inter_hare_misc_5.png", Texture.class);
        Texture miscTexture6 = interaction.assetManager.get("hare/inter_hare_misc_6.png", Texture.class);

        Ground ground = new Ground(groundTexture);
        DucksAnimation ducks = new DucksAnimation(ducksTexture);
        HareAnimation hare = new HareAnimation(hareTexture);
        HorseAnimation horse = new HorseAnimation(horseTexture);

        float miscX = ground.originPoint.getX() - ground.image.getWidth() / 2f;
        float miscY = ground.originPoint.getY();

        Misc misc1 = new Misc(miscTexture1, ground, 100, 100, miscX, miscY);
        Misc misc2 = new Misc(miscTexture2, ground, 100, 100, miscX, miscY);
        Misc misc3 = new Misc(miscTexture3, ground, 100, 100, miscX, miscY);
        Misc misc4 = new Misc(miscTexture4, ground, 100, 100, miscX, miscY);
        Misc misc5 = new Misc(miscTexture5, ground, 100, 100, miscX, miscY);
        Misc misc6 = new Misc(miscTexture6, ground, 100, 100, miscX, miscY);

        Cloud cloud1 = new Cloud(cloud1Texture, 200, 100, -100, MunhauzenGame.WORLD_HEIGHT * .9f);
        Cloud cloud2 = new Cloud(cloud2Texture, 200, 100, -200, MunhauzenGame.WORLD_HEIGHT * .8f);
        Cloud cloud3 = new Cloud(cloud3Texture, 200, 100, -180, MunhauzenGame.WORLD_HEIGHT * .75f);

        misc1.addAction(Actions.rotateBy(30));
        misc2.addAction(Actions.rotateBy(-30));
        misc3.addAction(Actions.rotateBy(-150));
        misc4.addAction(Actions.rotateBy(-180));
        misc5.addAction(Actions.rotateBy(-190));
        misc6.addAction(Actions.rotateBy(-250));

        background = new FitImage();

        backgroundTable = new Table();
        backgroundTable.setFillParent(true);
        backgroundTable.add(background).center().expand().fill();

        backgroundTable.setVisible(false);

        items = new Group();
        items.addActor(ground);
        items.addActor(misc1);
        items.addActor(misc2);
        items.addActor(misc3);
        items.addActor(misc4);
        items.addActor(misc5);
        items.addActor(misc6);
        items.addActor(hare);
        items.addActor(horse);
        items.addActor(cloud1);
        items.addActor(cloud2);
        items.addActor(cloud3);
        items.addActor(ducks);

        hare.start();
        horse.start();
        misc1.start();
        misc2.start();
        misc3.start();
        misc4.start();
        misc5.start();
        misc6.start();
        ground.start();
        ducks.start();
        cloud1.start();
        cloud2.start();
        cloud3.start();

        root = new Group();
        root.addActor(items);
        root.addActor(backgroundTable);
    }

    public void update() {

        HareStory story = interaction.storyManager.hareStory;
        if (story.currentScenario != null) {
            if ("a18_d".equals(story.currentScenario.scenario.name)) {

                if (story.progress > 30000) {

                    if (!backgroundTable.isVisible()) {

                        Texture back = interaction.assetManager.get("hare/p18_d.jpg", Texture.class);

                        setBackground(back);
                    }

                } else {

                    items.setVisible(true);
                    backgroundTable.setVisible(false);
                }

            }
        }

    }

    public void setBackground(Texture texture) {

        interaction.gameScreen.imageFragment.layer1ImageGroup.setVisible(false);
        interaction.gameScreen.imageFragment.layer2ImageGroup.setVisible(false);

        items.setVisible(false);
        backgroundTable.setVisible(true);

        background.setDrawable(new SpriteDrawable(new Sprite(texture)));

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

    }
}
