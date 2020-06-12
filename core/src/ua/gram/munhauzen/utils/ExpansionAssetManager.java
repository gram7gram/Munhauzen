package ua.gram.munhauzen.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.ExternalFileHandleResolver;
import com.badlogic.gdx.utils.GdxRuntimeException;

import ua.gram.munhauzen.MunhauzenGame;

public class ExpansionAssetManager extends AssetManager {

    final String tag = getClass().getSimpleName();
    final String prefix;

    public ExpansionAssetManager(MunhauzenGame game) {
        super(new ExternalFileHandleResolver());

        prefix = ExternalFiles.getExpansionDir(game.params).path();
    }

    @Override
    public synchronized boolean update() {
        try {
            return super.update();
        } catch (Throwable e) {
            Log.e(tag, e);

            throw e;
        }
    }

    @Override
    public synchronized <T> T get(String fileName, Class<T> type) {
        try {
            return super.get(getPath(fileName), type);
        } catch (Throwable ignore) {
        }

        return null;
    }

    @Override
    public synchronized boolean isLoaded(String fileName, Class type) {
        try {
            return super.isLoaded(getPath(fileName), type);
        } catch (Throwable ignore) {
        }

        return false;
    }


    @Override
    public synchronized boolean isLoaded(String fileName) {
        try {
            return super.isLoaded(getPath(fileName));
        } catch (Throwable ignore) {
        }

        return false;
    }

    @Override
    public synchronized void unload(String fileName) {
        try {
            if (!isLoaded(fileName)) return;

            super.unload(getPath(fileName));
        } catch (Throwable ignore) {
        }
    }

    @Override
    public synchronized <T> void load(String fileName, Class<T> type) {
        try {
            super.load(getPath(fileName), type);
        } catch (Throwable e) {
            Log.e(tag, e);
        }
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
