package ua.gram.munhauzen.history;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class InteractionEntry extends Entry {

    public String type;
    public String option;
    public boolean isCompleted;

    @SuppressWarnings("unused")
    public InteractionEntry() {
        super();
    }

    public InteractionEntry(String id) {
        super(id);
    }
}
