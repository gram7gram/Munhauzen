package ua.gram.munhauzen.screen.menu.fragment;

import com.badlogic.gdx.graphics.Texture;

import ua.gram.munhauzen.screen.MenuScreen;
import ua.gram.munhauzen.screen.menu.ui.ReferalBanner;
import ua.gram.munhauzen.ui.Banner;
import ua.gram.munhauzen.ui.BannerFragment;

public class ReferalFragment extends BannerFragment<MenuScreen> {

    public ReferalFragment(MenuScreen screen) {
        super(screen);
    }

    @Override
    public void create() {

        screen.assetManager.load("ui/banner_fond_1.png", Texture.class);
        screen.assetManager.load("wau/wau_sheet_1x4.png", Texture.class);
        screen.assetManager.finishLoading();

        Banner<?> banner = new ReferalBanner(this);
        banner.create();

        createRoot();

        root.addContainer(banner);

        screen.game.stopCurrentSfx();
        screen.game.sfxService.onReferralOpened();
    }
}
