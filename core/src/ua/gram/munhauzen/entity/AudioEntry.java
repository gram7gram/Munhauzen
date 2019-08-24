package ua.gram.munhauzen.entity;

import ua.gram.munhauzen.history.Entry;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class AudioEntry extends Entry {

    public String audio;
    public boolean isViewed;
    public boolean isPlaying;
    public boolean isPaused;

    public AudioEntry() {
        super();
    }

    public AudioEntry(String id) {
        super(id);
    }
}
