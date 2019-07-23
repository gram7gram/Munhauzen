package ua.gram.munhauzen.interaction.generals.fragment;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

import ua.gram.munhauzen.fragment.Fragment;
import ua.gram.munhauzen.interaction.GeneralsInteraction;
import ua.gram.munhauzen.interaction.generals.GeneralsStory;
import ua.gram.munhauzen.interaction.generals.GeneralsStoryImage;
import ua.gram.munhauzen.interaction.generals.animation.FireLeftAnimation;
import ua.gram.munhauzen.interaction.generals.animation.FireRightAnimation;
import ua.gram.munhauzen.interaction.generals.animation.FumesAnimation;
import ua.gram.munhauzen.ui.BackgroundImage;
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
    public BackgroundImage backgroundImage;

    public GeneralsImageFragment(GeneralsInteraction interaction) {
        this.interaction = interaction;
    }

    public void create() {

        Log.i(tag, "create");

        backgroundImage = new BackgroundImage(interaction.gameScreen);

        Texture fumesTexture = interaction.assetManager.get("generals/an_general_1_sheet_3x1.png", Texture.class);
        Texture fireLeftTexture = interaction.assetManager.get("generals/an_general_2_sheet_3x1.png", Texture.class);
        Texture fireRightTexture = interaction.assetManager.get("generals/an_general_3_sheet_3x1.png", Texture.class);

        fumes = new FumesAnimation(fumesTexture, backgroundImage.background);
        fireLeft = new FireLeftAnimation(fireLeftTexture, backgroundImage.background);
        fireRight = new FireRightAnimation(fireRightTexture, backgroundImage.background);

        fumes.setVisible(false);
        fireLeft.setVisible(false);
        fireRight.setVisible(false);

        items = new Group();

        root = new Group();
        root.addActor(backgroundImage);
        root.setName(tag);
    }

    @Override
    public Actor getRoot() {
        return root;
    }

    public void update() {

        GeneralsStory story = interaction.storyManager.generalsStory;

        if (story.currentScenario != null) {

            GeneralsStoryImage image = story.currentScenario.currentImage;
            if (image != null && image.isActive) {

                if (image.withFumes) {

                    if (story.currentScenario.currentImage.width > 0) {

                        if (!fumes.isVisible()) {

                            Log.i(tag, "show fumes");

                            fumes.init(story.currentScenario.currentImage);

                            fumes.start();

                            root.addActor(fumes);
                        }
                    }
                } else {
                    fumes.setVisible(false);
                }

                if (image.withFireLeft) {

                    if (story.currentScenario.currentImage.width > 0) {

                        if (!fireLeft.isVisible()) {

                            Log.i(tag, "show fire left");

                            fireLeft.init(story.currentScenario.currentImage);

                            fireLeft.start();

                            root.addActor(fireLeft);
                        }
                    }
                } else {
                    fireLeft.setVisible(false);
                }

                if (image.withFireRight) {

                    if (story.currentScenario.currentImage.width > 0) {

                        if (!fireRight.isVisible()) {

                            Log.i(tag, "show fire right");

                            fireRight.init(story.currentScenario.currentImage);

                            fireRight.start();

                            root.addActor(fireRight);
                        }
                    }
                } else {
                    fireRight.setVisible(false);
                }


            }
        }

    }
}
