package ua.gram.munhauzen.interaction.wauwau;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.StoryImage;
import ua.gram.munhauzen.interaction.WauInteraction;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.service.InternalImageService;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class WauImageService extends InternalImageService {

    final WauInteraction interaction;

    public WauImageService(GameScreen gameScreen, WauInteraction interaction) {
        super(gameScreen);
        this.interaction = interaction;
    }

    public void displayImage(final StoryImage item) {

        Log.i(tag, "displayImage " + getResource(item));

        WauStory story = interaction.storyManager.story;
        for (WauStoryScenario scenarioOption : story.scenarios) {
            for (StoryImage image : scenarioOption.scenario.images) {
                image.isActive = false;
            }
        }

        item.isActive = true;

        gameScreen.hideImageFragment();

        Image targetImage = interaction.imageFragment.background;

        targetImage.setDrawable(item.drawable);
        targetImage.setName(getResource(item));
        targetImage.setTouchable(Touchable.enabled);

        float scale = 1f * MunhauzenGame.WORLD_HEIGHT / item.drawable.getMinHeight();
        float width = 1f * item.drawable.getMinWidth() * scale;

        item.height = MunhauzenGame.WORLD_HEIGHT;
        item.width = width;

        interaction.imageFragment.backgroundTable.getCell(targetImage)
                .width(item.width)
                .height(item.height);
    }
}
