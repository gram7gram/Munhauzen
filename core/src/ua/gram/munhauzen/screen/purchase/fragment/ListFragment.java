package ua.gram.munhauzen.screen.purchase.fragment;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.Align;

import ua.gram.munhauzen.FontProvider;
import ua.gram.munhauzen.entity.Product;
import ua.gram.munhauzen.entity.Purchase;
import ua.gram.munhauzen.entity.PurchaseState;
import ua.gram.munhauzen.screen.PurchaseScreen;
import ua.gram.munhauzen.screen.purchase.ui.Card;
import ua.gram.munhauzen.screen.purchase.ui.FreeCard;
import ua.gram.munhauzen.screen.purchase.ui.PurchaseFullCard;
import ua.gram.munhauzen.screen.purchase.ui.PurchasePart1Card;
import ua.gram.munhauzen.screen.purchase.ui.PurchasePart2Card;
import ua.gram.munhauzen.ui.Fragment;
import ua.gram.munhauzen.ui.FragmentRoot;
import ua.gram.munhauzen.ui.VerticalScrollPane;
import ua.gram.munhauzen.utils.Log;

public class ListFragment extends Fragment {

    private final String tag = getClass().getSimpleName();
    private final PurchaseScreen screen;
    public Card cardFree, cardPart1, cardPart2, cardFull;
    public FragmentRoot root;
    boolean isFadeIn, isFadeOut;

    public ListFragment(PurchaseScreen screen) {
        this.screen = screen;
    }

    public void create() {

        Log.i(tag, "create");

        Label title = new Label(screen.game.t("purchase_screen.title"), new Label.LabelStyle(
                screen.game.fontProvider.getFont(FontProvider.h2),
                Color.BLACK
        ));
        title.setWrap(true);
        title.setAlignment(Align.center);

        cardFree = new FreeCard(screen);
        cardPart1 = new PurchasePart1Card(screen);
        cardPart2 = new PurchasePart2Card(screen);
        cardFull = new PurchaseFullCard(screen);

        cardFree.setPurchased(true);

        VerticalGroup list = new VerticalGroup();
        list.addActor(title);
        list.addActor(cardFree);
        list.addActor(cardPart1);
        list.addActor(cardPart2);
        list.addActor(cardFull);

        Table content = new Table();
        content.setFillParent(true);
        content.pad(10);
        content.add(new VerticalScrollPane(list)).expand().top().row();

        root = new FragmentRoot();
        root.addContainer(content);

        root.setName(tag);

        root.setVisible(false);

        cardFull.setPriceText(screen.game.t("purchase_screen.unavailable"));
        cardFull.setEnabled(false);
        cardPart1.setPriceText(screen.game.t("purchase_screen.unavailable"));
        cardPart1.setEnabled(false);
        cardPart2.setPriceText(screen.game.t("purchase_screen.unavailable"));
        cardPart2.setEnabled(false);
    }

    public void update() {
        PurchaseState state = screen.game.gameState.purchaseState;
        if (state != null) {

            state.setPro(screen.game.params);

            if (state.products != null) {

                Product part1 = null, part2 = null, full = null;
                for (Product product : state.products) {
                    if (screen.game.params.appStoreSkuFull.equals(product.id)) {
                        full = product;
                    } else if (screen.game.params.appStoreSkuPart1.equals(product.id)) {
                        part1 = product;
                    } else if (screen.game.params.appStoreSkuPart2.equals(product.id)) {
                        part2 = product;
                    }
                }

                if (part1 != null && part2 != null && full != null) {

                    updatePart1(part1);

                    updatePart2(part2);

                    updatePartFull(full);

                    if (cardFull.purchased) {
                        if (part1.isAvailable) {
//                            cardPart1.setTouchable(Touchable.disabled);
                            cardPart1.setPriceText("");
                            cardPart1.setPurchased(true);
                            cardPart1.setEnabled(true);
                        }

                        if (part2.isAvailable) {
//                            cardPart2.setTouchable(Touchable.disabled);
                            cardPart2.setPriceText("");
                            cardPart2.setPurchased(true);
                            cardPart2.setEnabled(true);
                        }
                    } else {
                        if (cardPart2.purchased) {
                            if (part1.isAvailable) {
//                            cardPart1.setTouchable(Touchable.disabled);
                                cardPart1.setPriceText("");
                                cardPart1.setPurchased(true);
                                cardPart1.setEnabled(true);
                            }
                        }

                        if (cardPart1.purchased && cardPart2.purchased) {
                            if (full.isAvailable) {
//                            cardFull.setTouchable(Touchable.disabled);
                                cardFull.setPriceText("");
                                cardFull.setEnabled(true);
                                cardFull.setPurchased(true);
                            }
                        }
                    }


                }
            }

        }
    }

    private void updatePart1(Product product) {
        PurchaseState state = screen.game.gameState.purchaseState;

        if (!product.isAvailable) {
            cardPart1.setPriceText(screen.game.t("purchase_screen.unavailable"));
            cardPart1.setEnabled(false);
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

            cardPart1.setEnabled(true);
            cardPart1.setPurchased(isPurchased);

            if (cardPart1.purchased) {
                cardPart1.setPriceText(screen.game.t("purchase_screen.download"));
            } else {
                cardPart1.setPriceNumber(product.localPricing);
            }
        }
    }

    private void updatePart2(Product product) {
        PurchaseState state = screen.game.gameState.purchaseState;

        if (!product.isAvailable) {
            cardPart2.setPriceText(screen.game.t("purchase_screen.unavailable"));
            cardPart2.setEnabled(false);
        } else {

            boolean isPart2Purchased = false, isPart1Purchased = false;

            if (state.purchases != null) {
                for (Purchase purchase : state.purchases) {
                    if (purchase.productId.equals(screen.game.params.appStoreSkuPart2)) {
                        isPart2Purchased = true;
                    }
                    if (purchase.productId.equals(screen.game.params.appStoreSkuPart1)) {
                        isPart1Purchased = true;
                    }
                }
            }

            cardPart2.setEnabled(isPart1Purchased);
            cardPart2.setPurchased(isPart2Purchased);

            if (cardPart2.purchased) {
                cardPart2.setPriceText(screen.game.t("purchase_screen.download"));
            } else {
                cardPart2.setPriceNumber(product.localPricing);
            }
        }
    }

    private void updatePartFull(Product product) {
        PurchaseState state = screen.game.gameState.purchaseState;

        if (!product.isAvailable) {
            cardFull.setPriceText(screen.game.t("purchase_screen.unavailable"));
            cardFull.setEnabled(false);
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

            cardFull.setEnabled(true);
            cardFull.setPurchased(isPurchased);

            if (cardFull.purchased) {
                cardFull.setPriceText(screen.game.t("purchase_screen.download"));
            } else {
                cardFull.setPriceNumber(product.localPricing);
            }
        }
    }

    public void fadeOut(Runnable runnable) {

        if (isFadeOut) return;

        isFadeIn = false;
        isFadeOut = true;

        root.addAction(Actions.sequence(
                Actions.fadeOut(.4f),
                Actions.visible(false),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        isFadeIn = false;
                        isFadeOut = false;
                    }
                }),
                Actions.run(runnable)
        ));
    }

    public void fadeIn() {

        if (isFadeIn) return;

        isFadeIn = true;
        isFadeOut = false;

        root.addAction(Actions.sequence(
                Actions.visible(true),
                Actions.fadeIn(.4f),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        isFadeIn = false;
                        isFadeOut = false;
                    }
                })
        ));
    }

    @Override
    public Actor getRoot() {
        return root;
    }
}