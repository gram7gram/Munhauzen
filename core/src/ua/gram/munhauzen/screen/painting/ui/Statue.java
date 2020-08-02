package ua.gram.munhauzen.screen.painting.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.screen.PaintingScreen;
import ua.gram.munhauzen.screen.gallery.entity.PaintingImage;

public class Statue extends Group {

    final PaintingScreen screen;
    public final Image item, statue;
    float statueWidth, statueHeight;
    StatueBanner statueBanner;

    public Statue(final PaintingScreen screen) {
        super();

        this.screen = screen;

        PaintingImage img = screen.paintingFragment.paintingImage;

        statueBanner = new StatueBanner(screen, this);
        item = new Image();
        statue = new Image();

        addActor(statue);

        setStatueBackground(
                screen.paintingFragment.assetManager.get("gallery/gv2_statue.png", Texture.class)
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

        if (img.canOpenLink()) {
            statueBanner.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);

                    screen.openAdultGateBanner(new Runnable() {
                        @Override
                        public void run() {
                            Gdx.net.openURI(screen.game.params.statueLink);
                        }
                    });
                }
            });
        }

        if (img.canDisplayStatueItem()) {

            addActor(statueBanner);
            addActor(item);

            setItemBackground(
                    screen.paintingFragment.assetManager.get(img.statueResource, Texture.class)
            );
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        statue.setPosition(MunhauzenGame.WORLD_WIDTH * .05f, 0);

        item.setPosition(
                statue.getX(),
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

        float width = statueWidth;
        float scale = 1f * width / drawable.getMinWidth();
        float height = 1f * drawable.getMinHeight() * scale;

        item.setSize(width, height);

    }
}
