package ua.gram.munhauzen.interaction;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Array;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.fragment.Fragment;
import ua.gram.munhauzen.interaction.hare.HareScenario;
import ua.gram.munhauzen.interaction.hare.HareStory;
import ua.gram.munhauzen.interaction.hare.HareStoryManager;
import ua.gram.munhauzen.interaction.hare.animation.DucksAnimation;
import ua.gram.munhauzen.interaction.hare.animation.HareAnimation;
import ua.gram.munhauzen.interaction.hare.animation.HorseAnimation;
import ua.gram.munhauzen.interaction.hare.fragment.HareProgressBarFragment;
import ua.gram.munhauzen.interaction.hare.fragment.HareScenarioFragment;
import ua.gram.munhauzen.interaction.hare.ui.Cloud;
import ua.gram.munhauzen.interaction.hare.ui.Ground;
import ua.gram.munhauzen.interaction.hare.ui.Misc;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.service.DatabaseManager;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class HareInteraction extends AbstractInteraction {

    public Array<HareScenario> scenarioRegistry;
    public HareStoryManager storyManager;
    public HareProgressBarFragment progressBarFragment;
    public HareScenarioFragment scenarioFragment;
    boolean isLoaded;

    public HareInteraction(GameScreen gameScreen) {
        super(gameScreen);
    }

    @Override
    public void start() {
        super.start();

        scenarioRegistry = new DatabaseManager().loadHareScenario();
        storyManager = new HareStoryManager(gameScreen, this);
        progressBarFragment = new HareProgressBarFragment(gameScreen, this);

        assetManager.load("LoadingScreen/lv_cloud_1.png", Texture.class);
        assetManager.load("LoadingScreen/lv_cloud_2.png", Texture.class);
        assetManager.load("LoadingScreen/lv_cloud_3.png", Texture.class);
        assetManager.load("hare/ducks_sheet_1x5.png", Texture.class);
        assetManager.load("hare/hare_sheet_4x1.png", Texture.class);
        assetManager.load("hare/horse_sheet_5x1.png", Texture.class);
        assetManager.load("hare/inter_hare_ground.png", Texture.class);
        assetManager.load("hare/inter_hare_misc_1.png", Texture.class);
        assetManager.load("hare/inter_hare_misc_2.png", Texture.class);
        assetManager.load("hare/inter_hare_misc_3.png", Texture.class);
        assetManager.load("hare/inter_hare_misc_4.png", Texture.class);
        assetManager.load("hare/inter_hare_misc_5.png", Texture.class);
        assetManager.load("hare/inter_hare_misc_6.png", Texture.class);
    }

    public void onResourcesLoaded() {
        isLoaded = true;

        if (gameScreen.progressBarFragment != null) {
            gameScreen.progressBarFragment.destroy();
            gameScreen.progressBarFragment = null;
        }

        progressBarFragment.create();

        gameScreen.gameLayers.setProgressBarLayer(progressBarFragment);

        storyManager.resume();

        Texture cloud1Texture = assetManager.get("LoadingScreen/lv_cloud_1.png", Texture.class);
        Texture cloud2Texture = assetManager.get("LoadingScreen/lv_cloud_2.png", Texture.class);
        Texture cloud3Texture = assetManager.get("LoadingScreen/lv_cloud_3.png", Texture.class);
        Texture ducksTexture = assetManager.get("hare/ducks_sheet_1x5.png", Texture.class);
        Texture groundTexture = assetManager.get("hare/inter_hare_ground.png", Texture.class);
        Texture hareTexture = assetManager.get("hare/hare_sheet_4x1.png", Texture.class);
        Texture horseTexture = assetManager.get("hare/horse_sheet_5x1.png", Texture.class);
        Texture miscTexture1 = assetManager.get("hare/inter_hare_misc_1.png", Texture.class);
        Texture miscTexture2 = assetManager.get("hare/inter_hare_misc_2.png", Texture.class);
        Texture miscTexture3 = assetManager.get("hare/inter_hare_misc_3.png", Texture.class);
        Texture miscTexture4 = assetManager.get("hare/inter_hare_misc_4.png", Texture.class);
        Texture miscTexture5 = assetManager.get("hare/inter_hare_misc_5.png", Texture.class);
        Texture miscTexture6 = assetManager.get("hare/inter_hare_misc_6.png", Texture.class);

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

        Group stack = new Group();
        stack.addActor(ground);
        stack.addActor(misc1);
        stack.addActor(misc2);
        stack.addActor(misc3);
        stack.addActor(misc4);
        stack.addActor(misc5);
        stack.addActor(misc6);
        stack.addActor(hare);
        stack.addActor(horse);
        stack.addActor(cloud1);
        stack.addActor(cloud2);
        stack.addActor(cloud3);
        stack.addActor(ducks);

        gameScreen.gameLayers.setInteractionLayer(
                new Fragment(stack)
        );

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
    }

    @Override
    public void update() {
        super.update();

        assetManager.update();

        if (!isLoaded) {
            if (assetManager.isFinished()) {
                onResourcesLoaded();
            }
            return;
        }

        HareStory story = storyManager.hareStory;

        if (!story.isCompleted) {

            if (!GameState.isPaused) {
                storyManager.update(
                        story.progress + (Gdx.graphics.getDeltaTime() * 1000),
                        story.totalDuration
                );

                if (story.isCompleted) {

                    storyManager.onCompleted();

                } else {
                    storyManager.startLoadingResources();
                }
            }
        }

        if (progressBarFragment != null)
            progressBarFragment.update();

    }

    @Override
    public void dispose() {
        super.dispose();

        if (progressBarFragment != null) {
            progressBarFragment.dispose();
            progressBarFragment = null;
        }

        if (scenarioFragment != null) {
            scenarioFragment.dispose();
            scenarioFragment = null;
        }

        isLoaded = false;
    }
}
