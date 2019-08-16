package ua.gram.munhauzen.screen.painting.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.screen.PaintingScreen;

public class Statue extends Group {

    final PaintingScreen screen;
    public final Image item, statue;
    float statueWidth, statueHeight;
    StatueBanner statueBanner;

    public Statue(PaintingScreen screen) {
        super();

        this.screen = screen;

        statueBanner = new StatueBanner(screen, this);
        item = new Image();
        statue = new Image();

        addActor(statue);

        setStatueBackground(
                screen.assetManager.get("gallery/gv2_statue.png", Texture.class)
        );

        if (screen.paintingImage.isOpened) {
            addActor(statueBanner);
            addActor(item);

            setItemBackground(
                    screen.assetManager.get(screen.paintingImage.statueResource, Texture.class)
            );

            item.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);

                    if (statueBanner.isVisible()) {
                        statueBanner.fadeOut();
                    } else {
                        statueBanner.fadeIn();
                    }
                }
            });
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        statue.setPosition(10, 0);

        item.setPosition(
                statue.getX() + statueWidth * .1f,
                statue.getY() + statueHeight * .95f
        );

    }

    public void setStatueBackground(Texture texture) {

        SpriteDrawable drawable = new SpriteDrawable(new Sprite(texture));

        statue.setDrawable(drawable);

        statueWidth = MunhauzenGame.WORLD_WIDTH / 4f;
        float scale = 1f * statueWidth / drawable.getMinWidth();
        statueHeight = 1f * drawable.getMinHeight() * scale;

        statue.setSize(statueWidth, statueHeight);
    }

    public void setItemBackground(Texture texture) {

        SpriteDrawable drawable = new SpriteDrawable(new Sprite(texture));

        item.setDrawable(drawable);

        float width = statueWidth * .9f;
        float scale = 1f * width / drawable.getMinWidth();
        float height = 1f * drawable.getMinHeight() * scale;

        item.setSize(width, height);

    }
}
