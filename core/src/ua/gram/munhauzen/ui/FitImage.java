package ua.gram.munhauzen.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Scaling;


/**
 * @author Gram <gram7gram@gmail.com>
 */
public class FitImage extends Image {

    public FitImage() {
        super();
        setScaling(Scaling.fit);
    }

    public FitImage(Texture texture) {
        super(texture);
        setScaling(Scaling.fit);
    }

    public FitImage(Drawable texture) {
        super(texture);
        setScaling(Scaling.fit);
    }
}
