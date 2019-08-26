package ua.gram.munhauzen.entity;

import com.badlogic.gdx.audio.Music;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class StoryAudio extends StoryMedia<StoryAudio> {

    @JsonProperty
    public String audio;
    @JsonProperty
    public int duration;
    @JsonIgnore
    public Music player;
}
