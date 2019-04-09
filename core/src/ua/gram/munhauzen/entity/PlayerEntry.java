package ua.gram.munhauzen.entity;

import com.badlogic.gdx.audio.Music;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class PlayerEntry {
    public int index;
    public int progress;
    public int duration;
    public Music player;

    public PlayerEntry(int index, int progress, int duration, Music player) {
        this.index = index;
        this.progress = progress;
        this.duration = duration;
        this.player = player;
    }
}

