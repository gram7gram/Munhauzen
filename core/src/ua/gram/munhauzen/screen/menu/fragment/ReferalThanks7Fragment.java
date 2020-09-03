package ua.gram.munhauzen.screen.menu.fragment;

import com.badlogic.gdx.graphics.Texture;

import ua.gram.munhauzen.screen.MenuScreen;
import ua.gram.munhauzen.screen.menu.ui.ReferralThanks7Banner;
import ua.gram.munhauzen.ui.Banner;
import ua.gram.munhauzen.ui.BannerFragment;

public class ReferalThanks7Fragment extends BannerFragment<MenuScreen> {

    public ReferalThanks7Fragment(MenuScreen screen) {
        super(screen);
    }

    @Override
    public void create() {

        screen.assetManager.load("ui/banner_fond_0.png", Texture.class);
        screen.assetManager.load("menu/b_full_version_2.png", Texture.class);
        screen.assetManager.finishLoading();

        Banner<?> banner = new ReferralThanks7Banner(this);
        banner.create();

        createRoot();

        root.addContainer(banner);
    }
}
