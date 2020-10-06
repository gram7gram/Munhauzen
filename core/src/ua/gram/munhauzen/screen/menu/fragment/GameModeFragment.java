package ua.gram.munhauzen.screen.menu.fragment;

import com.badlogic.gdx.graphics.Texture;

import ua.gram.munhauzen.screen.MenuScreen;
import ua.gram.munhauzen.screen.menu.ui.GameModeBanner;
import ua.gram.munhauzen.ui.Banner;
import ua.gram.munhauzen.ui.BannerFragment;

public class GameModeFragment extends BannerFragment<MenuScreen> {

    public GameModeFragment(MenuScreen screen) {
        super(screen);
    }

    @Override
    public void create() {

        screen.assetManager.load("ui/banner_fond_0.png", Texture.class);
        screen.assetManager.load("ui/banner_version.png", Texture.class);
        screen.assetManager.finishLoading();

        Banner<?> banner = new GameModeBanner(this);
        banner.create();

        createRoot();

        root.addContainer(banner);
    }
}
