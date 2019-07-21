package ua.gram.munhauzen.interaction.wauwau;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.StoryImage;
import ua.gram.munhauzen.interaction.WauInteraction;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.service.InteractionImageService;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class WauImageService extends InteractionImageService {

    final WauInteraction interaction;

    public WauImageService(GameScreen gameScreen, WauInteraction interaction) {
        super(gameScreen);
        this.interaction = interaction;
    }

    @Override
    public void onPrepared(StoryImage item) {

        gameScreen.hideImageFragment();

        WauStory story = interaction.storyManager.story;
        if (story != null) {
            for (WauStoryScenario scenarioOption : story.scenarios) {
                for (StoryImage image : scenarioOption.scenario.images) {
                    image.isActive = false;
                }
            }
        }

        super.onPrepared(item);
    }

    public void displayImage(final StoryImage item) {

        Log.i(tag, "displayImage " + getResource(item));

        item.isActive = true;

        Image targetImage = interaction.imageFragment.background;

        targetImage.setDrawable(item.drawable);
        targetImage.setName(getResource(item));
        targetImage.setTouchable(Touchable.enabled);

        if (item.drawable.getMinWidth() > item.drawable.getMinHeight()) {

            float scale = 1f * MunhauzenGame.WORLD_HEIGHT / item.drawable.getMinHeight();
            float width = 1f * item.drawable.getMinWidth() * scale;

            item.height = MunhauzenGame.WORLD_HEIGHT;
            item.width = width;

        } else {

            float scale = 1f * MunhauzenGame.WORLD_WIDTH / item.drawable.getMinWidth();
            float height = 1f * item.drawable.getMinHeight() * scale;

            item.width = MunhauzenGame.WORLD_WIDTH;
            item.height = height;
        }

        interaction.imageFragment.backgroundHeight = item.height;
        interaction.imageFragment.backgroundWidth = item.width;

        interaction.imageFragment.backgroundTable.getCell(targetImage)
                .width(item.width)
                .height(item.height);
    }
}
