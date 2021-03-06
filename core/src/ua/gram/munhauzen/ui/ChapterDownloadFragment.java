package ua.gram.munhauzen.ui;

import com.badlogic.gdx.graphics.Texture;

import ua.gram.munhauzen.screen.MunhauzenScreen;

public class ChapterDownloadFragment extends BannerFragment<MunhauzenScreen> {

    public ChapterDownloadFragment(MunhauzenScreen screen) {
        super(screen);
    }

    @Override
    public void create() {
        create(null);
    }

    public void create(Runnable action) {

        screen.game.internalAssetManager.load("ui/banner_fond_0.png", Texture.class);
        screen.game.internalAssetManager.load("wau/wau_sheet_1x4.png", Texture.class);
        screen.game.internalAssetManager.finishLoading();

        Banner<?> banner = new ChapterDownloadBanner(this, action);
        banner.create();

        createRoot();

        root.addContainer(banner);
    }

    @Override
    public void onBackDropClicked() {
        //ignore
    }
}
