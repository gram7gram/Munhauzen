package ua.gram.munhauzen.utils;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;

public class InternalAssetManager extends AssetManager {
    public InternalAssetManager() {
        super(new InternalFileHandleResolver());
    }

    @Override
    public synchronized void unload(String fileName) {
        if (!isLoaded(fileName)) return;

        super.unload(fileName);
    }
}
