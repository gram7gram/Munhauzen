package ua.gram.munhauzen.screen.menu.fragment;

import com.badlogic.gdx.graphics.Texture;

import ua.gram.munhauzen.screen.MenuScreen;
import ua.gram.munhauzen.screen.menu.ui.ReferralThanks3Banner;
import ua.gram.munhauzen.ui.Banner;
import ua.gram.munhauzen.ui.BannerFragment;

public class ReferalThanks3Fragment extends BannerFragment<MenuScreen> {

    public ReferalThanks3Fragment(MenuScreen screen) {
        super(screen);
    }

    @Override
    public void create() {

        screen.assetManager.load("ui/banner_fond_0.png", Texture.class);
        screen.assetManager.load("menu/b_full_version_2.png", Texture.class);
        screen.assetManager.finishLoading();

        Banner<?> banner = new ReferralThanks3Banner(this);
        banner.create();

        createRoot();

        root.addContainer(banner);
    }
}
