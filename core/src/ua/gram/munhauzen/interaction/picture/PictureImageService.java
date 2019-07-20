package ua.gram.munhauzen.interaction.picture;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.StoryImage;
import ua.gram.munhauzen.interaction.PictureInteraction;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.service.InteractionImageService;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class PictureImageService extends InteractionImageService {

    final PictureInteraction interaction;

    public PictureImageService(GameScreen gameScreen, PictureInteraction interaction) {
        super(gameScreen);
        this.interaction = interaction;
    }

    public void displayImage(final StoryImage item) {

        Log.i(tag, "displayImage " + getResource(item));

        PictureStory story = interaction.storyManager.pictureStory;
        for (PictureStoryScenario scenarioOption : story.scenarios) {
            for (StoryImage image : scenarioOption.scenario.images) {
                image.isActive = false;
            }
        }

        item.isActive = true;

        gameScreen.hideImageFragment();

        final Image targetImage = interaction.imageFragment.background;

        targetImage.setDrawable(item.drawable);
        targetImage.setName(getResource(item));
        targetImage.setTouchable(Touchable.enabled);

        if (item.drawable.getMinWidth() > item.drawable.getMinHeight()) {

            float scale = 1f * MunhauzenGame.WORLD_HEIGHT / item.drawable.getMinHeight();
            float width = 1f * item.drawable.getMinWidth() * scale;

            item.height = MunhauzenGame.WORLD_HEIGHT;
            item.width = width;

            targetImage.addListener(new ActorGestureListener() {

                @Override
                public void tap(InputEvent event, float x, float y, int count, int button) {
                    super.tap(event, x, y, count, button);

                    gameScreen.stageInputListener.clicked(event, x, y);
                }

                @Override
                public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
                    super.pan(event, x, y, deltaX, deltaY);

                    Log.i(tag, "pan");

                    try {
                        float newX = targetImage.getX() + deltaX;
                        float currentWidth = item.width;

                        float leftBound = -currentWidth + MunhauzenGame.WORLD_WIDTH;
                        float rightBound = 0;

                        if (leftBound < newX && newX < rightBound) {
                            targetImage.setX(targetImage.getX() + deltaX);
                        }

                        if (targetImage.getX() > 0) targetImage.setX(0);
                        if (targetImage.getX() < leftBound) targetImage.setX(leftBound);
                    } catch (Throwable e) {
                        Log.e(tag, e);
                    }

                }
            });

        } else {

            float scale = 1f * MunhauzenGame.WORLD_WIDTH / item.drawable.getMinWidth();
            float height = 1f * item.drawable.getMinHeight() * scale;

            item.width = MunhauzenGame.WORLD_WIDTH;
            item.height = height;
        }

        interaction.imageFragment.backgroundWidth = item.width;
        interaction.imageFragment.backgroundHeight = item.height;

        interaction.imageFragment.backgroundTable.getCell(targetImage)
                .width(item.width)
                .height(item.height);
    }
}
