package ua.gram.munhauzen.entity;

import com.badlogic.gdx.audio.Music;

public class StoryAudio extends StoryMedia<StoryAudio> {

    public String audio;
    public int duration;
    public Music player;

    public String getResource() {
        return "audio/" + audio + ".ogg";
    }
}
