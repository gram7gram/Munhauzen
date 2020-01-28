package ua.gram.munhauzen.screen.game.fragment;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import ua.gram.munhauzen.FontProvider;
import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.Purchase;
import ua.gram.munhauzen.entity.PurchaseState;
import ua.gram.munhauzen.entity.Scenario;
import ua.gram.munhauzen.screen.GameScreen;
import ua.gram.munhauzen.screen.game.ui.PurchaseBanner;
import ua.gram.munhauzen.ui.Fragment;
import ua.gram.munhauzen.ui.FragmentRoot;
import ua.gram.munhauzen.utils.Log;

public class PurchaseFragment extends Fragment {

    private final String tag = getClass().getSimpleName();
    public final GameScreen screen;
    public FragmentRoot root;
    public boolean isFadeIn, isFadeOut;

    public PurchaseFragment(GameScreen screen) {
        this.screen = screen;
    }

    public void create(Scenario scenario) {

        screen.assetManager.load("menu/b_demo_version_2.png", Texture.class);
        screen.assetManager.load("ui/banner_fond_1.png", Texture.class);
        screen.assetManager.load("gallery/b_closed_1.png", Texture.class);

        screen.assetManager.finishLoading();

        Texture txt = screen.assetManager.get("gallery/b_closed_1.png", Texture.class);

        float width = MunhauzenGame.WORLD_WIDTH * .25f;
        float height = txt.getHeight() * (txt.getWidth() / width);

        Image img = new Image(txt);
        img.setSize(width, height);

        Container<Image> backdrop = new Container<>(img);
        backdrop.setTouchable(Touchable.enabled);

        final PurchaseBanner banner = new PurchaseBanner(this);
        banner.setVisible(false);

        root = new FragmentRoot();
        root.addContainer(backdrop);

        if (scenario != null) {
            if (!screen.game.params.isProduction()) {

                Label info = new Label(scenario.name + "\n(" + scenario.expansion + ")", new Label.LabelStyle(
                        screen.game.fontProvider.getFont(FontProvider.DroidSansMono, FontProvider.h3),
                        Color.BLACK
                ));
                info.setAlignment(Align.center);

                Container<Label> container = new Container<>(info);
                container.pad(10);
                container.align(Align.top);
                container.setTouchable(Touchable.disabled);

                root.addContainer(container);
            }
        }

        root.addContainer(banner);

        backdrop.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                if (event.isHandled()) return;

                try {

                    Log.i(tag, "backdrop clicked");

                    screen.stageInputListener.clicked(event, x, y);

                    if (banner.isVisible()) {
                        banner.fadeOut();
                    } else {
                        banner.fadeIn();
                    }
                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }
        });

        root.setName(tag);

        root.setVisible(false);
    }

    public void fadeIn() {

        if (!isMounted()) return;
        if (isFadeIn) return;

        isFadeOut = false;
        isFadeIn = true;

        Log.i(tag, "fadeIn");

        root.setVisible(true);

        root.clearActions();
        root.addAction(Actions.sequence(
                Actions.alpha(0),
                Actions.alpha(1, .3f),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        isFadeIn = false;
                        isFadeOut = false;
                    }
                })
        ));

        screen.stopCurrentSfx();

        try {

            PurchaseState state = screen.game.gameState.purchaseState;

            Purchase part2Purchase = null, part1Purchase = null;

            for (Purchase purchase : state.purchases) {

                if (purchase.productId.equals(screen.game.params.appStoreSkuPart2)) {
                    part2Purchase = purchase;
                }

                if (purchase.productId.equals(screen.game.params.appStoreSkuPart1)) {
                    part1Purchase = purchase;
                }
            }

            if (part1Purchase == null) {
                screen.game.currentSfx = screen.game.sfxService.onPurchasePart1();
            } else if (part2Purchase == null) {
                screen.game.currentSfx = screen.game.sfxService.onPurchasePart2();
            }
        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }

    public void fadeOut() {

        if (isFadeOut) return;

        isFadeOut = true;
        isFadeIn = false;

        root.setTouchable(Touchable.disabled);
        root.setVisible(true);

        root.clearActions();
        root.addAction(
                Actions.sequence(
                        Actions.alpha(0, .2f),
                        Actions.visible(false),
                        Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                isFadeIn = false;
                                isFadeOut = false;
                            }
                        })
                )
        );
    }

    @Override
    public Actor getRoot() {
        return root;
    }

}
