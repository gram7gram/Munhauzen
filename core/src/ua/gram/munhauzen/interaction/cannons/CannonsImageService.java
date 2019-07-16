package ua.gram.munhauzen.interaction.cannons;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.StoryImage;
import ua.gram.munhauzen.interaction.CannonsInteraction;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.service.InternalImageService;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class CannonsImageService extends InternalImageService {

    final CannonsInteraction interaction;

    public CannonsImageService(GameScreen gameScreen, CannonsInteraction interaction) {
        super(gameScreen);
        this.interaction = interaction;
    }

    public void displayImage(final StoryImage item) {

        Log.i(tag, "displayImage " + getResource(item));

        CannonsStory story = interaction.storyManager.story;
        for (CannonsStoryScenario scenarioOption : story.scenarios) {
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

        interaction.imageFragment.backgroundHeight = item.height = MunhauzenGame.WORLD_HEIGHT;
        interaction.imageFragment.backgroundWidth = item.width = width;

        interaction.imageFragment.backgroundTable.getCell(targetImage)
                .width(item.width)
                .height(item.height);
    }
}
