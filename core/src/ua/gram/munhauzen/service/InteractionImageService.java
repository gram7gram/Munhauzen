package ua.gram.munhauzen.service;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.ExternalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;

import ua.gram.munhauzen.entity.StoryImage;
import ua.gram.munhauzen.repository.ImageRepository;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.utils.ExternalFiles;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class InteractionImageService extends ImageService {

    public InteractionImageService(GameScreen gameScreen) {
        super(gameScreen);
    }

    @Override
    public AssetManager createAssetManager() {
        return new AssetManager(new ExternalFileHandleResolver());
    }

    @Override
    public String getResource(StoryImage item) {

        FileHandle file;
        if (ImageRepository.LAST.equals(item.image)) {

            ua.gram.munhauzen.entity.Image image = gameScreen.getLastBackground();
            if (image == null) {
                return null;
            }

            file = ExternalFiles.getExpansionImage(gameScreen.game.params, image);
        } else {
            file = ExternalFiles.getExpansionFile(gameScreen.game.params, item.image);
        }

        if (!file.exists()) {
            throw new GdxRuntimeException("Image file does not exist " + item.image + " at " + file.path());
        }

        return file.path();
    }

    @Override
    public void saveCurrentBackground(StoryImage item) {
        if (item.image != null && !ImageRepository.LAST.equals(item.image)) {
            gameScreen.setLastBackground(item.image);
        }
    }
}
