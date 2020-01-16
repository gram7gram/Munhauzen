package ua.gram.munhauzen.screen.purchase.fragment;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.Align;

import ua.gram.munhauzen.FontProvider;
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
    public Card card1, card2, card3, card4;
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

        card1 = new FreeCard(screen);
        card2 = new PurchasePart1Card(screen);
        card3 = new PurchasePart2Card(screen);
        card4 = new PurchaseFullCard(screen);

        card1.setPurchased(true);

        VerticalGroup list = new VerticalGroup();
        list.addActor(card1);
        list.addActor(card4);
        list.addActor(card2);
        list.addActor(card3);

        Table content = new Table();
        content.setFillParent(true);
        content.pad(10);
        content.add(title).expandX().row();
        content.add(new VerticalScrollPane(list)).fill().row();

        root = new FragmentRoot();
        root.addContainer(content);

        root.setName(tag);

        root.setVisible(false);
    }

    public void update() {
        PurchaseState state = screen.game.gameState.purchaseState;
        if (state != null) {
            if (state.isPro) {
                if (!card2.purchased) {
                    card2.setEnabled(false);
                }
                if (!card3.purchased) {
                    card3.setEnabled(false);
                }
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