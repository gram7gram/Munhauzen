package ua.gram.munhauzen.interaction.hare.fragment;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.interaction.HareInteraction;
import ua.gram.munhauzen.interaction.hare.HareStory;
import ua.gram.munhauzen.interaction.hare.animation.DucksAnimation;
import ua.gram.munhauzen.interaction.hare.animation.HareAnimation;
import ua.gram.munhauzen.interaction.hare.animation.HorseAnimation;
import ua.gram.munhauzen.interaction.hare.ui.Butterflies;
import ua.gram.munhauzen.interaction.hare.ui.Cloud;
import ua.gram.munhauzen.interaction.hare.ui.Ground;
import ua.gram.munhauzen.screen.game.fragment.InteractionFragment;
import ua.gram.munhauzen.screen.game.ui.BackgroundImage;
import ua.gram.munhauzen.ui.FragmentRoot;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class HareImageFragment extends InteractionFragment {

    final HareInteraction interaction;
    FragmentRoot root;
    Group items;
    public BackgroundImage backgroundImage;

    public HareImageFragment(HareInteraction interaction) {
        this.interaction = interaction;
    }

    public void create() {

        Log.i(tag, "create");

        backgroundImage = new BackgroundImage(interaction.gameScreen);

        Texture cloud1Texture = interaction.assetManager.get("hare/lv_cloud_1.png", Texture.class);
        Texture cloud2Texture = interaction.assetManager.get("hare/lv_cloud_2.png", Texture.class);
        Texture cloud3Texture = interaction.assetManager.get("hare/lv_cloud_3.png", Texture.class);
        Texture ducksTexture = interaction.assetManager.get("hare/ducks_sheet_1x6.png", Texture.class);
        Texture groundTexture = interaction.assetManager.get("hare/inter_hare_ground.png", Texture.class);
        Texture hareTexture = interaction.assetManager.get("hare/hare_sheet.png", Texture.class);
        Texture horseTexture = interaction.assetManager.get("hare/horse_sheet.png", Texture.class);
        Texture miscTexture1 = interaction.assetManager.get("hare/inter_hare_butterflies.png", Texture.class);

        Ground ground = new Ground(groundTexture);
        DucksAnimation ducks = new DucksAnimation(ducksTexture);
        HareAnimation hare = new HareAnimation(hareTexture, ground);
        HorseAnimation horse = new HorseAnimation(horseTexture, ground);

        Butterflies misc1 = new Butterflies(miscTexture1);

        Cloud cloud1 = new Cloud(cloud1Texture, -100, MunhauzenGame.WORLD_HEIGHT * .9f);
        Cloud cloud2 = new Cloud(cloud2Texture, -200, MunhauzenGame.WORLD_HEIGHT * .8f);
        Cloud cloud3 = new Cloud(cloud3Texture, -180, MunhauzenGame.WORLD_HEIGHT * .75f);

        misc1.addAction(Actions.rotateBy(30));

        backgroundImage.setVisible(false);

        items = new Group();
        items.addActor(ground);
        items.addActor(hare);
        items.addActor(horse);
        items.addActor(cloud1);
        items.addActor(cloud2);
        items.addActor(cloud3);
        items.addActor(ducks);
        items.addActor(misc1);

        hare.start();
        horse.start();
        misc1.start();
        ground.start();
        ducks.start();
        cloud1.start();
        cloud2.start();
        cloud3.start();

        root = new FragmentRoot();
        root.addContainer(backgroundImage);
        root.addContainer(items);
    }

    public void update() {

        if (interaction.storyManager == null) return;

        HareStory story = interaction.storyManager.story;

        if (story != null && story.currentScenario != null) {
            if ("a18_d".equals(story.currentScenario.scenario.name)) {

                if (story.progress > 30000) {

                    if (!backgroundImage.isVisible()) {
                        setBackground(
                                interaction.assetManager.get("images/p18_d.jpg", Texture.class),
                                "images/p18_d.jpg"
                        );
                    }

                } else {

                    items.setVisible(true);
                    backgroundImage.setVisible(false);
                }

            }
        }

    }

    public void setBackground(Texture texture, String file) {

        interaction.gameScreen.hideImageFragment();

        items.setVisible(false);
        backgroundImage.setVisible(true);

        backgroundImage.setBackgroundDrawable(new SpriteDrawable(new Sprite(texture)));

        interaction.gameScreen.setLastBackground(file);
    }

    @Override
    public Actor getRoot() {
        return root;
    }
}
