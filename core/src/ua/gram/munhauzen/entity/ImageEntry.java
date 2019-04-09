package ua.gram.munhauzen.entity;

import ua.gram.munhauzen.history.Entry;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class ImageEntry extends Entry {

    public boolean isViewed;
    public boolean isShown;

    public ImageEntry() {
        super();
    }

    public ImageEntry(String id) {
        super(id);
    }
}
