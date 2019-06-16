package ua.gram.munhauzen.service;

import com.badlogic.gdx.assets.AssetManager;

import ua.gram.munhauzen.entity.StoryImage;
import ua.gram.munhauzen.screen.GameScreen;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class InternalImageService extends ImageService {

    public InternalImageService(GameScreen gameScreen) {
        super(gameScreen);
    }

    @Override
    public AssetManager createAssetManager() {
        return new AssetManager();
    }

    @Override
    public String getResource(StoryImage item) {
        return item.internalFile;
    }
}
