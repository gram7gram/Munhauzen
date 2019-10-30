package ua.gram.munhauzen.interaction.generals.fragment;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Container;

import ua.gram.munhauzen.interaction.GeneralsInteraction;
import ua.gram.munhauzen.interaction.generals.GeneralsStory;
import ua.gram.munhauzen.interaction.generals.GeneralsStoryImage;
import ua.gram.munhauzen.interaction.generals.animation.FireLeftAnimation;
import ua.gram.munhauzen.interaction.generals.animation.FireRightAnimation;
import ua.gram.munhauzen.interaction.generals.animation.FumesAnimation;
import ua.gram.munhauzen.screen.game.fragment.InteractionFragment;
import ua.gram.munhauzen.screen.game.ui.BackgroundImage;
import ua.gram.munhauzen.ui.FragmentRoot;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class GeneralsImageFragment extends InteractionFragment {

    private final GeneralsInteraction interaction;
    FumesAnimation fumes;
    FireLeftAnimation fireLeft;
    FireRightAnimation fireRight;
    public FragmentRoot root;
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

        fumes = new FumesAnimation(fumesTexture, backgroundImage);
        fireLeft = new FireLeftAnimation(fireLeftTexture, backgroundImage);
        fireRight = new FireRightAnimation(fireRightTexture, backgroundImage);

        fumes.setVisible(false);
        fireLeft.setVisible(false);
        fireRight.setVisible(false);

        root = new FragmentRoot();
        root.addContainer(backgroundImage);
        root.setName(tag);
    }

    @Override
    public Actor getRoot() {
        return root;
    }

    public void update() {

        if (interaction.storyManager == null) return;

        GeneralsStory story = interaction.storyManager.story;

        if (story != null && story.currentScenario != null) {

            GeneralsStoryImage image = story.currentScenario.currentImage;
            if (image != null && image.isActive) {

                if (image.withFumes) {

                    if (backgroundImage.backgroundWidth > 0) {

                        if (!fumes.isVisible()) {

                            Log.i(tag, "show fumes");

                            fumes.start();

                            root.addContainer(new Container<>(fumes));
                        }
                    }
                } else {
                    fumes.setVisible(false);
                }

                if (image.withFireLeft) {

                    if (backgroundImage.backgroundWidth > 0) {

                        if (!fireLeft.isVisible()) {

                            Log.i(tag, "show fire left");

                            fireLeft.start();

                            root.addContainer(new Container<>(fireLeft));
                        }
                    }
                } else {
                    fireLeft.setVisible(false);
                }

                if (image.withFireRight) {

                    if (backgroundImage.backgroundWidth > 0) {

                        if (!fireRight.isVisible()) {

                            Log.i(tag, "show fire right");

                            fireRight.start();

                            root.addContainer(new Container<>(fireRight));
                        }
                    }
                } else {
                    fireRight.setVisible(false);
                }


            }
        }

    }
}
