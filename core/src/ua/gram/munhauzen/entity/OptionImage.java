package ua.gram.munhauzen.entity;

import com.badlogic.gdx.graphics.Texture;

public class OptionImage extends OptionMedia {
    public int duration;
    public Texture image;
    public String interaction;
    public String transition;

    public String getResource() {
        return "images/" + id + ".jpg";
    }
}
