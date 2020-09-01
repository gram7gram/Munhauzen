package ua.gram.munhauzen.screen.purchase.fragment;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;

import ua.gram.munhauzen.ButtonBuilder;
import ua.gram.munhauzen.FontProvider;
import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.screen.PurchaseScreen;
import ua.gram.munhauzen.screen.purchase.ui.PromoInput;
import ua.gram.munhauzen.ui.Banner;
import ua.gram.munhauzen.ui.FixedImage;
import ua.gram.munhauzen.utils.Log;

public class PromoBanner extends Banner<PurchaseScreen> {

    final PromoFragment fragment;
    Button button;
    TextField input;
    ArrayList<String> codes;

    public PromoBanner(PromoFragment fragment) {
        super(fragment.screen);

        this.fragment = fragment;

        codes = new ArrayList<>();
        codes.add(fragment.screen.game.params.promocode1);
        codes.add(fragment.screen.game.params.promocode2);
    }

    @Override
    public Texture getBackgroundTexture() {
        return screen.game.internalAssetManager.get("ui/banner_fond_1.png", Texture.class);
    }

    @Override
    public Table createContent() {

        float minWidth = MunhauzenGame.WORLD_WIDTH * .9f;

        Table content = new Table();
        content.pad(20, MunhauzenGame.WORLD_WIDTH * .1f, 40, MunhauzenGame.WORLD_WIDTH * .1f);

        float cellMinWidth = minWidth - content.getPadLeft() - content.getPadRight();

        Table rows = new Table();

        Label.LabelStyle style = new Label.LabelStyle(
                screen.game.fontProvider.getFont(FontProvider.h4),
                Color.BLACK
        );

        for (String sentence : screen.game.t("promo_banner.title").split("\n")) {
            Label label = new Label(sentence, style);
            label.setAlignment(Align.center);
            label.setWrap(true);

            rows.add(label)
                    .minWidth(cellMinWidth - 40)
                    .padBottom(10)
                    .row();
        }

        FixedImage img = new FixedImage(
                screen.game.internalAssetManager.get("purchase/sv_baron.png", Texture.class),
                cellMinWidth / 2f
        );

        Table columns = new Table();

        columns.add(img)
                .height(img.height)
                .width(img.width)
                .center()
                .row();

        columns.add(rows)
                .minWidth(cellMinWidth)
                .center().row();

        button = screen.game.buttonBuilder.primary(screen.game.t("promo_banner.ok_btn"), new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                try {

                    if (game.gameState.purchaseState.promocodes == null) {
                        game.gameState.purchaseState.promocodes = new ArrayList<>();
                    }

                    String text = input.getText();

                    boolean hasMatch = false;
                    for (String code : codes) {
                        if (code.equals(text)) {
                            hasMatch = true;
                            break;
                        }
                    }

                    if (!hasMatch) {
                        Log.e(tag, "NO PROMOCODE " + text);
                        return;
                    }

                    boolean isAdded = false;
                    for (String promocode : game.gameState.purchaseState.promocodes) {
                        if (promocode.equals(text)) {
                            isAdded = true;
                            break;
                        }
                    }

                    if (!isAdded) {
                        Log.e(tag, "ALREADY ADDED PROMOCODE " + text);
                        return;
                    }

                    Log.e(tag, "ADD PROMOCODE " + text);

                    game.gameState.purchaseState.promocodes.add(text);

                    game.purchaseManager.updatePurchaseState();

                    game.syncState();

                    screen.openPromoSuccessBanner();

                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }
        });

        input = new PromoInput(screen.game);

        Table buttons = new Table();
        buttons.add(input)
                .width(ButtonBuilder.BTN_PRIMARY_WIDTH)
                .height(ButtonBuilder.BTN_PRIMARY_HEIGHT)
                .padBottom(10).row();
        buttons.add(button)
                .width(ButtonBuilder.BTN_PRIMARY_WIDTH)
                .height(ButtonBuilder.BTN_PRIMARY_HEIGHT).row();

        content.add(columns).row();
        content.add(buttons).row();

        return content;
    }

}
