package ua.gram.munhauzen.screen.game.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.entity.Inventory;
import ua.gram.munhauzen.screen.GameScreen;

public class Statue extends Group {

    final GameScreen screen;
    public final Image item, statue;
    final StarsAnimation stars;
    float statueWidth, statueHeight;
    float itemWidth, itemHeight;

    public float height;

    public Statue(GameScreen screen, Inventory inventory) {

        this.screen = screen;

        item = new Image();
        statue = new Image();
        stars = new StarsAnimation(screen);

        addActor(statue);
        addActor(item);
        addActor(stars);

        setStatueBackground(
                screen.assetManager.get("gallery/gv2_statue.png", Texture.class)
        );

        setItemBackground(
                screen.assetManager.get(inventory.statueImage, Texture.class)
        );

        setStarsBackground();

        height = statueHeight + itemHeight;

        stars.start();
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        statue.setPosition(MunhauzenGame.WORLD_WIDTH * .05f, 0);

        item.setPosition(
                statue.getX(),
                statue.getY() + statueHeight * .95f
        );

        stars.setPosition(
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

        itemWidth = statueWidth;
        float scale = 1f * itemWidth / drawable.getMinWidth();
        itemHeight = 1f * drawable.getMinHeight() * scale;

        item.setSize(itemWidth, itemHeight);

    }

    public void setStarsBackground() {

        Drawable drawable = stars.getCurrentDrawable();

        float width = statueWidth;
        float scale = 1f * width / drawable.getMinWidth();
        float height = 1f * drawable.getMinHeight() * scale;

        stars.setSize(width, height);

    }
}
