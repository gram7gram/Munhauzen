package ua.gram.munhauzen.service;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.ExternalFileHandleResolver;

import ua.gram.munhauzen.entity.Image;
import ua.gram.munhauzen.entity.StoryImage;
import ua.gram.munhauzen.repository.ImageRepository;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.utils.ExternalFiles;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class ExternalImageService extends ImageService {

    public ExternalImageService(GameScreen gameScreen) {
        super(gameScreen);
    }

    @Override
    public AssetManager createAssetManager() {
        return new AssetManager(new ExternalFileHandleResolver());
    }

    @Override
    public String getResource(StoryImage item) {
        Image image = ImageRepository.find(gameScreen.game.gameState, item.image);

        return ExternalFiles.getExpansionImagesDir().path() + "/" + image.file;
    }

}
