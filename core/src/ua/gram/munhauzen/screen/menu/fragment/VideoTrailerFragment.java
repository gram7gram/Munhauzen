package ua.gram.munhauzen.screen.menu.fragment;

import com.badlogic.gdx.graphics.Texture;

import ua.gram.munhauzen.screen.MenuScreen;
import ua.gram.munhauzen.screen.menu.ui.VideoTrailerBanner;
import ua.gram.munhauzen.ui.Banner;
import ua.gram.munhauzen.ui.BannerFragment;

public class VideoTrailerFragment extends BannerFragment<MenuScreen> {

    public VideoTrailerFragment(MenuScreen screen) {
        super(screen);
    }

    @Override
    public void create() {

        screen.assetManager.load("ui/banner_fond_0.png", Texture.class);
        screen.assetManager.load("menu/pVideo.png", Texture.class);
        screen.assetManager.finishLoading();

        Banner<?> banner = new VideoTrailerBanner(this);
        banner.create();

        createRoot();

        root.addContainer(banner);
    }
}
