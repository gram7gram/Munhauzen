package ua.gram.munhauzen.entity;

import com.badlogic.gdx.audio.Music;

public class OptionAudio extends OptionMedia {

    public int duration;
    public Music player;

    public String getResource() {
        return "audio/" + id + ".ogg";
    }
}
