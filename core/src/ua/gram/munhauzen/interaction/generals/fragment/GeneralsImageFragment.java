package ua.gram.munhauzen.interaction.generals.fragment;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.fragment.Fragment;
import ua.gram.munhauzen.interaction.GeneralsInteraction;
import ua.gram.munhauzen.interaction.generals.GeneralsStory;
import ua.gram.munhauzen.interaction.generals.GeneralsStoryImage;
import ua.gram.munhauzen.interaction.generals.animation.FireLeftAnimation;
import ua.gram.munhauzen.interaction.generals.animation.FireRightAnimation;
import ua.gram.munhauzen.interaction.generals.animation.FumesAnimation;
import ua.gram.munhauzen.ui.FitImage;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class GeneralsImageFragment extends Fragment {

    private final GeneralsInteraction interaction;
    FumesAnimation fumes;
    FireLeftAnimation fireLeft;
    FireRightAnimation fireRight;
    public Group root, items;
    public Image background;
    public Table backgroundTable;

    public GeneralsImageFragment(GeneralsInteraction interaction) {
        this.interaction = interaction;
    }

    public void create() {

        Log.i(tag, "create");

        Texture fumesTexture = interaction.assetManager.get("generals/an_general_1_sheet_3x1.png", Texture.class);
        Texture fireLeftTexture = interaction.assetManager.get("generals/an_general_2_sheet_3x1.png", Texture.class);
        Texture fireRightTexture = interaction.assetManager.get("generals/an_general_3_sheet_3x1.png", Texture.class);

        background = new FitImage();

        fumes = new FumesAnimation(fumesTexture, background);
        fireLeft = new FireLeftAnimation(fireLeftTexture, background);
        fireRight = new FireRightAnimation(fireRightTexture, background);

        backgroundTable = new Table();
        backgroundTable.setFillParent(true);
        backgroundTable.add(background).right().expand().fill();

        fumes.setVisible(false);
        fireLeft.setVisible(false);
        fireRight.setVisible(false);

        items = new Group();

        root = new Group();
        root.addActor(backgroundTable);

        root.addListener(new ActorGestureListener() {

            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                super.tap(event, x, y, count, button);

                try {
                    if (!interaction.progressBarFragment.getRoot().isVisible()) {
                        if (!interaction.progressBarFragment.isFadeIn) {
                            interaction.progressBarFragment.fadeIn();
                            interaction.progressBarFragment.scheduleFadeOut();
                        }
                    } else {
                        if (!interaction.progressBarFragment.isFadeOut) {
                            interaction.progressBarFragment.fadeOut();
                        }
                    }
                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }

            @Override
            public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
                super.pan(event, x, y, deltaX, deltaY);

                try {

                    root.clearActions();

                    GeneralsStory story = interaction.storyManager.generalsStory;

                    float newX = background.getX() + deltaX;
                    float currentWidth = story.currentScenario.currentImage.width;

                    float leftBound = -currentWidth + MunhauzenGame.WORLD_WIDTH;
                    float rightBound = 0;

                    if (leftBound < newX && newX < rightBound) {
                        background.setX(background.getX() + deltaX);
                    }

                    if (background.getX() > 0) background.setX(0);
                    if (background.getX() < leftBound) background.setX(leftBound);

                } catch (Throwable e) {
                    Log.e(tag, e);
                }

            }
        });

        root.setName(tag);
    }

    @Override
    public Actor getRoot() {
        return root;
    }

    public void update() {

        GeneralsStory story = interaction.storyManager.generalsStory;

        if (story.currentScenario != null) {
            if ("a2_2_0_interaction".equals(story.currentScenario.scenario.name)) {

                if (story.progress < 76000) {

                    if (!backgroundTable.isVisible()) {

                        Texture back = interaction.assetManager.get("generals/p33_d.jpg", Texture.class);

                        setBackground(back);
                    }

                } else {

                    items.setVisible(true);
                    backgroundTable.setVisible(false);
                }

            }


            GeneralsStoryImage image = story.currentScenario.currentImage;
            if (image != null && image.isActive) {
                if (image.internalFile != null) {
                    switch (image.internalFile) {
                        case "generals/inter_general_1.jpg":

                            boolean canDrawFumes = true;

                            if ("a2_2_0_interaction".equals(story.currentScenario.scenario.name)) {

                            }

                            fireLeft.setVisible(false);
                            fireRight.setVisible(false);

                            if (canDrawFumes) {

                                if (story.currentScenario.currentImage.width > 0) {

                                    if (!fumes.isVisible()) {

                                        Log.i(tag, "show fumes");

                                        fumes.init(story.currentScenario.currentImage);

                                        fumes.start();

                                        items.addActor(fumes);
                                    }
                                }
                            }
                            break;
                        case "generals/inter_general_2.jpg":

                            fumes.setVisible(false);

                            if (!fireLeft.isVisible() || !fireRight.isVisible()) {
                                Log.i(tag, "show fire");

                                if (!fireLeft.isVisible()) {
                                    fireLeft.init(story.currentScenario.currentImage);

                                    fireLeft.start();

                                    items.addActor(fireLeft);
                                }

                                if (!fireRight.isVisible()) {
                                    fireRight.init(story.currentScenario.currentImage);

                                    fireRight.start();

                                    items.addActor(fireRight);
                                }
                            }
                            break;
                    }
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
}
