package ua.gram.munhauzen.interaction.timer2.animation;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

import ua.gram.munhauzen.MunhauzenGame;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class Bar extends Image {

    final Image bomb, spark;

    public Bar(Image bomb, Image spark) {

        Pixmap pix = new Pixmap(3, 3, Pixmap.Format.RGB888);
        pix.setColor(Color.BLACK);
        pix.fill();

        setDrawable(new SpriteDrawable(new Sprite(new Texture(pix))));

        setSize(10, MunhauzenGame.WORLD_HEIGHT / 2f);

        this.bomb = bomb;
        this.spark = spark;
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        float bombCenterX = bomb.getX() + bomb.getWidth() / 2f;
        float bombCenterY = bomb.getY() + bomb.getHeight() / 2f;

        float sparkCenterY = spark.getY() + spark.getHeight() / 2f;

        setPosition(bombCenterX, bombCenterY);
        setHeight(sparkCenterY - bombCenterY);
    }
}
