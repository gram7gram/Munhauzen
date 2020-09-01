package ua.gram.munhauzen.screen.purchase.fragment;

import com.badlogic.gdx.graphics.Texture;

import ua.gram.munhauzen.screen.PurchaseScreen;
import ua.gram.munhauzen.ui.BannerFragment;

public class PromoFragment extends BannerFragment<PurchaseScreen> {

    public PromoFragment(PurchaseScreen screen) {
        super(screen);
    }

    @Override
    public void create() {

        screen.game.internalAssetManager.load("ui/banner_fond_1.png", Texture.class);
        screen.game.internalAssetManager.load("purchase/sv_baron.png", Texture.class);
        screen.game.internalAssetManager.finishLoading();

        PromoBanner banner = new PromoBanner(this);
        banner.create();

        createRoot();

        root.addContainer(banner);
    }

}
