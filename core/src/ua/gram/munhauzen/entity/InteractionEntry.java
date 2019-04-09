package ua.gram.munhauzen.entity;

import ua.gram.munhauzen.history.Entry;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class InteractionEntry extends Entry {

    public String type;
    public boolean isCompleted;

    public InteractionEntry() {
        super();
    }

    public InteractionEntry(String id) {
        super(id);
    }
}
