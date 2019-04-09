package ua.gram.munhauzen.history;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class ImageEntry extends Entry {

    public boolean isViewed;
    public boolean isShown;

    @SuppressWarnings("unused")
    public ImageEntry() {
        super();
    }

    public ImageEntry(String id) {
        super(id);
    }
}
