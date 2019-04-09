package ua.gram.munhauzen.history;

import com.badlogic.gdx.audio.Music;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class PlayerEntry {
    private final int index;
    private final int progress;
    private final int duration;
    private final Music player;

    public PlayerEntry(int index, int progress, int duration, Music player) {
        this.index = index;
        this.progress = progress;
        this.duration = duration;
        this.player = player;
    }

    public int getProgress() {
        return progress;
    }

    public int getDuration() {
        return duration;
    }

    public Music getPlayer() {
        return player;
    }

    public int getIndex() {
        return index;
    }
}
