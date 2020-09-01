package ua.gram.munhauzen.screen.purchase.fragment;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

import ua.gram.munhauzen.FontProvider;
import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.screen.PurchaseScreen;
import ua.gram.munhauzen.ui.Banner;

public class PurchasePendingBanner extends Banner<PurchaseScreen> {

    final PurchasePendingFragment fragment;

    public PurchasePendingBanner(PurchasePendingFragment fragment) {
        super(fragment.screen);

        this.fragment = fragment;
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

        for (String sentence : screen.game.t("purchase_pending_banner.title").split("\n")) {
            Label label = new Label(sentence, style);
            label.setAlignment(Align.center);
            label.setWrap(true);

            rows.add(label)
                    .minWidth(cellMinWidth - 40)
                    .padBottom(10)
                    .row();
        }

        Table columns = new Table();
        columns.pad(100, 0, 100, 0);

        columns.add(rows)
                .minWidth(cellMinWidth)
                .center().row();

        content.add(columns).row();

        return content;
    }

}
