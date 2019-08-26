package ua.gram.munhauzen.entity;

import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class StoryImage extends StoryMedia<StoryImage> {

    @JsonProperty
    public String image;
    @JsonProperty
    public int duration;
    @JsonProperty
    public float width, height;
    @JsonIgnore
    public SpriteDrawable drawable;
    @JsonProperty
    public String transition;
}
