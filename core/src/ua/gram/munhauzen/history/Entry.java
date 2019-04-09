package ua.gram.munhauzen.history;

import ua.gram.munhauzen.utils.DateUtils;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class Entry {

    public String name;
    public String createdAt;

    @SuppressWarnings("unused")
    public Entry() {
        createdAt = DateUtils.now();
    }

    public Entry(String name) {
        this();
        this.name = name;
    }

    @Override
    public String toString() {
        return createdAt + ": " + name;
    }
}
