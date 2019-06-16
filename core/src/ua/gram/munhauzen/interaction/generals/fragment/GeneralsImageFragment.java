package ua.gram.munhauzen.interaction.generals.fragment;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;

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
    public Group root;
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

        root = new Group();
        root.addActor(backgroundTable);

        root.addListener(new ActorGestureListener() {

            @Override
            public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
                super.pan(event, x, y, deltaX, deltaY);

                Log.i(tag, "pan");

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

        root.addAction(
                Actions.forever(
                        Actions.sequence(
                                Actions.moveBy(10, 0, 1.2f),
                                Actions.moveBy(-10, 0, 1.4f)
                        )
                )
        );

        root.setName(tag);
    }

    @Override
    public Actor getRoot() {
        return root;
    }

    public void update() {

        GeneralsStory story = interaction.storyManager.generalsStory;

        GeneralsStoryImage image = story.currentScenario.currentImage;
        if (image != null && image.isActive) {
            if (image.internalFile != null) {
                switch (image.internalFile) {
                    case "generals/inter_general_1.jpg":

                        fireLeft.setVisible(false);
                        fireRight.setVisible(false);

                        if (story.currentScenario.currentImage.width > 0) {

                            if (!fumes.isVisible()) {

                                Log.i(tag, "show fumes");

                                fumes.init(story.currentScenario.currentImage);

                                fumes.start();

                                root.addActor(fumes);
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

                                root.addActor(fireLeft);
                            }

                            if (!fireRight.isVisible()) {
                                fireRight.init(story.currentScenario.currentImage);

                                fireRight.start();

                                root.addActor(fireRight);
                            }
                        }
                        break;
                }
            }
        }
    }

    @Override
    public void dispose() {
        super.dispose();

        fireRight = null;
        fireLeft = null;
        fumes = null;

    }
}
