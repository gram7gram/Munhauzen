package ua.gram.munhauzen.entity;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public abstract class OptionMedia extends Entity {

    public int startsAt;
    public int finishesAt;
    public boolean isLocked;
    public boolean isCompleted;
    public int progress;
    public OptionMedia previous;
    public OptionMedia next;
}
