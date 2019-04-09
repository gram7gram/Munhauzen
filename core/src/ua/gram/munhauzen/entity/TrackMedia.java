package ua.gram.munhauzen.entity;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public abstract class TrackMedia extends Entity {

    public int startsAt;
    public int finishesAt;
    public boolean isLocked;
    public boolean isCompleted;
    public int progress;
    public TrackMedia previous;
    public TrackMedia next;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Entity) {
            return id.equals(((Entity) obj).id);
        } else return super.equals(obj);
    }

    abstract public int getDuration();
}
