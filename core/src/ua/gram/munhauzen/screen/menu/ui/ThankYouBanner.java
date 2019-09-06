package ua.gram.munhauzen.screen.menu.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import ua.gram.munhauzen.FontProvider;
import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.screen.menu.fragment.ThankYouFragment;
import ua.gram.munhauzen.ui.FitImage;
import ua.gram.munhauzen.utils.Log;

public class ThankYouBanner extends Banner {

    final ThankYouFragment fragment;

    public ThankYouBanner(ThankYouFragment fragment) {
        super(fragment.screen);

        this.fragment = fragment;
    }

    @Override
    Texture getBackgroundTexture() {
        return screen.assetManager.get("ui/banner_fond_0.png", Texture.class);
    }

    @Override
    Table createContent() {

        String[] sentences = {
                "Thank you for purchasing the full version!",
                "You are breathtaking!",
                "Please, rate out application and leave a positive review",
        };

        float minWidth = MunhauzenGame.WORLD_WIDTH * .7f;

        Table content = new Table();
        content.pad(20, 70, 20, 70);

        float cellMinWidth = minWidth - content.getPadLeft() - content.getPadRight();

        Table rows = new Table();

        Label.LabelStyle style = new Label.LabelStyle(
                screen.game.fontProvider.getFont(FontProvider.h4),
                Color.BLACK
        );

        for (String sentence : sentences) {
            Label label = new Label(sentence, style);
            label.setAlignment(Align.center);
            label.setWrap(true);

            rows.add(label)
                    .minWidth(cellMinWidth - 40)
                    .padBottom(10)
                    .row();
        }

        FitImage img = new FitImage(
                screen.assetManager.get("menu/b_full_version_2.png", Texture.class)
        );

        Table columns = new Table();

        columns.add(img)
                .height(MunhauzenGame.WORLD_HEIGHT * .2f)
                .minWidth(cellMinWidth)
                .center()
                .row();

        columns.add(rows)
                .minWidth(cellMinWidth)
                .center().row();

        Table buttons = new Table();
        buttons.add(getActionBtn())
                .width(MunhauzenGame.WORLD_WIDTH * .35f)
                .height(MunhauzenGame.WORLD_HEIGHT * .08f);

        content.add(columns).row();
        content.add(buttons).row();

        return content;
    }

    private Actor getActionBtn() {

        return screen.game.buttonBuilder.primary("Feedback", new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                try {
                    fragment.onOkClicked();

                } catch (Throwable e) {
                    Log.e(tag, e);
                }
            }
        });

    }

}
