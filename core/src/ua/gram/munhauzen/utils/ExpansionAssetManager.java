package ua.gram.munhauzen.utils;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.ExternalFileHandleResolver;

public class ExpansionAssetManager extends AssetManager {

    final String prefix;

    public ExpansionAssetManager() {
        super(new ExternalFileHandleResolver());

        prefix = ExternalFiles.getExpansionDir().path();
    }

    @Override
    public synchronized <T> T get(String fileName, Class<T> type) {
        return super.get(getPath(fileName), type);
    }

    @Override
    public synchronized boolean isLoaded(String fileName, Class type) {
        return super.isLoaded(getPath(fileName), type);
    }

    @Override
    public synchronized boolean isLoaded(String fileName) {
        return super.isLoaded(getPath(fileName));
    }

    @Override
    public synchronized void unload(String fileName) {
        super.unload(getPath(fileName));
    }

    @Override
    public synchronized <T> void load(String fileName, Class<T> type) {
        super.load(getPath(fileName), type);
    }

    private String getPath(String file) {
        if (!file.contains(prefix)) {
            return prefix + "/" + file;
        }

        return file;
    }
}
