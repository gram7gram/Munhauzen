package ua.gram.munhauzen.screen.purchase.fragment;

import com.badlogic.gdx.graphics.Texture;

import ua.gram.munhauzen.screen.PurchaseScreen;
import ua.gram.munhauzen.ui.BannerFragment;

public class PurchasePendingFragment extends BannerFragment<PurchaseScreen> {

    public PurchasePendingFragment(PurchaseScreen screen) {
        super(screen);
    }

    @Override
    public void create() {

        screen.game.internalAssetManager.load("ui/banner_fond_1.png", Texture.class);
        screen.game.internalAssetManager.finishLoading();

        PurchasePendingBanner banner = new PurchasePendingBanner(this);
        banner.create();

        createRoot();

        backdrop.clearListeners();

        root.addContainer(banner);
    }


}
