package ua.gram.munhauzen.history;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class AudioEntry extends Entry {

    public boolean isViewed;
    public boolean isPlaying;
    public boolean isPaused;

    @SuppressWarnings("unused")
    public AudioEntry() {
        super();
    }

    public AudioEntry(String id) {
        super(id);
    }
}
