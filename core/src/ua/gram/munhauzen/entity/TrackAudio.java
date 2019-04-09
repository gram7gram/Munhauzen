package ua.gram.munhauzen.entity;

import com.badlogic.gdx.audio.Music;

public class TrackAudio extends TrackMedia {

    public int duration;
    public Music player;
    public boolean isPrepared;
    public boolean isPreparing;
    public long startedLoadingAt;
    public long completedLoadingAt;

    @Override
    public int getDuration() {
        return duration;
    }

}
