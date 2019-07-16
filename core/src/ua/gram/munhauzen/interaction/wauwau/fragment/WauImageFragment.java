package ua.gram.munhauzen.interaction.wauwau.fragment;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.fragment.Fragment;
import ua.gram.munhauzen.interaction.WauInteraction;
import ua.gram.munhauzen.interaction.wauwau.WauStory;
import ua.gram.munhauzen.interaction.wauwau.WauStoryImage;
import ua.gram.munhauzen.interaction.wauwau.animation.WauAnimation;
import ua.gram.munhauzen.ui.FitImage;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class WauImageFragment extends Fragment {

    private final WauInteraction interaction;
    WauAnimation wauAnimation;
    public Group root, items;
    public Image background;
    public Table backgroundTable;
    public float backgroundWidth, backgroundHeight;

    public WauImageFragment(WauInteraction interaction) {
        this.interaction = interaction;
    }

    public void create() {

        Log.i(tag, "create");

        background = new FitImage();

        wauAnimation = new WauAnimation(
                interaction.assetManager.get("wau/wau_sheet_1x4.png", Texture.class),
                this
        );

        backgroundTable = new Table();
        backgroundTable.setFillParent(true);
        backgroundTable.add(background).right().expand().fill();

        items = new Group();

        root = new Group();
        root.addActor(backgroundTable);

        root.addListener(new ActorGestureListener() {

            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                super.tap(event, x, y, count, button);

                try {
                    interaction.gameScreen.stageInputListener.clicked(event, x, y);
                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }

            @Override
            public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
                super.pan(event, x, y, deltaX, deltaY);

                moveBackground(deltaX);

                if (wauAnimation.isVisible()) {
                    moveWau(deltaX);
                }

            }

            @Override
            public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
                super.touchDown(event, x, y, pointer, button);

                wauAnimation.stopMovement();
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);

                if (wauAnimation.isVisible()) {
                    wauAnimation.resumeMovement();
                }
            }

            private void moveWau(float deltaX) {
                try {

                    float newX = wauAnimation.getX() + deltaX;

                    float leftBound = -wauAnimation.width;
                    float rightBound = MunhauzenGame.WORLD_WIDTH;

                    if (leftBound < newX && newX < rightBound) {
                        wauAnimation.setX(newX);
                    }

                    if (wauAnimation.getX() > rightBound) wauAnimation.setX(rightBound);
                    if (wauAnimation.getX() < leftBound) wauAnimation.setX(leftBound);


                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }

            private void moveBackground(float deltaX) {
                try {

                    WauStory story = interaction.storyManager.story;

                    if (story.currentScenario.currentImage == null) return;

                    float newX = background.getX() + deltaX;
                    float currentWidth = story.currentScenario.currentImage.width;

                    float leftBound = -currentWidth + MunhauzenGame.WORLD_WIDTH;
                    float rightBound = 0;

                    if (leftBound < newX && newX < rightBound) {
                        background.setX(newX);
                    }

                    if (background.getX() > rightBound) background.setX(rightBound);
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

        WauStory story = interaction.storyManager.story;

        if (story.currentScenario != null) {

            WauStoryImage image = story.currentScenario.currentImage;
            if (image != null) {

                if (image.withWau) {
                    if (wauAnimation.getStage() == null) {

                        Log.i(tag, "show wauAnimation");

                        wauAnimation.init();

                        root.addActor(wauAnimation);

                        if (!wauAnimation.isMoving) {
                            wauAnimation.startMovement();
                        }
                    }

                } else {
                    wauAnimation.stopMovement();
                    wauAnimation.remove();
                }

            }
        }

    }
}
