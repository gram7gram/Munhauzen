package ua.gram.munhauzen.service;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;

import ua.gram.munhauzen.entity.Image;
import ua.gram.munhauzen.entity.StoryImage;
import ua.gram.munhauzen.repository.ImageRepository;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.utils.ExpansionAssetManager;
import ua.gram.munhauzen.utils.ExternalFiles;
import ua.gram.munhauzen.utils.Files;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class ExpansionImageService extends ImageService {

    public ExpansionImageService(GameScreen gameScreen) {
        super(gameScreen);
    }

    @Override
    public AssetManager createAssetManager() {
        return new ExpansionAssetManager(gameScreen.game);
    }

    @Override
    public String getResource(StoryImage item) {
        Image image = ImageRepository.find(gameScreen.game.gameState, item.image);

        if (ImageRepository.LAST.equals(item.image) && image == null) return null;

        if ("intro".equals(item.chapter)) {
            return Files.getIntroImage(image).path();
        }

        FileHandle file = ExternalFiles.getExpansionImage(gameScreen.game.params, image);
        if (!file.exists()) {
            return null;
        }

        return file.path();
    }

}
