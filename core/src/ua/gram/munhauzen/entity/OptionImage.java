package ua.gram.munhauzen.entity;

import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

public class OptionImage extends OptionMedia<OptionImage> {

    public int duration;
    public float width, height;
    public SpriteDrawable image;
    public String interaction;
    public String transition;

    public String getResource() {
        return "images/" + id + ".jpg";
    }
}
