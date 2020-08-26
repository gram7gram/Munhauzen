package ua.gram.munhauzen.screen.purchase.fragment;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import ua.gram.munhauzen.FontProvider;
import ua.gram.munhauzen.entity.PurchaseState;
import ua.gram.munhauzen.screen.PurchaseScreen;
import ua.gram.munhauzen.screen.purchase.ui.Card;
import ua.gram.munhauzen.screen.purchase.ui.FreeCard;
import ua.gram.munhauzen.screen.purchase.ui.Purchase10ChapterCard;
import ua.gram.munhauzen.screen.purchase.ui.Purchase1ChapterCard;
import ua.gram.munhauzen.screen.purchase.ui.Purchase3ChapterCard;
import ua.gram.munhauzen.screen.purchase.ui.Purchase5ChapterCard;
import ua.gram.munhauzen.screen.purchase.ui.PurchaseFullCard;
import ua.gram.munhauzen.screen.purchase.ui.PurchaseFullThanksCard;
import ua.gram.munhauzen.screen.purchase.ui.PurchasePart1Card;
import ua.gram.munhauzen.screen.purchase.ui.PurchasePart2Card;
import ua.gram.munhauzen.screen.purchase.ui.PurchaseThanksCard;
import ua.gram.munhauzen.ui.Fragment;
import ua.gram.munhauzen.ui.FragmentRoot;
import ua.gram.munhauzen.ui.PrimaryButton;
import ua.gram.munhauzen.ui.VerticalScrollPane;
import ua.gram.munhauzen.utils.Log;

public class ListFragment extends Fragment {

    private final String tag = getClass().getSimpleName();
    private final PurchaseScreen screen;
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

        Card cardFree = new FreeCard(screen);
        Card cardPart1 = new PurchasePart1Card(screen);
        Card cardPart2 = new PurchasePart2Card(screen);
        Card cardFull = new PurchaseFullCard(screen);
        Card chap1 = new Purchase1ChapterCard(screen);
        Card chap3 = new Purchase3ChapterCard(screen);
        Card chap5 = new Purchase5ChapterCard(screen);
        Card chap10 = new Purchase10ChapterCard(screen);
        Card chapThx = new PurchaseThanksCard(screen);
        Card chapFullThx = new PurchaseFullThanksCard(screen);

        cardFree.setPurchased(true);

        PrimaryButton promo = screen.game.buttonBuilder.primary(screen.game.t("promo_banner.btn"), new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                screen.openPromoBanner();
            }
        });

        Container<?> promoContainer = new Container<>(promo);
        promoContainer.pad(10, 0, 10, 0);
        promoContainer.align(Align.center);

        VerticalGroup list = new VerticalGroup();
        list.addActor(title);
        list.addActor(cardFree);
        list.addActor(chap1);
        list.addActor(chap3);
        list.addActor(chap5);
        list.addActor(chap10);
        list.addActor(chapThx);
        list.addActor(cardPart1);
        list.addActor(cardPart2);
        list.addActor(cardFull);
        list.addActor(chapFullThx);
        list.addActor(promoContainer);

        Table content = new Table();
        content.setFillParent(true);
        content.pad(10);
        content.add(new VerticalScrollPane(list)).expand().top().row();

        root = new FragmentRoot();
        root.addContainer(content);

        root.setName(tag);

        root.setVisible(false);
    }

    public void update() {
        PurchaseState state = screen.game.gameState.purchaseState;
        if (state != null) {
            state.setPro(screen.game.params);
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