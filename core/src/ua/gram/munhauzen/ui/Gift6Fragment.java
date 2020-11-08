package ua.gram.munhauzen.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.GdxRuntimeException;

import ua.gram.munhauzen.screen.MunhauzenScreen;

public class Gift6Fragment extends BannerFragment<MunhauzenScreen> {

    public Gift6Fragment(MunhauzenScreen screen) {
        super(screen);
    }

    @Override
    public void create() {
        throw new GdxRuntimeException("Not supported");
    }

    public void create(Runnable action) {

        screen.game.internalAssetManager.load("ui/banner_fond_0.png", Texture.class);
        screen.game.internalAssetManager.load("menu/b_full_version_2.png", Texture.class);
        screen.game.internalAssetManager.finishLoading();

        Banner<?> banner = new Gift6Banner(this, action);
        banner.create();

        createRoot();

        root.addContainer(banner);
    }

    @Override
    public void onBackDropClicked() {
        //ignore
    }
}
