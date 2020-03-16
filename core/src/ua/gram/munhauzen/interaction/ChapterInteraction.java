package ua.gram.munhauzen.interaction;

import com.badlogic.gdx.graphics.Texture;

import ua.gram.munhauzen.entity.Chapter;
import ua.gram.munhauzen.entity.Story;
import ua.gram.munhauzen.interaction.chapter.fragment.ChapterImageFragment;
import ua.gram.munhauzen.repository.ChapterRepository;
import ua.gram.munhauzen.screen.GameScreen;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class ChapterInteraction extends AbstractInteraction {

    boolean isLoaded;
    public ChapterImageFragment imageFragment;
    public Chapter chapter;

    public ChapterInteraction(GameScreen gameScreen) {
        super(gameScreen);
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public void start() {
        super.start();

        gameScreen.hideProgressBar();

        Story story = gameScreen.getStory();

        String chapterName = story.currentScenario.scenario.chapter;
        chapter = ChapterRepository.find(gameScreen.game.gameState, chapterName);

        assetManager.load("chapter/frame_2.png", Texture.class);

        loadIcon();
    }

    public Texture getIcon() {
        return assetManager.get(chapter.icon, Texture.class);
    }

    private void loadIcon() {
        assetManager.load(chapter.icon, Texture.class);
    }

    public void onResourcesLoaded() {

        isLoaded = true;

        imageFragment = new ChapterImageFragment(this);
        imageFragment.create(chapter);

        gameScreen.gameLayers.setInteractionLayer(imageFragment);
    }

    @Override
    public void update() {
        super.update();

        if (assetManager == null) return;

        gameScreen.hideProgressBar();

        if (!isLoaded) {
            if (assetManager.isFinished()) {
                onResourcesLoaded();
            }
            return;
        }

        if (imageFragment != null) {
            imageFragment.update();
        }
    }

    @Override
    public void dispose() {
        super.dispose();

        isLoaded = false;

        if (imageFragment != null) {
            imageFragment.dispose();
            imageFragment = null;
        }

    }
}
