package ua.gram.munhauzen.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.ExternalFileHandleResolver;
import com.badlogic.gdx.utils.GdxRuntimeException;

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
        String path = file;

        if (!path.contains(prefix)) {
            path = prefix + "/" + path;
        }

        if (!Gdx.files.external(path).exists()) {
            throw new GdxRuntimeException("No such asset " + path);
        }

        return path;
    }
}
