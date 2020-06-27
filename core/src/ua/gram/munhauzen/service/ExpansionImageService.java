package ua.gram.munhauzen.service;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.ExternalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;

import ua.gram.munhauzen.entity.Image;
import ua.gram.munhauzen.entity.StoryImage;
import ua.gram.munhauzen.repository.ImageRepository;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.utils.ExternalFiles;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class ExpansionImageService extends ImageService {

    public ExpansionImageService(GameScreen gameScreen) {
        super(gameScreen);
    }

    @Override
    public AssetManager createAssetManager() {
        return new AssetManager(new ExternalFileHandleResolver());
    }

    @Override
    public String getResource(StoryImage item) {
        Image image = ImageRepository.find(gameScreen.game.gameState, item.image);

        if (ImageRepository.LAST.equals(item.image) && image == null) return null;

        FileHandle file = ExternalFiles.getExpansionImage(gameScreen.game.params, image);
        if (!file.exists()) {
            return null;
        }

        return file.path();
    }

}
