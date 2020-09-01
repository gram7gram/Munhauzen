package ua.gram.munhauzen.screen.purchase.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import ua.gram.munhauzen.entity.Product;
import ua.gram.munhauzen.entity.Purchase;
import ua.gram.munhauzen.entity.PurchaseState;
import ua.gram.munhauzen.screen.PurchaseScreen;
import ua.gram.munhauzen.utils.Log;

public abstract class ConsumableCard extends PurchaseCard {

    public ConsumableCard(final PurchaseScreen screen, String id, String title, String description, String image) {
        super(screen, id, title, description, image);

        clearListeners();
        onClick(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                try {
                    Log.i(tag, "clicked on " + productId);

                    screen.openAdultGateBanner(new Runnable() {
                        @Override
                        public void run() {
                            screen.game.purchaseManager.purchase(productId);
                        }
                    });
                } catch (Throwable e) {
                    Log.e(tag, e);

                    screen.onCriticalError(e);
                }
            }
        });
    }

    @Override
    public void updateCardText(Product product) {
        PurchaseState state = screen.game.gameState.purchaseState;

        if (!product.isAvailable) {
            setPriceText(screen.game.t("purchase_screen.unavailable"));
            setEnabled(false);
        } else {

            boolean isPurchased = false;

            if (state.purchases != null) {
                for (Purchase purchase : state.purchases) {
                    if (purchase.productId.equals(product.id)) {
                        isPurchased = true;
                        break;
                    }
                }
            }

            setEnabled(true);
            setPurchased(isPurchased);
            setPriceNumber(product.localPricing);
        }
    }
}
