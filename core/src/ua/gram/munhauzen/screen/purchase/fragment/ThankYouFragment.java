package ua.gram.munhauzen.screen.purchase.fragment;

import com.badlogic.gdx.graphics.Texture;

import ua.gram.munhauzen.screen.PurchaseScreen;
import ua.gram.munhauzen.ui.BannerFragment;

public class ThankYouFragment extends BannerFragment<PurchaseScreen> {

    public ThankYouFragment(PurchaseScreen screen) {
        super(screen);
    }

    @Override
    public void create() {

        screen.game.internalAssetManager.load("ui/banner_fond_1.png", Texture.class);
        screen.game.internalAssetManager.load("purchase/b_full_version_2.png", Texture.class);

        screen.game.internalAssetManager.finishLoading();

        ThankYouBanner banner = new ThankYouBanner(this);
        banner.create();

        createRoot();

        root.addContainer(banner);
    }

}
