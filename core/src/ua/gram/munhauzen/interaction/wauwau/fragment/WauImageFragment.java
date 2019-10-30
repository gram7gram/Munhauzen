package ua.gram.munhauzen.interaction.wauwau.fragment;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.interaction.WauInteraction;
import ua.gram.munhauzen.interaction.wauwau.WauStory;
import ua.gram.munhauzen.interaction.wauwau.WauStoryImage;
import ua.gram.munhauzen.interaction.wauwau.animation.WauAnimation;
import ua.gram.munhauzen.screen.game.fragment.InteractionFragment;
import ua.gram.munhauzen.screen.game.ui.BackgroundImage;
import ua.gram.munhauzen.ui.FragmentRoot;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class WauImageFragment extends InteractionFragment {

    private final WauInteraction interaction;
    WauAnimation wauAnimation;
    public Group items;
    public FragmentRoot root;
    public BackgroundImage backgroundImage;

    public WauImageFragment(WauInteraction interaction) {
        this.interaction = interaction;
    }

    public void create() {

        Log.i(tag, "create");

        backgroundImage = new BackgroundImage(interaction.gameScreen);

        wauAnimation = new WauAnimation(
                interaction.assetManager.get("wau/wau_sheet_1x4.png", Texture.class),
                this
        );

        items = new Group();

        root = new FragmentRoot();
        root.addContainer(backgroundImage);

        root.setName(tag);

        backgroundImage.setBackgroundListener(new ActorGestureListener() {

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

                float backDeltaX = backgroundImage.moveBackground(deltaX);

                if (backDeltaX > 0 && wauAnimation.getStage() != null) {
                    wauAnimation.stopMovement();
                    moveWau(backDeltaX);
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
        });
    }

    @Override
    public Actor getRoot() {
        return root;
    }

    public void update() {

        if (interaction.storyManager == null) return;

        WauStory story = interaction.storyManager.story;

        if (story != null && story.currentScenario != null) {

            WauStoryImage image = story.currentScenario.currentImage;
            if (image != null) {

                if (image.withWau) {
                    if (wauAnimation.getStage() == null) {

                        Log.i(tag, "show wauAnimation");

                        wauAnimation.init();

                        root.addContainer(new Container<>(wauAnimation));
                    }

                    if (!wauAnimation.isMoving) {
                        wauAnimation.startMovement();
                    }

                } else {
                    wauAnimation.stopMovement();
                    wauAnimation.remove();
                }

            }
        }

    }
}
