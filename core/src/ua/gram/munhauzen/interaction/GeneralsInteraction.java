package ua.gram.munhauzen.interaction;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;

import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.fragment.Fragment;
import ua.gram.munhauzen.interaction.generals.GeneralsScenario;
import ua.gram.munhauzen.interaction.generals.GeneralsStory;
import ua.gram.munhauzen.interaction.generals.GeneralsStoryManager;
import ua.gram.munhauzen.interaction.generals.animation.FireLeftAnimation;
import ua.gram.munhauzen.interaction.generals.animation.FireRightAnimation;
import ua.gram.munhauzen.interaction.generals.animation.FumesAnimation;
import ua.gram.munhauzen.interaction.generals.fragment.GeneralsProgressBarFragment;
import ua.gram.munhauzen.interaction.generals.fragment.GeneralsScenarioFragment;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.service.DatabaseManager;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class GeneralsInteraction extends AbstractInteraction {

    public Array<GeneralsScenario> scenarioRegistry;
    public GeneralsStoryManager storyManager;
    public GeneralsProgressBarFragment progressBarFragment;
    public GeneralsScenarioFragment scenarioFragment;
    boolean isLoaded;
    FumesAnimation fumes;
    FireLeftAnimation fireLeft;
    FireRightAnimation fireRight;

    public GeneralsInteraction(GameScreen gameScreen) {
        super(gameScreen);
    }

    @Override
    public void start() {
        super.start();

        scenarioRegistry = new DatabaseManager().loadGeneralsScenario();
        storyManager = new GeneralsStoryManager(gameScreen, this);
        progressBarFragment = new GeneralsProgressBarFragment(gameScreen, this);

        assetManager.load("generals/fumes_sheet.png", Texture.class);
        assetManager.load("generals/fire_left_sheet.png", Texture.class);
        assetManager.load("generals/fire_right_sheet.png", Texture.class);
    }

    public void onResourcesLoaded() {
        isLoaded = true;

        progressBarFragment.create();

        gameScreen.gameLayers.setProgressBarLayer(progressBarFragment);

        storyManager.resume();

        Texture fumesTexture = assetManager.get("generals/fumes_sheet.png", Texture.class);
        Texture fireLeftTexture = assetManager.get("generals/fire_left_sheet.png", Texture.class);
        Texture fireRightTexture = assetManager.get("generals/fire_right_sheet.png", Texture.class);

        fumes = new FumesAnimation(fumesTexture);
        fireLeft = new FireLeftAnimation(fireLeftTexture);
        fireRight = new FireRightAnimation(fireRightTexture);

        fumes.setVisible(false);
        fireLeft.setVisible(false);
        fireRight.setVisible(false);

        Group stack = new Group();
        stack.addActor(fumes);
        stack.addActor(fireLeft);
        stack.addActor(fireRight);

        gameScreen.gameLayers.setInteractionLayer(
                new Fragment(stack)
        );
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

        GeneralsStory story = storyManager.generalsStory;

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

        if (story.currentScenario.currentImage != null) {
            switch (story.currentScenario.currentImage.image) {
                case "inter_general_1":

                    fireLeft.setVisible(false);
                    fireRight.setVisible(false);

                    if (!fumes.isVisible()) {
                        fumes.setVisible(true);

                        fumes.start();
                    }
                    break;
                case "inter_general_2":

                    fumes.setVisible(false);

                    if (!fireLeft.isVisible()) {
                        fireLeft.setVisible(true);

                        fireLeft.start();
                    }

                    if (!fireRight.isVisible()) {
                        fireRight.setVisible(true);

                        fireRight.start();
                    }
                    break;
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

        fireRight = null;
        fireLeft = null;
        fumes = null;

        isLoaded = false;
    }
}
