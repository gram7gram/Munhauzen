package ua.gram.munhauzen.interaction.hare.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import ua.gram.munhauzen.MunhauzenGame;
import ua.gram.munhauzen.ui.FitImage;
import ua.gram.munhauzen.utils.Log;
import ua.gram.munhauzen.utils.Random;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class Misc extends FitImage {

    Actor origin;
    ShapeRenderer sr;

    int width, height, x, y;

    public Misc(Texture texture, Actor origin, int width, int height, int x, int y) {
        super(texture);

        this.origin = origin;
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;

        addAction(
                Actions.forever(Actions.rotateBy(-90, new Random().between(9, 14)))
        );

        setSize(width, height);
        setPosition(x, y);

        sr = new ShapeRenderer();
        sr.setAutoShapeType(true);
        sr.setColor(Color.RED);

    }


    @Override
    public void draw(Batch batch, float parentAlpha) {

        super.draw(batch, parentAlpha);

        batch.end();

        sr.setProjectionMatrix(batch.getProjectionMatrix());
        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.line(
                getOriginX(), getOriginY(),
                getX() + getWidth() / 2f, getY() + getHeight() / 2f
                );
        sr.end();

        batch.begin();
    }

    @Override
    public void act(float delta) {
        super.act(delta);


        setSize(width, height);
        //setPosition(x, y);
        setOrigin(origin.getOriginX(), origin.getOriginY());

    }
}
